package za.co.devj.projectsv2.pojo.events;

import android.media.Image;
import android.widget.Adapter;

import java.io.Serializable;

/**
 * Created by Phil on 12/6/2016.
 */

public class Events implements Serializable
{
    private String ev_name;
    private String ev_desc;
    private String img_url;

    public Events()
    {

    }

    public String getEv_name()
    {
        return ev_name;
    }

    public void setEv_name(String ev_name)
    {
        this.ev_name = ev_name;
    }

    public String getEv_desc()
    {
        return ev_desc;
    }

    public void setEv_desc(String ev_desc)
    {
        this.ev_desc = ev_desc;
    }

    public String getImg_url()
    {
        return img_url;
    }

    public void setImg_url(String img_url)
    {
        this.img_url = img_url;
    }


    /* public HashMap<String,Object> toMap(Events e)
    {
        HashMap<String,Object> map = new HashMap<String,Object>();

        map.put("e_date_created",e.ev_date_cr);
        map.put("e_date_modified",e.ev_date_mod);
        map.put("e_name",e.ev_name);
        map.put("e_desc",e.ev_desc);
        map.put("e_type",e.ev_type);

        return map;
    }*/

   @Override
    public String toString()
    {
        return "Events{" +
                ", ev_name='" + ev_name + '\'' +
                ", ev_desc='" + ev_desc + '\'' +
                ", img_url ='" + img_url + '}';
    }
}
