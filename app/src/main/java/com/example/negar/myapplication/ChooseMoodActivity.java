package com.example.negar.myapplication;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.GregorianCalendar;
import java.util.HashMap;

public class ChooseMoodActivity extends FragmentActivity {
    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 5;
    private Callbacks mCallbacks;
    private int numberOfNotifs;
    private String name;

    public interface Callbacks {
        public void onBackPressedCallback();
    }

    private HashMap<String, String[]> moods;
    private String[] moodsName = {"happy", "smiley", "dont care", "confused", "sad"};
    private int[] pics = {R.drawable.m5, R.drawable.m4, R.drawable.m3, R.drawable.m2, R.drawable.m1};
    private int[] colors = {R.color.c5, R.color.c4, R.color.c3, R.color.c2, R.color.c1};
    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_mood);

        moods = new HashMap<>();
        moods.put(moodsName[0], new String[] {String.valueOf(R.string.cool), String.valueOf(R.string.lol), String.valueOf(R.string.tongue) });
        moods.put(moodsName[1], new String[] {String.valueOf(R.string.happy), String.valueOf(R.string.wink), String.valueOf(R.string.cool) });
        moods.put(moodsName[3], new String[] {String.valueOf(R.string.confused), String.valueOf(R.string.question), String.valueOf(R.string.crazy) });
        moods.put(moodsName[2], new String[] {String.valueOf(R.string.nerd), String.valueOf(R.string.sleeping), String.valueOf(R.string.surprised) });
        moods.put(moodsName[4], new String[] {String.valueOf(R.string.cry), String.valueOf(R.string.angry), String.valueOf(R.string.sad) });

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);


        if(main_mood.listAll(main_mood.class).isEmpty()){
            for (int i = 0; i < NUM_PAGES; i++){
                main_mood mm = new main_mood( moodsName[i], "des", 100 - 20 * i);
                mm.save();

            }
        }

        //Relation.deleteAll(Relation.class);
        if(Relation.listAll(Relation.class).isEmpty()){
            for (int i = 0; i < NUM_PAGES; i++){
                main_mood mm = main_mood.find(main_mood.class, "moodname = ?", moodsName[i]).get(0);
                for(int j = 0; j < 3; j++){
                    String str = getResources().getString(Integer.parseInt(moods.get(mm.moodname)[j]));
                    int val = mm.moodvalue;
                    Relation r = new Relation(mm, str , " ", val , val - 20);
                    r.save();

                }
                String sub = getResources().getString(R.string.other);
                Relation r = new Relation(mm, sub , " ", mm.moodvalue , mm.moodvalue - 20);
                r.save();

            }
        }
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_choose_mood, menu);
        return true;
    }

    public void pushNotif(){
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.emoticon1)
                        .setContentTitle("Whats Your Mood?")
                        .setContentText("سلام! حالت چطوره؟");
// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, ChooseMoodActivity.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(ChooseMoodActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        int mId = 7;
        mNotificationManager.notify(mId, mBuilder.build());
    }


    public void scheduleAlarm(View V){
        Long time= new GregorianCalendar().getTimeInMillis()+24*60*60*1000;
        Intent intentAlarm= new Intent(this, AlarmReceiver.class);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, time, PendingIntent.getBroadcast(this, 1, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));
        Toast.makeText(this, "Alarm Scheduled for Tomorrow", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.action_settings:



                final Dialog dialog = new Dialog(ChooseMoodActivity.this);
                dialog.setContentView(R.layout.notif_dialog);

                dialog.setTitle(R.string.notif);
                final EditText et = (EditText)dialog.findViewById(R.id.name_editText);
                RadioButton button0 = (RadioButton) dialog.findViewById(R.id.no_notif);
                button0.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        numberOfNotifs = 0;
                        name = et.getText().toString();
                        dialog.dismiss();
                    }
                });

                RadioButton button1 = (RadioButton) dialog.findViewById(R.id.once_notif);
                button1.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        numberOfNotifs = 1;
                        name = et.getText().toString();
                        dialog.dismiss();
                    }
                });

                RadioButton button3 = (RadioButton) dialog.findViewById(R.id.twice_notif);
                button3.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        numberOfNotifs = 3;
                        name = et.getText().toString();
                        dialog.dismiss();
                    }
                });


                RadioButton button5 = (RadioButton) dialog.findViewById(R.id.three_times_notif);
                button5.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        numberOfNotifs = 5;
                        name = et.getText().toString();
                        dialog.dismiss();
                    }
                });


                dialog.show();

                return true;

            case R.id.action_aboutUs:
                AlertDialog alertDialog = new AlertDialog.Builder(ChooseMoodActivity.this).create();
                alertDialog.setTitle(R.string.app_name);
                alertDialog.setMessage(ChooseMoodActivity.this.getString(R.string.about_us));
                //System.out.println("$$$$$" + numberOfNotifs);
                alertDialog.show();
                return true;
            case R.id.action_report:
                Intent intent = new Intent(this, ReportActivity.class);
                this.startActivity(intent);
                return true;

        }
        return false;
    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment frag = new MoodFragment();
            Bundle bundle = new Bundle();
            bundle.putString("position", moodsName[position]);
            bundle.putInt("pic", pics[position]);
            bundle.putInt("color", colors[position]);
            bundle.putInt("pos", position);
            bundle.putString("name", name);
            frag.setArguments(bundle);
            return frag;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}
