package com.example.negar.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

//TODO set defult subMood
//TODO report :D

public class AddInfoActivity extends Activity {
    String mood;
    String subMood;
    int SELECT_PICTURE = 1;
    String fileName;
    String name ;
    int sub_mood_value = 0;
    int pos;

    private int[] pics = {R.drawable.m5, R.drawable.m4, R.drawable.m3, R.drawable.m2, R.drawable.m1};
    private int[] colors = {R.color.c5, R.color.c4, R.color.c3, R.color.c2, R.color.c1};
    private  int[] str_moods = {R.string.mood1, R.string.mood2, R.string.mood3, R.string.mood4, R.string.mood5 };

    //map main moods to sub moods


    private HashMap<String, int[]> myMoods;

    Calendar calender;
    private File rootFile;
    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("&*&*&*&*&*&* onCreate ***************");
        fileName = "";

        setTitle("What is your Mood?");
        setContentView(R.layout.activity_add_info);
        View view = findViewById(R.id.main_layout);

        calender = Calendar.getInstance();
        imageView = (ImageView) findViewById(R.id.defualtView);


        myMoods = new HashMap<>();


        myMoods.put(main_mood.findById(main_mood.class, (long) 1).moodname, new int[]{R.drawable.cool, R.drawable.lol, R.drawable.tongue_out});
        myMoods.put(main_mood.findById(main_mood.class, (long) 2).moodname, new int[]{R.drawable.cool, R.drawable.happy, R.drawable.wink});
        myMoods.put(main_mood.findById(main_mood.class, (long) 3).moodname, new int[]{R.drawable.angel, R.drawable.vomited, R.drawable.surprised});
        myMoods.put(main_mood.findById(main_mood.class, (long) 4).moodname, new int[]{R.drawable.confused, R.drawable.question, R.drawable.crazy});
        myMoods.put(main_mood.findById(main_mood.class, (long) 5).moodname, new int[]{R.drawable.cry, R.drawable.angry, R.drawable.sad});


        rootFile = new File(Environment.getExternalStorageDirectory().getPath()+"/whatsYourMood");
        if (!rootFile.exists()){
            try {
                if(rootFile.mkdir());
                else {
                    System.out.println("Error : not created");
                }

            }
            catch (Exception e){
                System.out.println("Error: not exist" + e.getMessage());
            }
        }



        Bundle extras = getIntent().getExtras();

        ImageView iv = (ImageView) findViewById(R.id.mood_imageview);

        if (extras != null) {
            mood = extras.getString("myMood");
            name = extras.getString("name");
            pos = extras.getInt("pos");
            iv.setImageResource(pics[pos]);
            iv.setBackgroundColor(getResources().getColor(colors[pos]));

        }

        main_mood mm = main_mood.find(main_mood.class, "moodname = ?" , mood).get(0);

        subMood = (Relation.find(Relation.class, "mainmood = ?", String.valueOf(mm.getId())).get(0).submoodname);

        TextView tv = (TextView) findViewById(R.id.date_time_textview);
        String[] date = getDayMonth();
        tv.setText(date[3] + "    " + date[0] + " - " + date[1]);


