package za.co.devj.projectsv2.pojo.feedClass;

/**
 * Created by Codetribe on 3/20/2017.
 */

public class Feeds
{
    private String author;
    private String title;
    private String desc;
    private String url;
    private String imgUrl;
    private String publishDate;

    public Feeds()
    {

    }

    public Feeds(String author, String title, String desc, String url, String imgUrl, String publishDate)
    {
        this.author = author;
        this.title = title;
        this.desc = desc;
        this.url = url;
        this.imgUrl = imgUrl;
        this.publishDate = publishDate;
    }

    public String getAuthor()
    {
        return author;
    }

    public void setAuthor(String author)
    {
        this.author = author;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getDesc()
    {
        return desc;
    }

    public void setDesc(String desc)
    {
        this.desc = desc;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getImgUrl()
    {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl)
    {
        this.imgUrl = imgUrl;
    }

    public String getPublishDate()
    {
        return publishDate;
    }

    public void setPublishDate(String publishDate)
    {
        this.publishDate = publishDate;
    }
}
