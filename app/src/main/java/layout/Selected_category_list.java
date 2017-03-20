package layout;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import za.co.devj.projectsv2.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Selected_category_list extends Fragment
{


    public Selected_category_list()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_selected_category_list, container, false);



        return view;
    }

}