        final NumberPicker np = (NumberPicker) findViewById(R.id.numberPicker_vaue);
        np.setMinValue(1);
        np.setMaxValue(10);
        np.setWrapSelectorWheel(false);
        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener(){

            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                sub_mood_value = newVal;
            }
        });

        final EditText ev = (EditText) findViewById(R.id.description_text);
        ev.setText("الان "+ getResources().getString(str_moods[pos])+" هستم زیرا : \n");

        final Button other_button = (Button) findViewById(R.id.other_button);
        other_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final Dialog dialog = new Dialog(AddInfoActivity.this);
                dialog.setContentView(R.layout.get_dialog);
                dialog.setTitle(getResources().getString(R.string.moodDescribe));

                final EditText editText = (EditText) dialog.findViewById(R.id.editText_describe);
                Button button = (Button) dialog.findViewById(R.id.button_set);
                button.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        //save submood
                        subMood = getResources().getString(R.string.other);
                        //System.out.println("^^^^^^ Searching for submood: " + subMood + " of mainMood: " + mood);
                        main_mood mm = main_mood.find(main_mood.class, "moodname = ?" , mood).get(0);
                        Relation r = Relation.find(Relation.class, "mainmood = ? and submoodname = ?", String.valueOf(mm.getId()), subMood ).get(0);
                        r.descMood = String.valueOf(editText.getText());
                        r.save();
                        alertSetMood(3);

                    }
                });


                dialog.show();

            }
        });

        final ImageButton emo_button1 = (ImageButton) findViewById(R.id.imageButton);
        emo_button1.setImageResource(myMoods.get(mood)[0]);
        emo_button1.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                alertSetMood(0);

            }
        });

        final ImageButton emo_button2 = (ImageButton) findViewById(R.id.imageButton2);
        emo_button2.setImageResource((myMoods.get(mood)[1]));
        emo_button2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //save submood
                alertSetMood(1);

            }
        });

        final ImageButton emo_button3 = (ImageButton) findViewById(R.id.imageButton3) ;
        emo_button3.setImageResource((myMoods.get(mood)[2]));

        emo_button3.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                //save submood
                alertSetMood(2);
            }
        });



        final Button upload =(Button)findViewById(R.id.upload_button);
        upload.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                fileName = calender.get(Calendar.MONTH)+calender.get(Calendar.DAY_OF_MONTH)+ calender.get(Calendar.HOUR_OF_DAY)
                        + calender.get(Calendar.MINUTE) + ".jpg";
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
            }
        });
        final Button submit = (Button) findViewById(R.id.submit_button);
        submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                MyMood my = new MyMood(mood, String.valueOf(ev.getText()), subMood, fileName,  cal.get(Calendar.YEAR) ,
                        cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY),
                        cal.get(Calendar.MINUTE) , 2 * sub_mood_value + (4 - pos) * 10);

                my.save();
                AlertDialog alertDialog = new AlertDialog.Builder(AddInfoActivity.this).create();
                alertDialog.setTitle(R.string.submit);
                alertDialog.setMessage(getResources().getString(R.string.store));
                fileName = "";
                alertDialog.show();

                Intent myIntent = new Intent(AddInfoActivity.this, ReportActivity.class);
                myIntent.putExtra("position", pos);
                AddInfoActivity.this.startActivity(myIntent);
            }
        });


    }

    private com.squareup.picasso.Target target = new com.squareup.picasso.Target() {

        @Override
        public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    File file = new File(rootFile.getPath() + "/" + fileName);
                    try
                    {
                        file.createNewFile();
                        FileOutputStream ostream = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 75, ostream);
                        ostream.close();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                }
            }).start();
        }
        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };

    private void alertSetMood(int num){
        AlertDialog alertDialog = new AlertDialog.Builder(AddInfoActivity.this).create();
        alertDialog.setTitle(R.string.set_subMood);
        main_mood mm = main_mood.find(main_mood.class, "moodname = ?" , mood).get(0);
        System.out.println("^^^^^^^^^^^^^^^^^" + (Relation.find(Relation.class, "mainmood = ?", String.valueOf(mm.getId())).get(num).submoodname));
        subMood = (Relation.find(Relation.class, "mainmood = ?", String.valueOf(mm.getId())).get(num).submoodname);
        String des = Relation.find(Relation.class, "mainmood = ?", String.valueOf(mm.getId())).get(num).descMood;
        alertDialog.setMessage(getResources().getString(str_moods[pos]) + " -> " + subMood + " \n " + des);
        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //main_mood.deleteAll(main_mood.class);
        //System.out.println("&*&*&*&*&*&*&*&*&*&*&*&*" + requestCode + " " + resultCode);
        if(resultCode == 0)
            return;

        Uri uri = data.getData();
        Picasso.with(this)
                .load(uri.toString())
                .into(this.imageView);
//        fileName = "test3.jpg";
        Picasso.with(this)
                .load(uri.toString())
                .into(target);


    }
    /**
     * set current date in jalali calendar
     * @return
     */
    public String[] getDayMonth(){
        String[] date = new String[4];

        Calendar cal = Calendar.getInstance();
        //cal.set(Calendar.DAY_OF_YEAR, position);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        Date d = new Date();
        d.setDate(day);
        d.setMonth(month);
        d.setYear(cal.YEAR);
        Shamsi shamsi = new Shamsi(d);

        date[0] = String.valueOf(shamsi.date);
        date[1] = shamsi.strMonth;
        date[2] = String.valueOf(shamsi.month);
        date[3] = shamsi.strWeekDay;
        return date;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_info, menu);
        return true;
    }

    public void pushNotif(){
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.emoticon1)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!");
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        AlertDialog alertDialog ;
        switch (item.getItemId()) {
            case R.id.action_settings:
                alertDialog = new AlertDialog.Builder(AddInfoActivity.this).create();
                alertDialog.setTitle(R.string.notif);
                alertDialog.setMessage("در صفحه‌ی اول تعیین کنید!");
                alertDialog.show();
                return true;

            case R.id.action_aboutUs:
                alertDialog = new AlertDialog.Builder(AddInfoActivity.this).create();
                alertDialog.setTitle(R.string.app_name);
                alertDialog.setMessage(AddInfoActivity.this.getString(R.string.about_us));
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
}
