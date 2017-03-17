package za.co.devj.projectsv2.pojo.userEvents;

/**
 * Created by drmaf on 2017/03/16.
 */

public class UserEvents
{
    private String event_name;
    private String event_date;
    private String services;

    public UserEvents()
    {

    }

    public UserEvents(String event_name, String event_date, String services)
    {
        this.event_name = event_name;
        this.event_date = event_date;
        this.services = services;
    }

    public String getEvent_name()
    {
        return event_name;
    }

    public void setEvent_name(String event_name)
    {
        this.event_name = event_name;
    }

    public String getEvent_date()
    {
        return event_date;
    }

    public void setEvent_date(String event_date)
    {
        this.event_date = event_date;
    }

    public String getServices()
    {
        return services;
    }

    public void setServices(String services)
    {
        this.services = services;
    }
}
