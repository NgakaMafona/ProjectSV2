package za.co.devj.projectsv2.pojo.user;

import java.util.HashMap;

/**
 * Created by Philemon on 11/17/2016.
 */

public class User
{
    private String date_cr;
    private String date_mod;
    private String u_name;
    private String u_surname;
    private String u_gender;
    private String u_DOB;
    private String u_pro_pic_loc;

    public User()
    {

    }

    public User(String date_cr, String date_mod, String u_name, String u_surname, String u_gender, String u_DOB)
    {
        this.date_cr = date_cr;
        this.date_mod = date_mod;
        this.u_name = u_name;
        this.u_surname = u_surname;
        this.u_gender = u_gender;
        this.u_DOB = u_DOB;
    }

    public User(String date_cr, String date_mod, String u_name, String u_surname, String u_gender, String u_DOB, String u_pro_pic_loc)
    {

        this.date_cr = date_cr;
        this.date_mod = date_mod;
        this.u_name = u_name;
        this.u_surname = u_surname;
        this.u_gender = u_gender;
        this.u_DOB = u_DOB;
        this.u_pro_pic_loc = u_pro_pic_loc;
    }


    public String getU_Name()
    {
        return u_name;
    }

    public String getU_surname()
    {
        return u_surname;
    }

    public String getU_gender()
    {
        return u_gender;
    }

    public String getU_DOB()
    {
        return u_DOB;
    }

    public String getDate_cr()
    {
        return date_cr;
    }

    public String getDate_mod()
    {
        return date_mod;
    }

    public String getU_pro_pic_loc()
    {
        return u_pro_pic_loc;
    }

    public void setDate_cr(String date_cr)
    {
        this.date_cr = date_cr;
    }

    public void setDate_mod(String date_mod)
    {
        this.date_mod = date_mod;
    }

    public void setU_Name(String u_name)
    {
        this.u_name = u_name;
    }

    public void setU_surname(String u_surname)
    {
        this.u_surname = u_surname;
    }

    public void setU_gender(String u_gender)
    {
        this.u_gender = u_gender;
    }

    public void setU_DOB(String u_DOB)
    {
        this.u_DOB = u_DOB;
    }

    public void setU_pro_pic_loc(String u_pro_pic_loc)
    {
        this.u_pro_pic_loc = u_pro_pic_loc;
    }

    /*public int calcAge()
    {
        int age = 0;

        String strID = getU_DOB();
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
        map.put("u_name",u.getU_Name());
        map.put("u_surname",u.getU_surname());
        map.put("u_gender",u.getU_gender());
        map.put("u_DOB",u.getU_DOB());
        map.put("u_pro_pic_loc",""+u.getU_pro_pic_loc());

        return map;
    }
}
