package com.example.firsttry.android.eventapp.Activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.firsttry.android.eventapp.EventsList;
import com.example.firsttry.android.eventapp.ListViewAdapter;
import com.example.firsttry.android.eventapp.R;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by Piotr on 2015-07-19.
 */

public class EventListActivity extends Activity {

    private Bundle mBundle;
    private String mCategory;
    private ProgressBar mBar;
    private ActionBar mActionBar;
    private List<ParseObject> iterator;
    private ListViewAdapter mAdapter;
    private List<EventsList> mEventsList = null;
    private ListView mList;
    private Calendar currentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * setting the transparent ActionBar
         */

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        mActionBar = getActionBar();
        mActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#33ededed")));
        mActionBar.setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#55ededed")));

        setContentView(R.layout.activity_event_list);

        //getting the category name which user choosed in the main acitivity
        mBundle = getIntent().getExtras();
        mCategory = mBundle.getString("choosedCategory");
        mBar = (ProgressBar) findViewById(R.id.events_list_progressBar);
        mList = (ListView) findViewById(R.id.list);

        currentDate = new GregorianCalendar(TimeZone.getTimeZone("UTC+02:00"));

        new RemoteDataTask().execute();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {

            ParseUser.logOutInBackground();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            mEventsList = new ArrayList<EventsList>();

            try {

                //asking for query / table Event
                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Event");
                //selection according to category and time
                query.whereEqualTo("category", mCategory);
                //order based on event's date
                query.orderByAscending("date_of_event");
                //creating EventsList objects with downloaded data and putting them to the container
                iterator = query.find();
                for (ParseObject event : iterator) {
                    ParseFile image = (ParseFile) event.get("thumbnail");
                    EventsList temp = new EventsList();
                    temp.setCal(DateToCalendar((Date) event.get("date_of_event")));
                    temp.setName((String) event.get("name"));
                    temp.setObjectId(event.getObjectId());
                    temp.setShortDescription((String) event.get("shortDescription"));
                    temp.setThumbnail(image.getUrl());
                    /**
                     * counting remaining time
                     */
                    temp.setDiff(temp.getCal().getTimeInMillis() - currentDate.getTimeInMillis() - 3600000);
                    mEventsList.add(temp);
                }
            }catch (ParseException e){
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void result) {
            mAdapter = new ListViewAdapter(getApplicationContext(), mEventsList);
            mList.setAdapter(mAdapter);
            mBar.setVisibility(View.GONE);
        }

    }

    private static Calendar DateToCalendar(Date date){
        Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("UTC+2:00"));
        cal.setTime(date);
        return cal;
    }
}
