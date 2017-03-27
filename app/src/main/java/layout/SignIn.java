package layout;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import za.co.devj.projectsv2.AddNewEventToDBActivity;
import za.co.devj.projectsv2.MainActivity;
import za.co.devj.projectsv2.R;
import za.co.devj.projectsv2.pojo.Login;
import za.co.devj.projectsv2.utils.ProgressClass;
import za.co.devj.projectsv2.utils.Validations;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignIn extends android.app.Fragment implements View.OnClickListener
{
    //global edit text variables
    private EditText edt_email;
    private EditText edt_password;

    //global button variables
    private Button btn_email_signIn;
    private Button btn_google_signIn;

    //TextInputLayout global declaration
    private TextInputLayout til_email;
    private TextInputLayout til_pass;

    //Global TextView variables
    private TextView tv_sign_up;
    private TextView tv_fog_pass;

    //validation booleans
    private boolean valid_email;
    private boolean valid_password;

    private boolean isSucc;

    //Sign Up tag
    public final String TAG_SIGN_UP = "sign_up";

    //google auth
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

    //Progress class to start and stop progress dialog
    ProgressClass pc;


    public SignIn()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);

        //Initialize editTexts
        edt_email = (EditText) view.findViewById(R.id.edt_sign_in_email);
        edt_password = (EditText) view.findViewById(R.id.edt_sign_in_password);

        //initialise input layouts
        til_email = (TextInputLayout) view.findViewById(R.id.til_logEmail);
        til_pass = (TextInputLayout) view.findViewById(R.id.til_logPassword);

        //Initialize buttons
        btn_email_signIn = (Button) view.findViewById(R.id.btn_email_sign_in);
        btn_google_signIn = (Button) view.findViewById(R.id.btn_google_sign_in);

        //Initialize Views
        tv_sign_up = (TextView) view.findViewById(R.id.edt_sign_up);
        tv_fog_pass = (TextView) view.findViewById(R.id.tv_forgot_pass);

        tv_sign_up.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();

                SignUp frag_signUp = new SignUp();

                ft.add(R.id.container,frag_signUp,TAG_SIGN_UP);
                ft.commit();
                return false;
            }
        });

        tv_fog_pass.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();

               ForgotPasswordFragment fp = new ForgotPasswordFragment();

                ft.add(R.id.container,fp,TAG_SIGN_UP);
                ft.commit();

                return false;
            }
        });

        //Check for button onClick listener
        btn_email_signIn.setOnClickListener(this);
        btn_google_signIn.setOnClickListener(this);

        //Get firebase instance
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

                Log.e("0000 : "," Checking user results");

                if(user != null)
                {
                    Log.d("MADE_IT_IN" , "Signed in :" + user.getUid());

                    String email = user.getEmail();

                    /*if(email.equalsIgnoreCase("meh@meh.com"))
                    {
                        startActivity(new Intent(getActivity(), AddNewEventToDBActivity.class));
                        getActivity().finish();
                    }
                    else
                    {

                    }*/

                    startActivity(new Intent(getActivity(), MainActivity.class));
                    getActivity().finish();

                }
                else
                {
                    Log.d("MADE_IT_OUT" , "Sign out :");
                }
            }
        };

        return view;
    }

    @Override
    public void onClick(View view)
    {
        int btn_id = view.getId();

        if (btn_id == R.id.btn_email_sign_in)
        {

            Validations val = new Validations();
            Login l;

            //get text from edittexts
            String email = edt_email.getText().toString();
            String password = edt_password.getText().toString();

            //validate email and password
            valid_email =  val.isValidEmail(email);
            valid_password = val.validPassword(password);

            //check for empty string
            if(email.equals(""))
            {
                til_email.setError("Enter an email address");
            }
            else
            {
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
            if(password.equals(""))
            {
                til_pass.setError("Enter a password");
            }
            else
            {
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
                pc = new ProgressClass();
                pc.startProgressDialog(getActivity(),"Signing In","Please wait...");

                l = new Login(email,password);

                onSignIn(l);

                Handler h = new Handler();

                h.postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {

                        pc.stopProgressDialoge();

                        if(isSucc)
                        {
                            startActivity(new Intent(getActivity(), MainActivity.class));

                            getActivity().finish();
                        }
                        else
                        {
                            Toast.makeText(getActivity(), "Account not Registered", Toast.LENGTH_LONG).show();
                        }
                    }
                },3000);

            }

        }
        else if(btn_id == R.id.btn_google_sign_in)
        {

            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    Log.e("Signing in"," here");
                    onSignIn();
                }
            }).start();

        }
    }

    //[Start of signing in]
    private void onSignIn(Login l)
    {
        Log.d("SIGNING IN", "Signing in with " + l.getEmail());

        mFirebaseAuth.signInWithEmailAndPassword(l.getEmail(),l.getPassword())
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        Log.d("SIGNING IN COMPLETE", "Signing in complete ");

                        if(!task.isSuccessful())
                        {
                            Log.d("SIGNING IN Failed", "Signing in failed ");

                            Toast.makeText(getActivity(),"Login Failed", Toast.LENGTH_LONG).show();

                           // isSucc = false;

                        }
                        else
                        {
                            //isSucc = true;


                        }
                    }
                });
    }
    //[End of email sign in]

    //[Start of google Sign in]
    private void onSignIn()
    {
        Log.e("2 : "," Getting google intent");
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent,RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e("3 : "," Checking request code");
        if(requestCode == RC_SIGN_IN)
        {
            Log.e("4 : "," Getting google results");
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            if(result.isSuccess())
            {
                Log.e("5 : "," Getting firebase account with google account results");

                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            }
            else
            {
                Log.e("6 : "," results not found");
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct)
    {
        Log.e("GOOGLE ACCT SIGN IN", "Signing in with Google Account " + acct.getId());

        pc = new ProgressClass();
        pc.startProgressDialog(getActivity(),"Signing In","Please wait...");

        AuthCredential cred = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        mFirebaseAuth.signInWithCredential(cred)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        Log.e("GOOGLE SIGN IN COMPLETE", "Signing in complete ");

                        if(!task.isSuccessful())
                        {
                            Log.e("SIGNING IN FAILED", "Signing in failed ");

                            Toast.makeText(getActivity(),"Login Failed", Toast.LENGTH_LONG).show();

                            isSucc = false;

                        }
                        else
                        {
                            isSucc = true;

                            Toast.makeText(getActivity(),"Login Success", Toast.LENGTH_LONG).show();
                        }
                    }
                });

        pc.stopProgressDialoge();
    }
    //[End of Google Sign in]

    //OnStart
    @Override
    public void onStart()
    {
        super.onStart();

        mFirebaseAuth.addAuthStateListener(mAuthStateListener);

    }

    //OnStop
    @Override
    public void onStop()
    {
        super.onStop();
        if(mAuthStateListener == null)
        {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
    }
}

