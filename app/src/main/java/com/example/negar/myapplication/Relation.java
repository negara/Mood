package com.example.negar.myapplication;

/**
 * Created by negar on 1/31/15.
 */
import com.orm.SugarRecord;

/**
 * Created by peyman on 1/30/15.
 */
public class Relation extends SugarRecord<Relation> {
    main_mood mainmood;
    String submoodname;

    //Uri moodPic;

    String descMood;
    int maxvalue;
    int minvalue;

    public Relation() {
    }

    public Relation(main_mood mood, String subMoodName, String descMood, int maxValue, int minValue) {
        this.mainmood = mood;
        this.submoodname = subMoodName;
        this.descMood = descMood;
        this.maxvalue = maxValue;
        this.minvalue = minValue;
    }
}
