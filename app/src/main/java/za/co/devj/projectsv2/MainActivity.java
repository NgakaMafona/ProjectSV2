package za.co.devj.projectsv2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import za.co.devj.projectsv2.adapters.AdViewHolder;
import za.co.devj.projectsv2.adapters.FeedsAdapter;
import za.co.devj.projectsv2.pojo.feedClass.Feeds;
import za.co.devj.projectsv2.pojo.services.Services;
import za.co.devj.projectsv2.pojo.user.User;
import za.co.devj.projectsv2.pojo.userEvents.UserEvents;
import za.co.devj.projectsv2.ui.GoHttp;
import za.co.devj.projectsv2.utils.Constants;

public class MainActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener
{

    public static int SPLASH_TIME_OUT = 2000;

    private View v_header;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseUser mFirebaseUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDataRef;
    private StorageReference storageRef;

    FirebaseRecyclerAdapter<Services,AdViewHolder> adAdapter;

    private TextView user_name;
    private TextView user_email;
    private ImageView profile_image;

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

    private MenuItem itm_ent;
    private MenuItem itm_trv;
    private MenuItem itm_food;

    private MenuItem nav_editEvent;
    private static MenuItem item;
    private static MenuItem nav_update;

    //feed recucler view
    private RecyclerView rv;

    //Feed url
    private String feed_url;
    private ArrayList<Feeds> feeds;
    private FeedsAdapter feeds_adapter;

    private SwipeRefreshLayout swipeRefreshLayout;


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

        rv = (RecyclerView) findViewById(R.id.feed_rec_view);

        //navigation menu
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //navigation header items
        v_header = navigationView.getHeaderView(0);

        profile_image = (ImageView) v_header.findViewById(R.id.profile_image);
        user_name = (TextView) v_header.findViewById(R.id.user_name);
        user_email = (TextView) v_header.findViewById(R.id.user_email);



        //initialize menu items
        itm_accom = navigationView.getMenu().findItem(R.id.nav_accom);
        itm_trans = navigationView.getMenu().findItem(R.id.nav_tarvel);
        itm_cater = navigationView.getMenu().findItem(R.id.nav_food);
        itm_cake = navigationView.getMenu().findItem(R.id.nav_cakes);
        itm_decor = navigationView.getMenu().findItem(R.id.nav_deco);
        itm_mesc = navigationView.getMenu().findItem(R.id.nav_mes);

        itm_ent = navigationView.getMenu().findItem(R.id.nav_entertainment_feed);
        itm_trv = navigationView.getMenu().findItem(R.id.nav_travel_feed);
        itm_food = navigationView.getMenu().findItem(R.id.nav_food_feed);

