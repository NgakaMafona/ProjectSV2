package za.co.devj.projectsv2;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.DrawableRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileDescriptor;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import za.co.devj.projectsv2.pojo.user.User;
import za.co.devj.projectsv2.utils.Validations;

public class UpdateProfileActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener
{

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseDatabase db;
    private StorageReference storageRef;

    private Uri filePath;
    private Uri firebasePath;

    Bitmap bitmap = null;

    @BindView(R.id.rdg_gender)RadioGroup rdg_gender;

    EditText first_name;
    EditText last_name;

    Button btn_create;

    TextInputLayout til_name;
    TextInputLayout til_surname;
    TextInputLayout til_idNumber;

    String gender;
    String name;
    String surname;
    String dob;

    private DatePicker picker;

    private boolean edit = false;

    private int count = 0;

    private static int GAL_REQ_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        first_name = (EditText) findViewById(R.id.edt_pro_firstname);
        last_name = (EditText) findViewById(R.id.edt_pro_lastname);

        til_name = (TextInputLayout) findViewById(R.id.til_pro_fname);
        til_surname = (TextInputLayout) findViewById(R.id.til_pro_lname);
        til_idNumber = (TextInputLayout) findViewById(R.id.til_pro_idNumber);

        picker = (DatePicker) findViewById(R.id.datePicker);

        rdg_gender = (RadioGroup) findViewById(R.id.rdg_gender);
        btn_create = (Button) findViewById(R.id.btn_create_pro);

        btn_create.setOnClickListener(this);
        rdg_gender.setOnCheckedChangeListener(this);

        mFirebaseAuth = FirebaseAuth.getInstance();

        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        String user = mFirebaseUser.getDisplayName();

        String name = user.substring(0,user.indexOf(" "));
        String surname = user.substring(user.indexOf(" "));

        picker.setEnabled(false);
        rdg_gender.setEnabled(false);


        first_name.setText(name);
        last_name.setText(surname);


        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                count++;
                edit = true;

                Log.e("EDIT", "YES");

                if(edit)
                {
                    first_name.setEnabled(true);
                    last_name.setEnabled(true);
                    picker.setEnabled(true);
                    rdg_gender.setEnabled(true);
                }

                if(count == 1)
                {
                    count++;
                    fab.setImageDrawable(ContextCompat.getDrawable(getBaseContext(),R.drawable.ic_camera_24dp));
                }
                else
                {
                    View v = new View(getBaseContext());

                    loadImage(v);
                }


            }
        });

        picker.init(picker.getYear(), picker.getMonth(), picker.getDayOfMonth(), new DatePicker.OnDateChangedListener()
        {
            @Override
            public void onDateChanged(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth)
            {
                int mYear = year;
                int month = monthOfYear+1;
                int day = dayOfMonth;

                dob = ""+day+"/"+month+"/"+year;

                Log.e("Date :",""+day + " " + month + " " + mYear);
            }
        });
    }

    public void loadImage(View view)
    {
        //Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);

        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(i,"Select Imane"),GAL_REQ_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        ParcelFileDescriptor pfd = null;

        try
        {
            if(requestCode == GAL_REQ_CODE && resultCode == RESULT_OK && null != data && data.getData() != null)
            {

                filePath = data.getData();

                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);

                ImageView img_view = (ImageView) findViewById(R.id.pro_pic);

                img_view.setImageBitmap(bitmap);
                addProPic();

            }
            else
            {
                Toast.makeText(this,"You haven't selected an image",Toast.LENGTH_LONG).show();
            }
        }
        catch(Exception e)
        {
            Toast.makeText(this,"Something went wrong",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i)
    {

        RadioButton btn_selected = (RadioButton) findViewById(i);

        String txt = btn_selected.getText().toString();

        if(txt.equalsIgnoreCase("male"))
        {
            gender = "Male";
        }
        else if(txt.equalsIgnoreCase("female"))
        {
            gender = "Female";
        }
    }

    @Override
    public void onClick(View view)
    {
        Validations val = new Validations();

        name = first_name.getText().toString();
        surname = last_name.getText().toString();

        boolean valid_name = false;
        boolean valid_surname = false;



        if(name.equalsIgnoreCase(""))
        {
            til_name.setError("Enter Your Name");
        }
        else
        {
            til_name.setError("");

            valid_name = val.validString(name);

            if(!valid_name)
            {
                til_name.setError("No numbers or special characters allowed");
            }
            else
            {
                til_name.setError("");
            }
        }

        if(surname.equalsIgnoreCase(""))
        {
            til_surname.setError("Enter Your Surname");
        }
        else
        {
            til_surname.setError("");
            valid_surname = val.validString(surname);

            if(!valid_surname)
            {
                til_name.setError("No numbers or special characters allowed");
            }
            else
            {
                til_surname.setError("");
            }
        }

        if(valid_name && valid_surname)
        {
            Date date = new Date();

            String date_cr = date.toString();
            String date_mod = date.toString();



            if(bitmap == null)
            {
                User u = new User(date_cr, date_mod, name, surname, gender,dob);
                addUser(u);
            }
            else
            {
                String path = firebasePath.getPath();

                User u = new User(date_cr, date_mod, name, surname, gender,dob,path);
                addUser(u);
            }




            startActivity(new Intent(UpdateProfileActivity.this,MainActivity.class));
            finish();
        }

    }

    private void addUser(User u)
    {
        User user = new User();
        Map<String,Object> map = new HashMap<String,Object>();

        //Retrieve user details in hashmap
        map = user.toMap(u);

        db = FirebaseDatabase.getInstance();

        DatabaseReference ref = db.getReference();


        //Add user details to the Users table
        ref.child("User").child(mFirebaseAuth.getCurrentUser().getUid()).updateChildren(map);

        Toast.makeText(this,"Profile created",Toast.LENGTH_LONG).show();
    }

    public void addProPic()
    {
        storageRef = FirebaseStorage.getInstance().getReference();

        String uID = mFirebaseUser.getUid();

        StorageReference img_ref = storageRef.child("profileImage/"+uID+"/");

        img_ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
        {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                  firebasePath = taskSnapshot.getDownloadUrl();

            }
        });
    }
}
