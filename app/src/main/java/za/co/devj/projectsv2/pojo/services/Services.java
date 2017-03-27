package za.co.devj.projectsv2.pojo.services;

/**
 * Created by Codetribe on 3/10/2017.
 */

public class Services
{
    public String serve_name;
    public String serve_desc;
    public String serve_address;
    public String serve_coordinates;
    public String serve_tel;
    public String serve_site;
    public String serve_img;
    public String serve_rating;

    public Services()
    {

    }

    public Services(String serve_name, String serve_desc, String serve_address, String serve_coordinates, String serve_tel, String serve_site, String serve_img,String serve_rating)
    {
        this.serve_name = serve_name;
        this.serve_desc = serve_desc;
        this.serve_address = serve_address;
        this.serve_coordinates = serve_coordinates;
        this.serve_tel = serve_tel;
        this.serve_site = serve_site;
        this.serve_img = serve_img;
        this.serve_rating = serve_rating;
    }

    public String getServe_name()
    {
        return serve_name;
    }

    public void setServe_name(String serve_name)
    {
        this.serve_name = serve_name;
    }

    public String getServe_desc()
    {
        return serve_desc;
    }

    public void setServe_desc(String serve_desc)
    {
        this.serve_desc = serve_desc;
    }

    public String getServe_address()
    {
        return serve_address;
    }

    public void setServe_address(String serve_address)
    {
        this.serve_address = serve_address;
    }

    public String getServe_coordinates()
    {
        return serve_coordinates;
    }

    public void setServe_coordinates(String serve_coordinates)
    {
        this.serve_coordinates = serve_coordinates;
    }

    public String getServe_tel()
    {
        return serve_tel;
    }

    public void setServe_tel(String serve_tel)
    {
        this.serve_tel = serve_tel;
    }

    public String getServe_site()
    {
        return serve_site;
    }

    public void setServe_site(String serve_site)
    {
        this.serve_site = serve_site;
    }

    public String getServe_img()
    {
        return serve_img;
    }

    public void setServe_img(String serve_img)
    {
        this.serve_img = serve_img;
    }

    public String getServe_rating()
    {
        return serve_rating;
    }

    public void setServe_rating(String serve_rating)
    {
        this.serve_rating = serve_rating;
    }
}
