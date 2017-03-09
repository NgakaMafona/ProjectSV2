package za.co.devj.projectsv2.utils;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Home on 2016/06/09.
 */
public class Validations
{
    public Validations()
    {

    }

    public boolean isValidEmail(String email)
    {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

    public boolean validateTel(String tel)
    {
        boolean valid = false;

        int count = 0;

        for(int i = 0; i < tel.length(); i++)
        {
            if(Character.isDigit(tel.charAt(i)))
            {
                count++;
                valid = true;
            }
            else
            {
                valid = false;
                break;
            }
        }

        return valid;
    }

    public boolean validPassword(String password)
    {
        boolean valid = false;

        if(password.length() >= 8)
        {
            valid = true;
        }
        else
        {
            valid = false;
        }

        return valid;
    }

    public boolean validString(String s)
    {
        boolean valid = false;

        for(int x = 0; x <= s.length() -1; x++)
        {
            if(!Character.isDigit( s.charAt( x ) ))
            {
                valid = true;
                x++;
            }
            else
            {
                valid = false;
                break;
            }
        }

        return valid;
    }

    public boolean isValidID(String id)
    {
        boolean valid = false;

        String subYear = id.substring(0,2);

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


        if(id.length() != 13)
        {
            valid = false;
        }
        else
        {
            if(bYear >= current_year)
            {
                valid = false;
            }
            else
            {
                valid = true;
            }
        }

        return valid;
    }

}
