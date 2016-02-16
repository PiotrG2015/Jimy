package com.example.firsttry.android.eventapp;

import java.util.Calendar;

/**
 * Created by Piotr on 2015-07-21.
 * Class which represents object with data displayed on the details screen
 */

public class EventPage extends EventsList{

    private int dayOfWeek;
    private int month;
    private int dayInMonth;
    private int startHour;
    private int startMinute;
    private String place;
    private String longDescription;
    private String image;
    private Calendar cal;

    public Calendar getCal() {
        return cal;
    }

    public void setCal(Calendar cal) {
        this.cal = cal;
    }



    public EventPage() {
        super();
        this.dayOfWeek = 0;
        this.month = 0;
        this.dayInMonth = 0;
        this.startHour = 0;
        this.startMinute = 0;
        this.place = "";
        this.longDescription = "";
        this.image = "";
        this.cal = null;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLongDescription() {

        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public String getDayOfWeek() {

        switch(dayOfWeek){

            case 1:
                return "Sunday";

            case 2:
                return "Monday";

            case 3:
                return "Tuesday";

            case 4:
                return "Wedensday";

            case 5:
                return "Thursday";

            case 6:
                return "Friday";

            case 7:
                return "Saturday";

            default:
                return "";
        }
    }

    public void setDayOfWeek(int dayOfWeek) {

        this.dayOfWeek = dayOfWeek;
    }

    public void setMonth(int month) {

        this.month = month;
    }

    public String getMonth() {

        switch(month){

            case 0:
                return "January";

            case 1:
                return "February";

            case 2:
                return "March";

            case 3:
                return "April";

            case 4:
                return "May";

            case 5:
                return "June";

            case 6:
                return "July";

            case 7:
                return "August";

            case 8:
                return "September";

            case 9:
                return "October";

            case 10:
                return "November";

            case 11:
                return "December";

            default:
                return "";
        }
    }

    public String getDayInMonth() {
        return Integer.toString(dayInMonth);
    }

    public void setDayInMonth(int dayInMonth) {
        this.dayInMonth = dayInMonth;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public void setStartMinute(int startMinute) {
        this.startMinute = startMinute;
    }

    public String getStartTime(){
        if(startHour <= 9 && startMinute <=9){
            return "0"+ startHour + ":0" + startMinute;
        } else if (startHour <= 9 && startMinute > 9) {
            return "0"+ startHour + ":" + startMinute;
        } else if (startHour > 9 && startMinute <= 9) {
            return startHour + ":0" + startMinute;
        } else return startHour + ":" + startMinute;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }
}
