package com.example.negar.myapplication;

        import com.github.mikephil.charting.charts.LineChart;
        import com.github.mikephil.charting.charts.PieChart;
        import com.github.mikephil.charting.data.Entry;
        import com.github.mikephil.charting.data.LineData;
        import com.github.mikephil.charting.data.LineDataSet;
        import com.github.mikephil.charting.data.PieData;
        import com.github.mikephil.charting.data.PieDataSet;
        import com.github.mikephil.charting.utils.ColorTemplate;
        import com.github.mikephil.charting.utils.XLabels;
        import com.squareup.picasso.Picasso;

        import android.app.Activity;
        import android.app.DatePickerDialog;
        import android.app.Dialog;
        import android.content.Context;
        import android.content.Intent;
        import android.graphics.Bitmap;
        import android.graphics.Color;
        import android.graphics.drawable.Drawable;
        import android.net.Uri;
        import android.os.Environment;
        import android.os.Bundle;
        import android.view.LayoutInflater;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.MotionEvent;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.BaseAdapter;
        import android.widget.Button;
        import android.widget.DatePicker;
        import android.widget.EditText;
        import android.widget.ImageView;
        import android.widget.ListView;
        import android.widget.TextView;

        import java.io.File;
        import java.io.FileOutputStream;
        import java.util.ArrayList;
        import java.util.Calendar;
        import java.util.Date;
        import java.util.HashMap;
        import java.util.List;
        import java.util.Random;

public class ReportActivity extends Activity {

    private final static int SELECT_PICTURE = 1;
    ImageView imageView;
    private  File rootFile;
    //private myTarget target;
    private String fileName;
    LineChart lineChart;
    private Date fromDate;
    private Date toDate;
    private int whichDate;
    static final int DATE_DIALOG = 0;

    private int[] pics = {R.drawable.m5, R.drawable.m4, R.drawable.m3, R.drawable.m2, R.drawable.m1};
    private int[] colors = {R.color.c5, R.color.c4, R.color.c3, R.color.c2, R.color.c1};
    private String[] moodsName = {"happy", "smiley", "dont care", "confused", "sad"};

    private EditText editTextFromDate;
    private EditText editTextToDate;
    private Button showMoods;

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener(){
        public void onDateSet(DatePicker view, int year, int month, int day){
            if(whichDate == 1){
                fromDate.setYear(year);
                fromDate.setMonth(month + 1);
                fromDate.setDate(day);
            }
            if(whichDate == 2){
                toDate.setYear(year);
                toDate.setMonth(month + 1);
                toDate.setDate(day);
            }

            updateDisplay();

        }
    };

    private void updateDisplay(){
        editTextFromDate.setText(new StringBuilder()
                .append(fromDate.getMonth()).append("/")
                .append(fromDate.getDate()).append("/")
                .append(fromDate.getYear()).append(" "));
        editTextToDate.setText(new StringBuilder()
                .append(toDate.getMonth()).append("/")
                .append(toDate.getDate()).append("/")
                .append(toDate.getYear()).append(" "));

        List<MyMood> subMoods = getSubMoodList();
        updateLineChart(subMoods);
        updatePieChart(subMoods);
    }


    private List<MyMood> getSubMoodList(){
        List<MyMood> subMoods;
        if(fromDate.getYear() == toDate.getYear()){
            if(fromDate.getMonth() == toDate.getMonth()){
                subMoods = MyMood.find(MyMood.class, "year = ? and month = ? and day >= ? and day <= ?",
                        String.valueOf(fromDate.getYear()),
                        String.valueOf(fromDate.getMonth()),
                        String.valueOf(fromDate.getDate()), String.valueOf(toDate.getDate()));
            }
            else{
                subMoods = MyMood.find(MyMood.class, "year = ? and month = ? and day >= ?",
                        String.valueOf(fromDate.getYear()), String.valueOf(fromDate.getMonth()), String.valueOf(fromDate.getDate()));
                List<MyMood> subMoodsBetweenMonth = MyMood.find(MyMood.class, "year = ? and month > ? and month < ?",
                        String.valueOf(fromDate.getYear()), String.valueOf(fromDate.getMonth()), String.valueOf(toDate.getMonth()));
                List<MyMood> subMoodsLastMonth = MyMood.find(MyMood.class, "year = ? and month = ? and day <= ?",
                        String.valueOf(fromDate.getYear()), String.valueOf(toDate.getMonth()), String.valueOf(toDate.getDate()));

                subMoods.addAll(subMoodsBetweenMonth);
                subMoods.addAll(subMoodsLastMonth);
            }
        }
        else {
            subMoods = MyMood.find(MyMood.class, "year = ? and month = ? and day >=",
                    String.valueOf(fromDate.getYear()), String.valueOf(fromDate.getMonth()), String.valueOf(fromDate.getDate()));
            List<MyMood> subMoodsMonthsYear = MyMood.find(MyMood.class, "year = ? and month > ?",
                    String.valueOf(fromDate.getYear()), String.valueOf(fromDate.getMonth()));
            List<MyMood> subMoodsBetweenYear = MyMood.find(MyMood.class, "year > ? and year < ?",
                    String.valueOf(fromDate.getYear()), String.valueOf(toDate.getYear()));
            List<MyMood> subMoodsMonthLastYear = MyMood.find(MyMood.class, "year = ? and month < ? ",
                    String.valueOf(toDate.getYear()), String.valueOf(toDate.getMonth()));
            List<MyMood> subMoodsLastYear = MyMood.find(MyMood.class, "year = ? and month = ? and day <=",
                    String.valueOf(toDate.getYear()), String.valueOf(toDate.getMonth()), String.valueOf(toDate.getDate()));

            subMoods.addAll(subMoodsMonthsYear);
            subMoods.addAll(subMoodsBetweenYear);
            subMoods.addAll(subMoodsMonthLastYear);
            subMoods.addAll(subMoodsLastYear);
        }

        return subMoods;
    }



