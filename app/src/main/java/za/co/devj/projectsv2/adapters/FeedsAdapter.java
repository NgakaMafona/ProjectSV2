package za.co.devj.projectsv2.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import za.co.devj.projectsv2.R;
import za.co.devj.projectsv2.pojo.feedClass.Feeds;

/**
 * Created by Codetribe on 3/20/2017.
 */

public class FeedsAdapter extends RecyclerView.Adapter<FeedsAdapter.MyViewHolder>
{
    ArrayList<Feeds> feed_list;

    Context c;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_cards,parent,false);
        c = v.getContext();
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
        Feeds feeds = feed_list.get(position);
        holder.tv_desc.setText(feeds.getDesc());
        holder.tv_title.setText(feeds.getTitle());
        Picasso.with(c).load(feeds.getImgUrl()).into(holder.img);
    }

    @Override
    public int getItemCount()
    {
        return feed_list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        ImageView img;
        TextView tv_title, tv_desc;

        public MyViewHolder(View itemView)
        {
            super(itemView);

            img = (ImageView) itemView.findViewById(R.id.feed_image);
            tv_title = (TextView) itemView.findViewById(R.id.tv_feed_title);
            tv_desc = (TextView) itemView.findViewById(R.id.tv_feed_desc);
        }
    }

    public FeedsAdapter(ArrayList<Feeds> feed_list)
    {
        this.feed_list = feed_list;
    }

}
