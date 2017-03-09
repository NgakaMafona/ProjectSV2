package za.co.devj.projectsv2.pojo.user;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by Philemon on 11/17/2016.
 */

public class User
{
    private String date_cr;
    private String date_mod;
    private String name;
    private String surname;
    private String gender;
    private String DOB;
    private String url;

    public User()
    {

    }

    public User(String date_cr, String date_mod, String name, String surname, String gender, String DOB)
    {
        this.date_cr = date_cr;
        this.date_mod = date_mod;
        this.name = name;
        this.surname = surname;
        this.gender = gender;
        this.DOB = DOB;
    }

    public User(String date_cr, String date_mod, String name, String surname, String gender, String DOB, String url)
    {

        this.date_cr = date_cr;
        this.date_mod = date_mod;
        this.name = name;
        this.surname = surname;
        this.gender = gender;
        this.DOB = DOB;
        this.url = url;
    }


    public String getName()
    {
        return name;
    }

    public String getSurname()
    {
        return surname;
    }

    public String getGender()
    {
        return gender;
    }

    public String getDOB()
    {
        return DOB;
    }

    public String getDate_cr()
    {
        return date_cr;
    }

    public String getDate_mod()
    {
        return date_mod;
    }

    public String getUrl()
    {
        return url;
    }

    public void setDate_cr(String date_cr)
    {
        this.date_cr = date_cr;
    }

    public void setDate_mod(String date_mod)
    {
        this.date_mod = date_mod;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setSurname(String surname)
    {
        this.surname = surname;
    }

    public void setGender(String gender)
    {
        this.gender = gender;
    }

    public void setDOB(String DOB)
    {
        this.DOB = DOB;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    /*public int calcAge()
    {
        int age = 0;

        String strID = getDOB();
        String subYear = strID.substring(0,2);

        int birth_year = Integer.parseInt(subYear);

        String y = "";

        if(birth_year > 16 && birth_year <=99)
        {
            y = "19"+birth_year;
        }
        else
        {
            y = "20"+birth_year;
        }

        int bYear= Integer.parseInt(y);

        //get current year
        Date date = new Date();

        String str_year = date.toString();

        String a = str_year.substring(30);

        int current_year = Integer.parseInt(a);

        age = current_year - bYear;

        return age;
    }*/

    public HashMap<String,Object> toMap(User u)
    {
        HashMap<String,Object> map = new HashMap<String,Object>();

        map.put("date_created",u.getDate_cr());
        map.put("date_modified",u.getDate_mod());
        map.put("u_name",u.getName());
        map.put("u_surname",u.getSurname());
        map.put("u_gender",u.getGender());
        map.put("u_DOB",u.getDOB());
        map.put("u_pro_pic_loc",""+u.getUrl());

        return map;
    }
}
