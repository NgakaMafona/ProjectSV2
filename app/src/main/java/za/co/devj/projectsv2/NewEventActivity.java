package za.co.devj.projectsv2;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import za.co.devj.projectsv2.utils.Constants;

public class NewEventActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener,View.OnClickListener
{

    private RadioGroup rdg_type;
    private RadioButton selected;
    private Button btn_next;

    private String category;

    public static final String TAG_CAT = "category";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        rdg_type = (RadioGroup) findViewById(R.id.rdg_category);
        btn_next = (Button) findViewById(R.id.btn_next);

        rdg_type.setOnCheckedChangeListener(this);
        btn_next.setOnClickListener(this);

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId)
    {
        selected = (RadioButton) group.findViewById(checkedId);

        int id = selected.getId();

        if(id == R.id.rdb_soc)
        {
            category = Constants.TAG_SOC;
        }
        else
        {
            category = Constants.TAG_CORP;
        }
    }

    @Override
    public void onClick(View v)
    {
        int id = v.getId();

        if(id == R.id.btn_next)
        {
            Intent i = new Intent(this,EventListActivity.class);
            i.putExtra(TAG_CAT,category);
            startActivity(i);
        }

    }
}
