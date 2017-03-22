package za.co.devj.projectsv2.ui;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Codetribe on 3/20/2017.
 */

public class GoHttp extends AsyncTask<String,Void,String>
{
    @Override
    protected String doInBackground(String... strings)
    {
        try
        {
            BufferedReader reader = null;

            URL url = new URL(strings[0]);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();

            int status = con.getResponseCode();

            if(status == HttpURLConnection.HTTP_OK)
            {
                InputStream is = con.getInputStream();

                StringBuffer buffer = new StringBuffer();

                if(is == null)
                {
                    return "";
                }

                reader = new BufferedReader(new InputStreamReader(is));

                String line;

                while((line = reader.readLine()) != null)
                {
                    buffer.append(line + "\n");
                }

                if(buffer.length() == 0)
                {
                    return "";
                }

                return buffer.toString();
            }

        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s)
    {
        super.onPostExecute(s);


    }
}
