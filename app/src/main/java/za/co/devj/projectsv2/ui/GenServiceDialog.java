package za.co.devj.projectsv2.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import za.co.devj.projectsv2.EditEventActivity;

/**
 * Created by drmaf on 2017/03/26.
 */

public class GenServiceDialog
{
    private DatabaseReference db_ref;
    private ArrayList<String> events = new ArrayList<>();
    private ArrayList<String> service_list = new ArrayList<>();
    private ArrayList<Boolean> service_checked = new ArrayList<>();

    private static ArrayList<Integer> user_selection = new ArrayList<>();

    private String[] list;
    private boolean[] checked;

    Context context;

    public GenServiceDialog(Context c)
    {
        context = c;
    }

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
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
                            String item = "";
                            for(int i = 0; i < user_selection.size(); i++)
                            {
                                item = item+list[user_selection.get(i)];

                                if(i != user_selection.size() -1)
                                {
                                    item = item + ", ";

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
}
