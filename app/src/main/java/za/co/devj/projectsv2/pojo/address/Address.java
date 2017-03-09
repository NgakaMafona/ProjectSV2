package za.co.devj.projectsv2.pojo.address;

/**
 * Created by Philemon on 11/17/2016.
 */

public class Address
{
    private String number;
    private String street;
    private String suburb;
    private String city;
    private String province;
    private int code;

    public Address()
    {

    }

    public Address(String number, String street, String suburb, String city, String province, int code)
    {
        this.number = number;
        this.street = street;
        this.suburb = suburb;
        this.city = city;
        this.province = province;
        this.code = code;
    }

    public String getNumber()
    {
        return number;
    }

    public String getStreet()
    {
        return street;
    }

    public String getSuburb()
    {
        return suburb;
    }

    public String getCity()
    {
        return city;
    }

    public String getProvince()
    {
        return province;
    }

    public int getCode()
    {
        return code;
    }
}
