package com.example.negar.myapplication;

/**
 * Created by negar on 1/31/15.
 */


import com.orm.SugarRecord;


/**
 * Created by peyman on 1/28/15.
 */
public class main_mood extends SugarRecord<main_mood>{
    String moodname;
    String mooddesc;
    int moodvalue;


    public main_mood() {
    }

    public main_mood(String mood_name, String mood_desc, int mood_value) {
        this.moodname = mood_name;
        this.mooddesc = mood_desc;
        this.moodvalue = mood_value;
    }
}