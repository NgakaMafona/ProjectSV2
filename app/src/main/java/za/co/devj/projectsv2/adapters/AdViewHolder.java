package za.co.devj.projectsv2.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import za.co.devj.projectsv2.R;

/**
 * Created by drmaf on 2017/03/26.
 */

public class AdViewHolder extends ViewHolder
{
    private ImageView ad_image;
    private TextView ad_name;
    private TextView ad_desc;
    private RatingBar ad_rating;

    Context context;
    View v;

    public AdViewHolder(View itemView)
    {
        super(itemView);

        v = itemView;
        context = itemView.getContext();
    }

    public void setAd_image(Context c,String url)
    {
        ad_image = (ImageView) v.findViewById(R.id.ad_image);
        Picasso.with(context).load(url).into(ad_image);
    }

    public void setAd_name(String t)
    {
        ad_name = (TextView) v.findViewById(R.id.ad_name);
        ad_name.setText(t);
    }
    public void setAd_desc(String d)
    {
        ad_desc = (TextView) v.findViewById(R.id.ad_desc);
        ad_desc.setText(d);
    }
    public void setAd_rating(String r)
    {
        ad_rating = (RatingBar) v.findViewById(R.id.ad_rating);

        int numStars = Integer.parseInt(r);

        ad_rating.setNumStars(numStars);
    }

    public View getView()
    {
        return v;
    }
}
