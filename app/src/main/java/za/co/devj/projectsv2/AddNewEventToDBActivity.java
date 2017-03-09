package za.co.devj.projectsv2;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import za.co.devj.projectsv2.pojo.events.Events;
import za.co.devj.projectsv2.utils.Constants;

public class AddNewEventToDBActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener
{

    Spinner spn_ev;
    ArrayAdapter<String> adapter;
    Resources res;
    String[] events = null;

    EditText edt_ev_name;
    EditText edt_ev_desc;

    String selected_type;
    String name;
    String desc;

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


        spn_ev = (Spinner) findViewById(R.id.new_event_type);
        res = getResources();
        events = res.getStringArray(R.array.event_types);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,events);
        spn_ev.setAdapter(adapter);

        spn_ev.setOnItemSelectedListener(this);

        edt_ev_name = (EditText) findViewById(R.id.edt_ev_name);
        edt_ev_desc = (EditText) findViewById(R.id.edt_ev_desc);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                name = edt_ev_name.getText().toString();
                desc = edt_ev_desc.getText().toString();

                if(selected_type.equalsIgnoreCase("Select event type"))
                {
                    Toast.makeText(getBaseContext(),"Select an event type",Toast.LENGTH_LONG).show();
                }
                else
                {
                    Events e = new Events();

                    e.setEv_name(name);
                    e.setEv_desc(desc);

                    addEvent(e);

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

    public void addEvent(Events ev)
    {


        db = FirebaseDatabase.getInstance();

        DatabaseReference ref = db.getReference();

        //Add events
        ref.child(selected_type).push().setValue(ev);

    }
}
