package layout;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import za.co.devj.projectsv2.R;
import za.co.devj.projectsv2.utils.Validations;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForgotPasswordFragment extends Fragment implements View.OnClickListener
{

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private EditText edt_email;

    private TextInputLayout tilErr;

    private Button btn_reset;

    //ImageButton
    private ImageButton img_back;

    public ForgotPasswordFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        //Initialise ImageButton
        img_back = (ImageButton) v.findViewById(R.id.img_btn_back);

        edt_email = (EditText) v.findViewById(R.id.edt_forgot_pass);
        tilErr = (TextInputLayout) v.findViewById(R.id.til_logEmail);
        btn_reset = (Button) v.findViewById(R.id.btn_submit);

        mAuth = FirebaseAuth.getInstance();

        btn_reset.setOnClickListener(this);
        img_back.setOnClickListener(this);


        mAuthListener = new FirebaseAuth.AuthStateListener()
        {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
            {
                FirebaseUser u = mAuth.getCurrentUser();

                if(u != null)
                {
                    Log.d("AUTH :", "Success, user found");
                }
                else
                {
                    Log.d("AUTH :", "Fail, User does not exist within the database");
                }
            }
        };

        return v;
    }

    @Override
    public void onClick(View view)
    {
        int btn_id = view.getId();

        Validations val = new Validations();

        String email = edt_email.getText().toString();
        boolean valid = false;

        if(btn_id == R.id.img_btn_back)
        {
            SignIn signIn = new SignIn();

            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            ft.add(R.id.container,signIn);
            ft.commit();

        }
        else if(btn_id == R.id.btn_submit)
        {
            if(TextUtils.isEmpty(email))
            {
                tilErr.setError("Please provide an email");
            }
            else
            {
                valid = val.isValidEmail(email);
            }

            if (valid)
            {
                resetPassword(email);
            }
        }

    }

    //[Start of password reset using email]
    /**
     * @param email
     */
    private void resetPassword(String email)
    {
        if(mAuth != null)
        {
            //checks for email and sends reset instructions
            mAuth.sendPasswordResetEmail(email);

            Toast.makeText(getActivity(),"Reset instructions sent to " + email,Toast.LENGTH_LONG).show();

            //Switch to login fragment after email has been sent
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            SignIn signin = new SignIn();

            ft.add(R.id.container,signin);
            ft.commit();
        }
        else
        {
            Toast.makeText(getActivity(),"Email address does not exist " + email,Toast.LENGTH_LONG).show();
        }
    }
    //[End of password reset]
}
