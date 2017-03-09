package za.co.devj.projectsv2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.StackView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Stack;

import za.co.devj.projectsv2.pojo.events.Events;
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
            protected void populateViewHolder(final EventViewHolder viewHolder, Events model, int position)
            {
                viewHolder.setTitle(model.getEv_name());
                viewHolder.setDesc(model.getEv_desc());
                viewHolder.setImage(getApplicationContext(),model.getImg_url());


                viewHolder.options.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        PopupMenu pop = new PopupMenu(getApplicationContext(),viewHolder.options);

                        pop.inflate(R.menu.card_menu);

                        pop.show();
                    }
                });
            }
        };

        rv.setAdapter(adapter);
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder
    {
        View myView;

        Context context;

        TextView options;

        public EventViewHolder(View itemView)
        {
            super(itemView);

            myView = itemView;

            options =(TextView) myView.findViewById(R.id.tv_options);

        }
        public void setTitle(String t)
        {
            TextView title = (TextView) myView.findViewById(R.id.tv_title);
            title.setText(t);
        }

        public void setDesc(String d)
        {
            TextView desc = (TextView) myView.findViewById(R.id.tv_desc);
            desc.setText(d);
        }

        public void setImage(Context c,String img_url)
        {
            ImageView img_view = (ImageView) myView.findViewById(R.id.image_content);
            Picasso.with(c).load(img_url).into(img_view);
        }

        public void createPopUp(Context c)
        {
            PopupMenu pop = new PopupMenu(c,null);

            pop.inflate(R.menu.card_menu);

            pop.show();
        }
    }
}
