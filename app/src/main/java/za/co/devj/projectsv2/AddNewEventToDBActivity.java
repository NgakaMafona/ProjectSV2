package za.co.devj.projectsv2;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import za.co.devj.projectsv2.pojo.events.Events;
import za.co.devj.projectsv2.pojo.services.Services;

public class AddNewEventToDBActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener
{

    Spinner spn_serv;
    ArrayAdapter<String> adapter;
    Resources res;
    String[] array_serv = null;

    EditText edt_c_name;
    EditText edt_c_desc;
    EditText edt_c_tel;
    EditText edt_c_web;
    EditText edt_c_addr;

    String selected_type;
    String name;
    String desc;
    String tel;
    String web;
    String addr;

    private FirebaseAuth mAuth;
    private FirebaseDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_event_to_db);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();


        spn_serv = (Spinner) findViewById(R.id.new_event_type);
        res = getResources();
        array_serv = res.getStringArray(R.array.services);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, array_serv);
        spn_serv.setAdapter(adapter);

        spn_serv.setOnItemSelectedListener(this);

        edt_c_name = (EditText) findViewById(R.id.edt_c_name);
        edt_c_desc = (EditText) findViewById(R.id.edt_c_desc);
        edt_c_tel = (EditText) findViewById(R.id.edt_c_tel);
        edt_c_web = (EditText) findViewById(R.id.edt_c_web);
        edt_c_addr = (EditText) findViewById(R.id.edt_c_addr);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                name = edt_c_name.getText().toString();
                desc = edt_c_desc.getText().toString();
                tel = edt_c_tel.getText().toString();
                web = edt_c_web.getText().toString();
                addr = edt_c_addr.getText().toString();

                if(selected_type.equalsIgnoreCase("Select event type"))
                {
                    Toast.makeText(getBaseContext(),"Select an event type",Toast.LENGTH_LONG).show();
                }
                else
                {
                    Services s = new Services(name,desc,addr,"12345-65432",tel,web,"image url here");

                    addServ(s);

                    Toast.makeText(getBaseContext(),"added " + name + " to " + selected_type,Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
    {
        selected_type = (String) adapterView.getItemAtPosition(i);

        //Toast.makeText(this,"selected " + selected_type,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView)
    {

    }

    public void addServ(Services s)
    {


        db = FirebaseDatabase.getInstance();

        DatabaseReference ref = db.getReference();

        //Add array_serv
        ref.child("Serv_"+selected_type+"_ads").push().setValue(s);

    }
}