        item = navigationView.getMenu().findItem(R.id.signin);
        nav_update = navigationView.getMenu().findItem(R.id.nav_update);
        nav_update.setVisible(false);
        nav_editEvent = navigationView.getMenu().findItem(R.id.nav_edt_ev);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.content_main);

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

                    getDP();

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

                    itm_accom.setVisible(false);
                    itm_trans.setVisible(false);
                    itm_cater.setVisible(false);
                    itm_decor.setVisible(false);
                    itm_cake.setVisible(false);
                    itm_mesc.setVisible(false);
                    nav_editEvent.setVisible(false);


                    itm_food.setVisible(true);
                    itm_ent.setVisible(true);
                    itm_trv.setVisible(true);

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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_accom)
        {
           //get Accommodation ads
            mDataRef = FirebaseDatabase.getInstance().getReference().child(Constants.TAG_NAV_ACCOM);

            getServiceAds();

            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
            {
                @Override
                public void onRefresh()
                {
                    adAdapter.notifyDataSetChanged();
                    rv.setAdapter(adAdapter);
                }
            });
            swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        }
        else if (id == R.id.nav_tarvel)
        {
            //get Transport ads
            mDataRef = FirebaseDatabase.getInstance().getReference().child(Constants.TAG_NAV_TRANS);

            getServiceAds();

            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
            {
                @Override
                public void onRefresh()
                {
                    adAdapter.notifyDataSetChanged();
                    rv.setAdapter(adAdapter);
                }
            });
            swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        }
        else if (id == R.id.nav_food)
        {
            //get Catering ads
            mDataRef = FirebaseDatabase.getInstance().getReference().child(Constants.TAG_NAV_CATER);

            getServiceAds();

            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
            {
                @Override
                public void onRefresh()
                {
                    adAdapter.notifyDataSetChanged();
                    rv.setAdapter(adAdapter);
                }
            });
            swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        }
        else if (id == R.id.nav_share)
        {

        }
        else if (id ==  R.id.nav_edt_ev)
        {
            startActivity(new Intent(MainActivity.this,EditEventActivity.class));
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
        else if (id == R.id.nav_entertainment_feed)
        {
            //get entertainment news feeds
            feed_url = Constants.FEED_ENT;
            getFeed(feed_url);

            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
            {
                @Override
                public void onRefresh()
                {
                    feeds_adapter.notifyDataSetChanged();
                    rv.setAdapter(feeds_adapter);
                }
            });
            swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
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

                    nav_editEvent.setVisible(true);
                }
                else
                {
                    Toast.makeText(MainActivity.this,"You have no array_serv yet",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
    }
    //end

    //Customize menu items visibility
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
    //end

    //Get current news feeds
    public ArrayList<Feeds> getFeeds(String data) throws JSONException
    {
        ArrayList<Feeds> feeds_list = new ArrayList<>();

        JSONObject jObject = new JSONObject(data);
        JSONArray artiObject = jObject.getJSONArray("articles");

        for(int x = 0; x < artiObject.length(); x++)
        {
            Feeds feeds = new Feeds();

            JSONObject j = artiObject.getJSONObject(x);

            feeds.setAuthor(j.getString("author"));
            feeds.setTitle(j.getString("title"));
            feeds.setDesc(j.getString("description"));
            feeds.setUrl(j.getString("url"));
            feeds.setImgUrl(j.getString("urlToImage"));
            feeds.setPublishDate(j.getString("publishedAt"));

            feeds_list.add(feeds);

            Log.e("Added " + x + " to list ", " Success");
        }

        return feeds_list;
    }
    //end

    public void getFeed(String url)
    {
        String u = url;

        feeds = new ArrayList<>();

        try
        {
            String value = new GoHttp().execute(u).get();

            Log.e("DATA ",value);

            feeds = getFeeds(value);

            feeds_adapter = new FeedsAdapter(feeds);
            rv.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            rv.setItemAnimator(new DefaultItemAnimator());
            feeds_adapter.notifyDataSetChanged();
            rv.setAdapter(feeds_adapter);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        catch (ExecutionException e)
        {
            e.printStackTrace();
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        //end
    }

    //get profile pic
    public void getDP()
    {
        mFirebaseUser  = mFirebaseAuth.getCurrentUser();


        mDataRef = FirebaseDatabase.getInstance().getReference().child("User").child(mFirebaseUser.getUid());

        mDataRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                User u = dataSnapshot.getValue(User.class);

                if(u.getU_pro_pic_loc() != null)
                {
                    Picasso.with(MainActivity.this).load(u.getU_pro_pic_loc()).into(profile_image);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
    }

    //[Start og getting ads]
    public void getServiceAds()
    {
        adAdapter = new FirebaseRecyclerAdapter<Services, AdViewHolder>
                (Services.class,R.layout.ads_card,AdViewHolder.class,mDataRef)
        {
            @Override
            protected void populateViewHolder(AdViewHolder viewHolder, Services model, int position)
            {
                viewHolder.setAd_image(getApplicationContext(),model.getServe_img());
                viewHolder.setAd_name(model.getServe_name());
                viewHolder.setAd_desc(model.getServe_desc());
                viewHolder.setAd_rating(model.getServe_rating());
            }
        };

        adAdapter.notifyDataSetChanged();
        rv.setAdapter(adAdapter);
    }
    //[End og getting ads]

}
