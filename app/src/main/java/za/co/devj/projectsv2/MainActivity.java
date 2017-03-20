package za.co.devj.projectsv2;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import layout.ProfileSetup;
import za.co.devj.projectsv2.pojo.user.User;
import za.co.devj.projectsv2.pojo.userEvents.UserEvents;

public class MainActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener
{

    public static int SPLASH_TIME_OUT = 2000;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseUser mFirebaseUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDataRef;
    private StorageReference storageRef;

    private NavigationView navigationView;

    private static MenuItem item;
    private static MenuItem nav_update;
    private TextView user_name;
    private TextView user_email;
    private ImageView img_view;

    FloatingActionButton fab;

    private Uri pic_uri;
    private String uID;

    private String[] users_selected_services;

    //navigation menu items
    private MenuItem itm_accom;
    private MenuItem itm_trans;
    private MenuItem itm_cater;
    private MenuItem itm_cloth;
    private MenuItem itm_cake;
    private MenuItem itm_decor;
    private MenuItem itm_mesc;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
               startActivity(new Intent(MainActivity.this,NewEventActivity.class));
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //navigation menu
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //navigation header items
        View v = navigationView.getHeaderView(0);

        img_view = (ImageView) v.findViewById(R.id.profile_image);
        user_name = (TextView) v.findViewById(R.id.user_name);
        user_email = (TextView) v.findViewById(R.id.user_email);

        //initialize menu items
        itm_accom = navigationView.getMenu().findItem(R.id.nav_accom);
        itm_trans = navigationView.getMenu().findItem(R.id.nav_tarvel);
        itm_cater = navigationView.getMenu().findItem(R.id.nav_food);
        itm_cake = navigationView.getMenu().findItem(R.id.nav_cakes);
        itm_decor = navigationView.getMenu().findItem(R.id.nav_deco);
        itm_mesc = navigationView.getMenu().findItem(R.id.nav_mes);

        item = navigationView.getMenu().findItem(R.id.signin);
        nav_update = navigationView.getMenu().findItem(R.id.nav_update);
        nav_update.setVisible(false);


        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference();


        //request email addresses from local email handler
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mAuthStateListener = new FirebaseAuth.AuthStateListener()
        {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
            {
                 mFirebaseUser = firebaseAuth.getCurrentUser();

                if(mFirebaseUser != null)
                {
                    String email = mFirebaseUser.getEmail();
                    String name = mFirebaseUser.getDisplayName();

                    uID = mFirebaseUser.getUid();
                    pic_uri = mFirebaseUser.getPhotoUrl();

                    user_name.setText(name);
                    user_email.setText(email);
                    item.setIcon(R.drawable.img_logout);
                    item.setTitle("Sign out");

                    nav_update.setVisible(true);

                    fab.setVisibility(View.VISIBLE);

                    getCustomEventData();

                }
                else
                {
                    Log.e("STEP 6", "USER NOT FOUND");

                    user_name.setText("meh");
                    user_email.setText("Meh");

                    item.setIcon(R.drawable.img_login);

                    item.setTitle("Sign in");

                    fab.setVisibility(View.GONE);

                }
            }
        };
    }

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_signin)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_accom)
        {
            // Handle the camera action
        }
        else if (id == R.id.nav_tarvel)
        {

        }
        else if (id == R.id.nav_food)
        {

        }
        else if (id == R.id.nav_share)
        {

        }
        else if (id == R.id.nav_update)
        {
            startActivity(new Intent(MainActivity.this, UpdateProfileActivity.class));
        }
        else if(id == R.id.signin)
        {
            String text = item.getTitle().toString();

            if(text.equalsIgnoreCase("Sign In"))
            {

                startActivity(new Intent(MainActivity.this, SignInActivity.class));

            }
            else if(text.equalsIgnoreCase("Sign Out"))
            {
                signOut();

            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        mGoogleApiClient.connect();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);

    }

    @Override
    protected void onStop()
    {
        super.onStop();

        if(mAuthStateListener != null)
        {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    private void signOut()
    {
        mFirebaseAuth.signOut();

    }

    //[customize layout here]
    public void getCustomEventData()
    {
        mDatabase = FirebaseDatabase.getInstance();
        mDataRef = mDatabase.getReference().child("UserEvents").child(uID);

        mDataRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {

                if(dataSnapshot != null)
                {
                    UserEvents data = dataSnapshot.getValue(UserEvents.class);

                    String serv = data.getServices();

                    Log.e("Event Stuff over here", " " + data.getServices());

                    users_selected_services = serv.split(", ");

                    //customize menu here
                    customizeMenu();
                }
                else
                {
                    Toast.makeText(MainActivity.this,"You have no events yet",Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
    }

    public void customizeMenu()
    {
        for(int x  = 0; x < users_selected_services.length;x++)
        {
            if(users_selected_services[x].equalsIgnoreCase("Accommodation"))
            {
                itm_accom.setVisible(true);
            }
            else if(users_selected_services[x].equalsIgnoreCase("Transport"))
            {
                itm_trans.setVisible(true);
            }
            else if(users_selected_services[x].equalsIgnoreCase("Catering"))
            {
                itm_cater.setVisible(true);
            }
            else if(users_selected_services[x].equalsIgnoreCase("Decor"))
            {
                itm_decor.setVisible(true);
            }
            else if(users_selected_services[x].equalsIgnoreCase("Cake"))
            {
                itm_cake.setVisible(true);
            }
            else if(users_selected_services[x].equalsIgnoreCase("Miscellaneous"))
            {
                itm_mesc.setVisible(true);
            }
        }
    }
}
