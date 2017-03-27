package za.co.devj.projectsv2;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import za.co.devj.projectsv2.ui.GenServiceDialog;

public class EditEventActivity extends AppCompatActivity implements View.OnClickListener
{
    private SharedPreferences sp;

    TextView tv_type;

    Button btn_edit;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        tv_type = (TextView) findViewById(R.id.type);

        sp = getSharedPreferences("myEvent",MODE_APPEND);
        String n = sp.getString("EventType",null);

        tv_type.setText(n);

        btn_edit = (Button) findViewById(R.id.btn_edtServices);
        btn_edit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        int id = v.getId();

        if(id == R.id.btn_edtServices)
        {
            GenServiceDialog dialog = new GenServiceDialog(this);

            dialog.getServices();
        }
    }



}
