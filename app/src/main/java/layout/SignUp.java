package layout;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import za.co.devj.projectsv2.MainActivity;
import za.co.devj.projectsv2.R;
import za.co.devj.projectsv2.pojo.Login;
import za.co.devj.projectsv2.pojo.user.User;
import za.co.devj.projectsv2.utils.ProgressClass;
import za.co.devj.projectsv2.utils.Validations;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUp extends android.app.Fragment implements View.OnClickListener
{

    //EditText global declaration
    private EditText edt_sign_in_email;
    private EditText edt_sign_in_password;
    private TextView tv_sign_up;

    View v;

    User u;

    //ImageButton
    private ImageButton img_back;

    //TextInputLayout global declaration
    private TextInputLayout til_email;
    private TextInputLayout til_pass;

    //Button global declaration
    private Button btn_google_signUp;
    private Button btn_on_signUp;

    //check for valid email and password
    private boolean valid_email;
    private boolean valid_password;

    private boolean isSucc;
    private boolean isIn;

    //Firebase global declaration
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private DatabaseReference db;

    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

    //Sign Up tag
    public final String TAG_SIGN_UP = "sign_up";

    //Progress class to start and stop progress dialog
    ProgressClass pc;

    //Login object
    Login l;

    public SignUp()
    {
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_sign_up, container, false);

        //Initialise ImageButton
        img_back = (ImageButton) v.findViewById(R.id.img_btn_back);

        //Initialise EditTexts
        edt_sign_in_email = (EditText) v.findViewById(R.id.edt_sign_in_email);
        edt_sign_in_password = (EditText) v.findViewById(R.id.edt_sign_in_password);
        tv_sign_up = (TextView) v.findViewById(R.id.edt_sign_up);

        //initialise buttons
        btn_on_signUp = (Button) v.findViewById(R.id.btn_email_sign_up);
        btn_google_signUp = (Button) v.findViewById(R.id.btn_google_sign_up);

        //initialise input layouts
        til_email = (TextInputLayout) v.findViewById(R.id.til_logEmail);
        til_pass = (TextInputLayout) v.findViewById(R.id.til_logPassword);

        //set On click listeners for the buttons
        btn_google_signUp.setOnClickListener(this);
        btn_on_signUp.setOnClickListener(this);
        img_back.setOnClickListener(this);

        mFirebaseAuth = FirebaseAuth.getInstance();

        //request email addresses from local email handler
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mAuthStateListener = new FirebaseAuth.AuthStateListener()
        {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
            {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if(user != null)
                {
                    // User is signed in

                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                }
                else
                {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };


        return v;
    }

    @Override
    public void onClick(View view)
    {
        int btn_id = view.getId();

        if(btn_id == R.id.img_btn_back)
        {
            SignIn signIn = new SignIn();

            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            ft.add(R.id.container,signIn);
            ft.commit();

        }
        else if(btn_id == R.id.btn_google_sign_up)
        {

            pc = new ProgressClass();
            pc.startProgressDialog(getActivity(),"Authenticating Account","Please wait...");

            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    signIn();
                }
            }).start();

        }
        else if(btn_id ==  R.id.btn_email_sign_up)
        {

            Validations val = new Validations();


            //get text from edittexts
            String email = edt_sign_in_email.getText().toString();
            String password = edt_sign_in_password.getText().toString();




            //check for empty string
            if(TextUtils.isEmpty(email))
            {
                til_email.setError("Enter an email address");
            }
            else
            {
                //validate email
                valid_email =  val.isValidEmail(email);

                til_email.setErrorEnabled(false);

                if(!valid_email)
                {
                    til_email.setError("Enter a valid email address");
                }
                else
                {
                    til_email.setError("");
                }
            }

            //check for password empty string
            if(TextUtils.isEmpty(password))
            {
                til_pass.setError("Enter a password");
            }
            else
            {
                //validate password
                valid_password = val.validPassword(password);

                til_pass.setError("");

                if(!valid_password)
                {
                    til_pass.setError("Enter a valid password");
                }
                else
                {
                    til_pass.setError("");
                }
            }

            if(valid_email && valid_password)
            {
                u = new User();

                pc = new ProgressClass();
                pc.startProgressDialog(getActivity(),"Creating Account","Please wait...");

                l = new Login(email,password);

                new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        createAccount(l);
                    }
                }).start();

                Handler h = new Handler();

               /* h.postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {

                        if(isSucc)
                        {

                        }
                        else
                        {
                            pc.stopProgressDialoge();
                            Toast.makeText(getActivity(), "Error creating account", Toast.LENGTH_LONG).show();
                        }
                    }
                },3000);*/

            }
        }

    }

    //Manually manage account connection
    @Override
    public void onStart()
    {
        super.onStart();

        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
        // mGoogleApiClient.connect();
    }

    @Override
    public void onStop()
    {
        super.onStop();

        if(mAuthStateListener != null)
        {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }

        // mGoogleApiClient.disconnect();
    }

    //Create account using email and password
    private void createAccount(Login l)
    {

        mFirebaseAuth.createUserWithEmailAndPassword(l.getEmail(),l.getPassword())
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if(!task.isSuccessful())
                        {
                            isSucc = false;
                            Log.e("Success : ", "NOT");
                            Toast.makeText(getActivity(),"Error creating account " + task.isSuccessful() ,Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            isSucc = true;

                            db = FirebaseDatabase.getInstance().getReference();

                            db.child("User").child(mFirebaseAuth.getCurrentUser().getUid()).setValue(u);

                            pc.stopProgressDialoge();

                           Intent i = new Intent(getActivity(), MainActivity.class);
                            startActivity(i);

                            Log.e("Success : ", "Yep");
                            // Toast.makeText(getActivity(),"Account Created Successfully " + task.isSuccessful(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    //end of...

    //Create account ausing gmail
    private void signIn()
    {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {


        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN)
        {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess())
            {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();

                pc.stopProgressDialoge();

                pc = new ProgressClass();
                pc.startProgressDialog(getActivity(),"Creating Account","Please wait...");

                firebaseAuthWithGoogle(account);

            }
            else
            {
                // Google Sign In failed, update UI appropriately
                updateUI(null);
            }
        }


    }

    //[End onActivityResult]

    //[start auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct)
    {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
        //showProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        mFirebaseAuth.signInWithCredential(credential).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                // If sign in fails, display a message to the user. If sign in succeeds
                // the auth state listener will be notified and logic to handle the
                // signed in user can be handled in the listener.

                if(!task.isSuccessful())
                {
                    Log.w(TAG, "signInWithCredential", task.getException());
                    Toast.makeText(getActivity(), "Authentication failed.",Toast.LENGTH_SHORT).show();
                    pc.stopProgressDialoge();
                }
                else
                {
                    isIn = true;
                    if(isIn)
                    {
                        db = FirebaseDatabase.getInstance().getReference();

                        db.child("User").child(mFirebaseAuth.getCurrentUser().getUid()).setValue(u);

                        ProfileSetup prf = new ProfileSetup();

                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();

                        ft.add(R.id.container,prf);
                        ft.commit();
                    }

                    pc.stopProgressDialoge();
                }

                // [START_EXCLUDE]
                // hideProgressDialog();
                // ;
                // [END_EXCLUDE]
            }
        });
    }


    private void updateUI(FirebaseUser user)
    {
        if(user != null)
        {
            Toast.makeText(getActivity(),"User : " + user.getEmail() + " \n ID : " + user.getUid(),Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(getActivity(),"Signed Out",Toast.LENGTH_LONG).show();
        }
    }



}