    private void buildRoot(){
        rootFile = new File(Environment.getExternalStorageDirectory().getPath() + "/whatsYourMood");
        if(!rootFile.exists()){
            try {
                if(rootFile.mkdir()){
                    //target = new myTarget(rootFile);
                }
                else
                    System.out.println("ERROR!!!!!!! Drirectory not created");

            }catch (Exception e){
                System.out.println("ERROR!!!!!!!!!!" + e.getMessage());
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        System.out.println("Just for testing the git");

        buildRoot();
        this.whichDate = 0;

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;

        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        System.out.println("((((((***** year month day in reprt is: " + year + " " + month + " " + day);

        this.fromDate = new Date();
        this.fromDate.setYear(year);
        this.fromDate.setMonth(month);
        this.fromDate.setDate(day - 2);

        this.toDate = new Date();
        this.toDate.setYear(year);
        this.toDate.setMonth(month);
        this.toDate.setDate(day);


        editTextFromDate = (EditText) findViewById(R.id.editTextFromDate);
        editTextToDate = (EditText) findViewById(R.id.editTextToDate);
        updateDisplay();
        editTextFromDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                whichDate = 1;
                showDialog(DATE_DIALOG);
                return false;
            }
        });

        editTextToDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                whichDate = 2;
                showDialog(DATE_DIALOG);
                return false;
            }
        });

        showMoods = (Button) findViewById(R.id.buttonShowMoods);

        showMoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               final Dialog dialog = new Dialog(ReportActivity.this);
                dialog.setContentView(R.layout.mood_detail_dialog);

                ListView listView = (ListView) dialog.findViewById(R.id.listView);

                MoodAdapter moodAdapter = new MoodAdapter();
                listView.setAdapter(moodAdapter);
                dialog.show();

