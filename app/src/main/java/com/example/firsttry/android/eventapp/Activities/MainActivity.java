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
import android.widget.ImageView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.example.firsttry.android.eventapp.EventsList;
import com.example.firsttry.android.eventapp.R;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;


public class MainActivity extends Activity implements View.OnClickListener {


    private ImageView mCategoryConcertsImageButton;
    private ImageView mCategoryNightLiveImageButton;
    private ImageView mCategoryMeetUpsImageButton;
    private ImageView mCategoryOthersImageButton;
    private View mFeaturedEventView;

    private EventsList mFeaturedEvent = null;
    private List<ParseObject> iterator;
    private AQuery aq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * Setting up a transparent ActionBar
         */
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#33ededed")));
            actionBar.setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#55ededed")));
        }
        aq = new AQuery(this);

        /**
         * If getCurrentUser()== null it means user is logged out -> redirect to a login screen
         */
        if(ParseUser.getCurrentUser() == null){
            goToLogin();
            return;
        }
        setContentView(R.layout.activity_main);

        mCategoryConcertsImageButton = (ImageView) findViewById(R.id.concerts_ibtn);
        mCategoryNightLiveImageButton = (ImageView) findViewById(R.id.night_live_ibtn);
        mCategoryMeetUpsImageButton = (ImageView) findViewById(R.id.meet_ups_ibtn);
        mCategoryOthersImageButton = (ImageView) findViewById(R.id.others_ibtn);
        mFeaturedEventView = findViewById(R.id.ma_featured_event_ll);

        /**
         * Setting up OnClickListeners
         */
        mCategoryConcertsImageButton.setOnClickListener(this);
        mCategoryNightLiveImageButton.setOnClickListener(this);
        mCategoryMeetUpsImageButton.setOnClickListener(this);
        mCategoryOthersImageButton.setOnClickListener(this);
        mFeaturedEventView.setOnClickListener(this);

        /**
         * Downloading "featured event" data
         */
        new RemoteDataTask().execute();


    }

    /**
     * Method used to redirect a user to a login screen
     * Using finish() method in order to close MainActivity since the user who is not logged in shouldn't have an access to that Activity
     */
    private void goToLogin() {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
        finish();
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
            goToLogin();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){

            case R.id.concerts_ibtn:
                goToEventsList("concert");
                break;

            case R.id.night_live_ibtn:
                goToEventsList("night_live");
                break;

            case R.id.meet_ups_ibtn:
                goToEventsList("meet_up");
                break;

            case R.id.others_ibtn:
                goToEventsList("other");
                break;

            case R.id.ma_featured_event_ll:
                goToEvent();
                break;


        }
    }

    private void goToEvent() {
        if(mFeaturedEvent.getObjectId()!=null){
            Intent intent = new Intent(getApplicationContext(), EventsDetailsActivity.class);
            // Giving an extra data to another activity in which featured event details will be shown.
            intent.putExtra("name", (mFeaturedEvent.getName()));
            intent.putExtra("objectId", (mFeaturedEvent.getObjectId()));
            startActivity(intent);
        } else
            Toast.makeText(getApplicationContext(), "Sorry, seems like there is a problem with your internet connection :(", Toast.LENGTH_SHORT).show();
    }

    private void goToEventsList(String category) {
        Intent intent = new Intent(getApplicationContext(), EventListActivity.class);
        intent.putExtra("choosedCategory", category);
        startActivity(intent);
    }

    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            mFeaturedEvent = new EventsList();
            try {
                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Event");
                query.whereEqualTo("featured", true);
                iterator = query.find();
                for (ParseObject event : iterator) {
                    ParseFile image = (ParseFile) event.get("thumbnail");
                    mFeaturedEvent.setName((String) event.get("name"));
                    mFeaturedEvent.setObjectId(event.getObjectId());
                    mFeaturedEvent.setShortDescription((String) event.get("shortDescription"));
                    mFeaturedEvent.setThumbnail(image.getUrl());
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
        }

        @Override
        protected void onPostExecute(Void result) {
            aq.id(R.id.ma_thumbnail_iv).image(mFeaturedEvent.getThumbnail());
            aq.id(R.id.ma_featured_name).text(mFeaturedEvent.getName());
            aq.id(R.id.ma_featured_description).text(mFeaturedEvent.getShortDescription());

        }
    }
}
