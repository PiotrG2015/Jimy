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
import android.widget.ProgressBar;

import com.example.firsttry.android.eventapp.EventPage;
import com.example.firsttry.android.eventapp.R;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import com.androidquery.AQuery;

/**
 * Created by Piotr Gorczyca on 2015-07-19. Enjoy!
 */

public class EventsDetailsActivity extends Activity {

    private String mObjectId;
    private EventPage mTemp;
    private Bundle mBundle;
    private ProgressBar mBar;
    private AQuery aq;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * setting the ActionBar transparent
         */
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#33ededed")));
            actionBar.setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#55ededed")));
        }
        setContentView(R.layout.singleitemview);

        mTemp = new EventPage();
        mBundle = getIntent().getExtras();
        mBar = (ProgressBar) findViewById(R.id.event_page_progressBar);
        aq = new AQuery(this);

        // getting data passed from the previous activity
        mTemp.setName(mBundle.getString("name"));
        mTemp.setDiff(mBundle.getLong("timeLeft"));
        mObjectId = mBundle.getString("objectId");
        new DownloadDataTask().execute();

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


        } else if(id == android.R.id.home){
            finish();
            return true;
        }

        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private class DownloadDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Event");
                query.whereEqualTo("objectId", mObjectId);
                List<ParseObject> iterator;
                iterator = query.find();
                for (ParseObject event : iterator) {
                    mTemp.setLongDescription((String) event.get("longDescription"));
                    mTemp.setPlace((String) event.get("place"));
                    mTemp.setImage(((ParseFile) event.get("image")).getUrl());

                    /**
                     * Managing the date, time
                     */
                    mTemp.setCal(DateToCalendar((Date) event.get("date_of_event")));
                    mTemp.setDayOfWeek(mTemp.getCal().get(Calendar.DAY_OF_WEEK));
                    mTemp.setMonth(mTemp.getCal().get(Calendar.MONTH));
                    mTemp.setDayInMonth(mTemp.getCal().get(Calendar.DATE));
                    mTemp.setStartHour(mTemp.getCal().get(Calendar.HOUR_OF_DAY));
                    mTemp.setStartMinute(mTemp.getCal().get(Calendar.MINUTE));

                }

            } catch (ParseException e) {
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
            aq.id(R.id.siv_name_tv).text(mTemp.getName());
            aq.id(R.id.siv_weekday_tv).text(mTemp.getDayOfWeek());
            aq.id(R.id.siv_month_tv).text(mTemp.getMonth());
            aq.id(R.id.siv_day_tv).text(mTemp.getDayInMonth());
            aq.id(R.id.siv_long_description_tv).text(mTemp.getLongDescription());
            aq.id(R.id.siv_place_tv).text(mTemp.getPlace());
            aq.id(R.id.siv_start_time_tv).text(mTemp.getStartTime());
            aq.id(R.id.siv_remaining_time_tv).text(timeConverter(mTemp.getDiff()));
            aq.id(R.id.siv_image_iv).image(mTemp.getImage(), true, true, 200, 0);
            // loading finished - run a splash screen
            aq.id(R.id.siv_splash_screen).gone();
            mBar.setVisibility(View.GONE);

        }
    }


    private static Calendar DateToCalendar(Date date){
        Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("UTC+2:00"));
        cal.setTime(date);
        return cal;
    }

    /**
     * method which converts time difference given milliseconds to a String message
     * @param diff time difference in milliseconds
     * @return string with information on how many min/hours/days are left
     */
    private static String timeConverter(long diff) {
        if(diff <= 0){
            return "";
        } else if(TimeUnit.MILLISECONDS.toMinutes(diff) < 60){
            return "(" + TimeUnit.MILLISECONDS.toMinutes(diff) + " min. left)";
        } else if(TimeUnit.MILLISECONDS.toHours(diff) < 24){
            return "(" + TimeUnit.MILLISECONDS.toHours(diff) + " hours left)";
        } else if(TimeUnit.MILLISECONDS.toDays(diff) < 2){
            return "(" + TimeUnit.MILLISECONDS.toDays(diff) + " day left)";
        } else return "(" + TimeUnit.MILLISECONDS.toDays(diff) + " days left)";
    }
}
