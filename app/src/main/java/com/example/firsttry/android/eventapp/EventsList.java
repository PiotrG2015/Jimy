package com.example.firsttry.android.eventapp;

import java.util.Calendar;

/**
 * Created by Piotr on 2015-07-19.
 * Class which represents a single object with data to be displayed in a single row in the list
 */

public class EventsList {
    private String name;
    private String shortDescription;
    private String thumbnail;
    private String objectId;
    private Calendar cal;
    private long diff;


    public EventsList(){
        setName("");
        setShortDescription("");
        setThumbnail("");
        setObjectId(null);
        setCal(null);
        setDiff(0);
    }
    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }



    public String getShortDescription() {

        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }



    public String getThumbnail() {

        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {

        this.thumbnail = thumbnail;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {

        this.objectId = objectId;
    }


    public Calendar getCal() {
        return cal;
    }

    public void setCal(Calendar cal) {
        this.cal = cal;
    }


    public long getDiff() {
        return diff;
    }

    public void setDiff(long diff) {
        this.diff = diff;
    }

}
