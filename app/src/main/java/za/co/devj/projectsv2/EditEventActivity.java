package za.co.devj.projectsv2;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class EditEventActivity extends AppCompatActivity
{
    private SharedPreferences sp;

    TextView tv_type;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        tv_type = (TextView) findViewById(R.id.type);

        sp = getSharedPreferences("myEvent",MODE_APPEND);
        String n = sp.getString("EventType",null);

        tv_type.setText(n);
    }
}
