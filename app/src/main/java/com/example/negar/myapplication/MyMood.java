package com.example.negar.myapplication;

import com.orm.SugarRecord;

/**
 * Created by negar on 1/31/15.
 */

public class MyMood extends SugarRecord<MyMood> {
    String moodname;
    String subname;
    String textmood;
    String picpath;
    int year;
    int month;
    int day;
    int hour;
    int minute;
    int value;


    public MyMood() {
    }

    public MyMood(String moodname, String textmood, String subname,String path, int year, int month, int day, int hour,int minute, int value) {
        this.moodname = moodname;
        this.textmood = textmood;
        this.subname = subname;
        picpath = path;
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.value = value;
    }
}
