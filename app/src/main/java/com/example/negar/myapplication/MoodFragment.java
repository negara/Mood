package com.example.negar.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MoodFragment extends Fragment {
    int position;
    private  String name;
    View v, tv;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        ViewGroup rootView = (ViewGroup) inflater.inflate(
//                R.layout.fragment_mood, container, false);
        final String mood = getArguments().getString("position");
        int rec = getArguments().getInt("pic");

        v = inflater.inflate(R.layout.fragment_mood, container, false);
        v.setBackgroundColor(getResources().getColor(getArguments().getInt("color")));
        name = getArguments().getString("name");
        tv = v.findViewById(R.id.mood_text);
        ((TextView) tv).setText(" ");
        ImageView iv = (ImageView) v.findViewById(R.id.imageView);
        iv.setImageResource(rec);


        iv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // ((TextView) tv).setText("Hereeeeee");
                Intent myIntent = new Intent(getActivity(), AddInfoActivity.class);
                myIntent.putExtra("myMood", mood);
                myIntent.putExtra("pos", getArguments().getInt("pos"));
                myIntent.putExtra("name", name);
                myIntent.putExtra("color", getArguments().getInt("color"));
                getActivity().startActivity(myIntent);
            }
        });

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }
}
