package za.co.devj.projectsv2.utils;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by Codetribe on 11/16/2016.
 */

public class ProgressClass
{
    ProgressDialog pd;

    public ProgressClass()
    {

    }

    /**
     * Start progress dialog
     * @param c: Context
     * @param t: title
     * @param m: message
     */
    public void startProgressDialog(Context c,String t,String m)
    {
        pd = new ProgressDialog(c);
        pd.setTitle(t);
        pd.setMessage(m);
        pd.setIndeterminate(false);
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        pd.show();

    }

    public void stopProgressDialoge()
    {
        pd.dismiss();
    }
}