//                final StableArrayAdapter adapter = new StableArrayAdapter(getBaseContext(), R.layout.listview_item, list);
//                listView.setAdapter(adapter);

            }
        });

    }



    public void updatePieChart(List<MyMood> subMoods){
        ArrayList<String> xValues = new ArrayList<String>();
        List<main_mood> mainMoods = main_mood.listAll(main_mood.class);
        for(int i = 0; i < mainMoods.size(); i++)
            xValues.add(mainMoods.get(i).moodname);

        int[] values = new int[mainMoods.size()];

        for(int i = 0; i < subMoods.size(); i++){
            String MoodName = subMoods.get(i).moodname;
            for(int j = 0; j < mainMoods.size(); j++){
                if(MoodName.equals(mainMoods.get(j).moodname)){
                    values[j]++;
                    break;
                }
            }
        }

        ArrayList<Entry> yValues = new ArrayList<Entry>();
        for(int i = 0; i < mainMoods.size(); i++)
            yValues.add(new Entry(values[i], i));

        drawPieChart(yValues, xValues);

    }

    public void drawPieChart(ArrayList<Entry> yValues, ArrayList<String> xValues){
        PieChart pieChart = (PieChart) findViewById(R.id.pieChart);
        pieChart.setHoleRadius(60f);
        pieChart.setDrawYValues(false);
        pieChart.setDrawCenterText(true);
        pieChart.setDrawXValues(true);
        pieChart.setRotationEnabled(true);
        pieChart.setUsePercentValues(true);
        pieChart.setCenterText(getResources().getString(R.string.pie));
        pieChart.setDescription(getResources().getString(R.string.des_pie));

        PieDataSet set1 = new PieDataSet(yValues, "Report Result");
        set1.setSliceSpace(3f);


        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(Color.rgb(255, 218, 15));
        colors.add(Color.rgb(164, 204, 1));
        colors.add(Color.rgb(0, 198, 255));
        colors.add(Color.rgb(255, 56, 164));
        colors.add(Color.rgb(193, 15, 183));
        set1.setColors(colors);

        PieData pieData = new PieData(xValues, set1);

        pieChart.setData(pieData);
        pieChart.setBackgroundColor(Color.rgb(255,255,255));
        pieChart.setDrawXValues(false);
        pieChart.invalidate();
    }

    public void updateLineChart(List<MyMood> subMoods){
        ArrayList<Entry> diagramValues = new ArrayList<Entry>();
        for(int  i = 0; i < subMoods.size(); i++){
            Entry entry = new Entry(subMoods.get(i).value, i);
            diagramValues.add(entry);
        }

        ArrayList<String> xValues = new ArrayList<String>();
        for(int i = 0; i < subMoods.size(); i++){
            xValues.add(String.valueOf(subMoods.get(i).month) + "/"+String.valueOf(subMoods.get(i).day) + " " + String.valueOf(subMoods.get(i).hour) + ":" + String.valueOf(subMoods.get(i).minute));
        }

        drawLineChart(diagramValues, xValues);
    }

    public void drawLineChart(ArrayList<Entry> diagramValues, ArrayList<String> xValues){
        lineChart = (LineChart) findViewById(R.id.Linechart);
        lineChart.setDescription("نمودار زمانی ");

        lineChart.setStartAtZero(true);
        lineChart.setDrawYValues(false);

        LineDataSet lineDataSet = new LineDataSet(diagramValues, "Your state");
        lineDataSet.setColor(Color.rgb(0,0,0));
        lineDataSet.setLineWidth(2f);

        ArrayList<LineDataSet> lineDataSetArrayList =  new ArrayList<LineDataSet>();
        lineDataSetArrayList.add(lineDataSet);


        LineData lineData = new LineData(xValues, lineDataSetArrayList);
        lineChart.setData(lineData);
/*
        lineChart.setDrawLegend(true);
        Legend legend = lineChart.getLegend();
        legend.setXEntrySpace(5f);
        legend.setYEntrySpace(5f);
        legend.setFormSize(20f);
        lineChart.setDrawLegend(true);
*/
        XLabels xLabels = lineChart.getXLabels();
        xLabels.setPosition(XLabels.XLabelPosition.BOTTOM);
        lineChart.invalidate();
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //main_mood.deleteAll(main_mood.class);
        Uri uri = data.getData();

        Picasso.with(this)
                .load(uri.toString())
                .into(this.imageView);
        fileName = "test3.jpg";
        Picasso.with(this)
                .load(uri.toString())
                .into(target);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_report, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG:
                if(whichDate == 1)
                    return new DatePickerDialog(this,
                            dateSetListener,
                            fromDate.getYear(), fromDate.getMonth() - 1, fromDate.getDay());
                if(whichDate == 2)
                    return new DatePickerDialog(this,
                            dateSetListener,
                            toDate.getYear(), toDate.getMonth() - 1, toDate.getDay());

        }
        return null;
    }

    class MoodAdapter extends BaseAdapter{

        List<MyMood> myMoods = getSubMoodList();

        @Override
        public int getCount() {
            return myMoods.size();
        }

        @Override
        public Object getItem(int position) {
            if(position >= myMoods.size())
                return null;
            return myMoods.get(position);
        }

        @Override
        public long getItemId(int position) {
            if(position >= myMoods.size())
                return 0;
            return myMoods.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(position >= myMoods.size())
                return null;
            if(convertView == null){
                LayoutInflater inflater = (LayoutInflater) getApplication().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.listview_item, parent,false);
            }

            TextView date = (TextView) convertView.findViewById(R.id.textViewDate);
            TextView time = (TextView) convertView.findViewById(R.id.textViewTime);
            ImageView moodPic = (ImageView) convertView.findViewById(R.id.ImageViewMood);
            TextView desc = (TextView) convertView.findViewById(R.id.textViewDesc);
            ImageView pic = (ImageView) convertView.findViewById(R.id.imageViewPic);
            TextView text = (TextView) convertView.findViewById(R.id.textViewText);

            //position o size check shavad

//            Bundle extras = getIntent().getExtras();
//            int pos = extras.getInt("position");

            MyMood myMood = myMoods.get(position);
            String str = myMood.moodname;
            int pos = 0;

            if (str.equals(moodsName[0])){
                pos = 0;
            }
            else if(str.equals(moodsName[1])){
                pos = 1;
            }
            else if(str.equals(moodsName[2])){
                pos = 2;
            }
            else if(str.equals(moodsName[3])){
                pos = 3;
            }
            else if(str.equals(moodsName[4])){
                pos = 4;
            }

            date.setText(new StringBuilder().append(myMood.year).append("/").append(myMood.month).append("/").append(myMood.day));
            time.setText(new StringBuilder().append(myMood.hour).append(":").append(myMood.minute));
            System.out.println("*&*&*&*&*& " + myMood.hour  + " " + myMood.minute);
            moodPic.setImageResource(pics[pos]);
            Relation relation = Relation.find(Relation.class, "submoodname = ?", myMood.subname).get(0);
            desc.setText(new StringBuilder().append(relation.descMood));
            if(!myMood.picpath.equals("")) {
                File file = new File(rootFile + "/" + myMood.picpath);
                if (file.exists()) {
                    Picasso.with(getApplication())
                            .load("file:" + file.toString())
                            .into(pic);

                }
            }
            text.setText(new StringBuilder().append(myMood.textmood));

            return convertView;
        }
    }
}





