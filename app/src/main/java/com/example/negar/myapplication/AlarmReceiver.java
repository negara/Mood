package com.example.negar.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by negar on 2/3/15.
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        String phoneNumberReciver="987654321";
        String message="Hi I will be there later, See You Soon";
        Toast.makeText(context, "Alarm Triggered and SMS Sent", Toast.LENGTH_LONG);
    }

}
