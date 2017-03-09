package layout;


import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import butterknife.BindView;
import za.co.devj.projectsv2.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileSetup extends Fragment implements RadioGroup.OnCheckedChangeListener, View.OnClickListener
{

    //Bind edit text views
    EditText first_name;
    EditText last_name;
    EditText id_number;

    //Bind RadioGroup
    @BindView(R.id.rdg_gender)RadioGroup rdg_gender;

    Button btn_create;

    TextInputLayout til_name;
    TextInputLayout til_surname;
    TextInputLayout til_idNumber;

    String gender;
    String name;
    String surname;
    String str_id;

    private DatabaseReference db;
    private FirebaseAuth mFirebaseAuth;

    public ProfileSetup()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile_setup, container, false);

        /*first_name = (EditText) v.findViewById(R.id.edt_pro_firstname);
        last_name = (EditText) v.findViewById(R.id.edt_pro_lastname);
        id_number = (EditText) v.findViewById(R.id.edt_pro_idNumber);

        til_name = (TextInputLayout) v.findViewById(R.id.til_pro_fname);
        til_surname = (TextInputLayout) v.findViewById(R.id.til_pro_lname);
        til_idNumber = (TextInputLayout) v.findViewById(R.id.til_pro_idNumber);

        rdg_gender = (RadioGroup) v.findViewById(R.id.rdg_gender);
        btn_create = (Button) v.findViewById(R.id.btn_create_pro);

        btn_create.setOnClickListener(this);
        rdg_gender.setOnCheckedChangeListener(this);

        mFirebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mFirebaseAuth.getCurrentUser();

        String f_name = user.getDisplayName();

        first_name.setText(f_name);*/

        return v;
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i)
    {

    }

    @Override
    public void onClick(View view)
    {

    }
}
