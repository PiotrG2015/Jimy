package com.example.firsttry.android.eventapp;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.example.firsttry.android.eventapp.Activities.EventsDetailsActivity;


/**
 * Created by Piotr on 2015-07-19.
 */

public class ListViewAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<EventsList> mEventsList = null;
    private ArrayList<EventsList> mArraylist;
    private AQuery aq;

    public ListViewAdapter(Context context, List<EventsList> eventsList) {
        this.mContext = context;
        this.mEventsList = eventsList;
        mInflater = LayoutInflater.from(context);
        this.mArraylist = new ArrayList<EventsList>();
        this.mArraylist.addAll(eventsList);
        aq = new AQuery(context);
    }

    public class ViewHolder {
        TextView name;
        TextView description;
        ImageView thumbnail;
        TextView timeLeft;
    }

    @Override
    public int getCount() {
        return mEventsList.size();
    }

    @Override
    public Object getItem(int position) {
        return mEventsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;

        if(view == null) {
            holder = new ViewHolder();
            view = mInflater.inflate(R.layout.row_item, null);
            holder.name = (TextView) view.findViewById(R.id.name_tv);
            holder.description = (TextView) view.findViewById(R.id.short_description_tv);
            holder.thumbnail = (ImageView) view.findViewById(R.id.thumbnail_iv);
            holder.timeLeft = (TextView) view.findViewById(R.id.time_left_tv);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        // setting name, descriprion and thumbnail
        holder.name.setText(mEventsList.get(position).getName());
        holder.description.setText(mEventsList.get(position).getShortDescription());
        aq.id(holder.thumbnail).image(mEventsList.get(position).getThumbnail(), true, true, 100, 0);
        holder.timeLeft.setText(timeConverter(mEventsList.get(position).getDiff()));
        if(mEventsList.get(position).getDiff() < 86400000) {
            holder.timeLeft.setTextColor(mContext.getResources().getColor(R.color.orange));
        }

        /**
         * when clicked starts an activity with choosed event details
         */
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(mContext, EventsDetailsActivity.class);
                // passing info from events list which can be used in next activity
                intent.putExtra("name", (mEventsList.get(position).getName()));
                intent.putExtra("objectId", (mEventsList.get(position).getObjectId()));
                intent.putExtra("timeLeft", mEventsList.get(position).getDiff());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
        return view;
    }

    private static String timeConverter(long diff) {
        if(diff <= 0){
            return "";
        } else if(TimeUnit.MILLISECONDS.toMinutes(diff) < 60){
            return TimeUnit.MILLISECONDS.toMinutes(diff) + " min. left";
        } else if(TimeUnit.MILLISECONDS.toHours(diff) < 24){
            return TimeUnit.MILLISECONDS.toHours(diff) + " hours left";
        } else if(TimeUnit.MILLISECONDS.toDays(diff) < 2){
            return TimeUnit.MILLISECONDS.toDays(diff) + " day left";
        } else return TimeUnit.MILLISECONDS.toDays(diff) + " days left";
    }
}
