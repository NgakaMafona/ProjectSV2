package za.co.devj.projectsv2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;

import za.co.devj.projectsv2.pojo.events.Events;
import za.co.devj.projectsv2.pojo.userEvents.UserEvents;
import za.co.devj.projectsv2.utils.Constants;

public class EventListActivity extends AppCompatActivity
{

    private DatabaseReference db_ref;
    private StorageReference st_ref;

    private FirebaseAuth mAuth;
    private FirebaseDatabase fdb;

    private static TextView tv_title;
    private static TextView tv_evDesc;

    private RecyclerView rv;

    private ArrayList<String> events = new ArrayList<>();
    private ArrayList<String> service_list = new ArrayList<>();
    private ArrayList<Boolean> service_checked = new ArrayList<>();

    private static ArrayList<Integer> user_selection = new ArrayList<>();

    private String[] list;
    private boolean[] checked;

    String item = "";

    private static TextView title;
    private static TextView desc;

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        Intent i = getIntent();
        String category = i.getStringExtra(NewEventActivity.TAG_CAT);

        rv = (RecyclerView) findViewById(R.id.rec_view);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));

        mAuth = FirebaseAuth.getInstance();
        fdb = FirebaseDatabase.getInstance();


        if(category.equalsIgnoreCase("Social"))
        {


            Log.e("OK: ", "get db ref");
            db_ref = FirebaseDatabase.getInstance().getReference().child(Constants.TAG_SOC);

            onStart();

        }
        else
        {
            Log.e("OK: ", "get db ref");
            db_ref = FirebaseDatabase.getInstance().getReferenceFromUrl(Constants.DB_URL).child(Constants.TAG_CORP);

            onStart();
        }

    }

    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseRecyclerAdapter<Events,EventViewHolder> adapter = new FirebaseRecyclerAdapter<Events, EventViewHolder>
                (Events.class,R.layout.event_row,EventViewHolder.class,db_ref)
        {
            @Override
            protected void populateViewHolder(final EventViewHolder viewHolder, Events model, final int position)
            {
                viewHolder.setTitle(model.getEv_name());
                viewHolder.setDesc(model.getEv_desc());
                viewHolder.setImage(getApplicationContext(),model.getImg_url());

                events.add(model.getEv_name());

                viewHolder.btn_options.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        getServices();
                    }
                });

                View v = viewHolder.getView();

                v.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        String uid = user.getUid();

                        if(user_selection.isEmpty())
                        {
                            Toast.makeText(getApplicationContext(),"Please select services to proceed",Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            String t = events.get(position);

                            UserEvents ue = new UserEvents(t,"null",item);

                            editor = sp.edit();
                            editor.putString("EventType",ue.getEvent_name());
                            editor.commit();

                            DatabaseReference db_ref = FirebaseDatabase.getInstance().getReference();

                            db_ref.child("UserEvents").child(uid).setValue(ue);

                            Toast.makeText(getApplicationContext(),"Event Created " + t,Toast.LENGTH_LONG).show();

                            startActivity(new Intent(EventListActivity.this,MainActivity.class));
                            finish();
                        }
                    }
                });
            }
        };
        adapter.notifyDataSetChanged();
        rv.setAdapter(adapter);

    }

    //[Start of getting event list]
    public static class EventViewHolder extends RecyclerView.ViewHolder
    {
        View myView;
        Context context;

        ImageButton btn_options;

        public EventViewHolder(View itemView)
        {
            super(itemView);

            myView = itemView;

            context = itemView.getContext();

            btn_options = (ImageButton) myView.findViewById(R.id.btn_options);

        }
        public void setTitle(String t)
        {
            title = (TextView) myView.findViewById(R.id.tv_title);
            title.setText(t);
        }

        public void setDesc(String d)
        {
            desc = (TextView) myView.findViewById(R.id.tv_desc);
            desc.setText(d);
        }

        public void setImage(Context c,String img_url)
        {
            ImageView img_view = (ImageView) myView.findViewById(R.id.image_content);
            Picasso.with(c).load(img_url).into(img_view);
        }

        public View getView()
        {
            return myView;
        }

    }
    //[End of getting event list]

    //[get Service list from database and add it to a multi-choice dialog]
    public void getServices()
    {
        db_ref = FirebaseDatabase.getInstance().getReference().child("Services");

        service_list = new ArrayList<>();

        db_ref.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                String serv = "";
                boolean ch = false;

                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    serv = String.valueOf(ds.getKey());
                    ch = (Boolean) ds.getValue();

                    service_list.add(serv);
                    service_checked.add(ch);
                }

                //convert from arraylist to array
                list = service_list.toArray(new String[service_list.size()]);
                checked = new boolean[list.length];

                for(int x = 0; x < checked.length;x++)
                {
                    checked[x] = false;

                    Log.e("Here ", ""+service_list.get(x));
                }

                //create alert dialog with service list
                if(checked.length == service_list.size())
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(EventListActivity.this);
                    builder.setTitle("Available Services");
                    builder.setCancelable(false);

                    builder.setMultiChoiceItems(list, checked, new DialogInterface.OnMultiChoiceClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int position, boolean isChecked)
                        {
                            checked[position] = isChecked;

                            if(isChecked)
                            {
                                if(!user_selection.contains(position))
                                {
                                    user_selection.add(position);
                                }
                                else
                                {
                                    user_selection.remove(position);
                                }
                            }
                        }
                    });

                    //OK button
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int position)
                        {

                            sp = getSharedPreferences("myEvent",MODE_APPEND);
                            editor = sp.edit();

                            for(int i = 0; i < user_selection.size(); i++)
                            {
                                item = item+list[user_selection.get(i)];

                                if(i != user_selection.size() -1)
                                {
                                    item = item + ", ";

                                    editor.putString("Item " + i,item);
                                    editor.commit();
                                }
                            }
                        }
                    });

                    //Cancel button
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();
                        }
                    });

                    //clear all button
                    builder.setNeutralButton("Clear all", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            for(int x = 0; x < checked.length;x++)
                            {
                                checked[x] = false;
                            }
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
    }
    //[end of getting Service list from database and add it to a multi-choice dialog]

    //[Start of adding user event to firebase]
    public void addUserEvent()
    {

    }

    @Override
    protected void onPostResume()
    {
        super.onPostResume();
    }
}
