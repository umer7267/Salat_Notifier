package com.quince.salatnotifier.notifications;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

public class AlarmScheduler {

    private static final String TAG = "AlarmScheduler";

    public void setRepeatAlarm(Context context, long alarmTime, int id, String name, long repeatTime){

        Log.d(TAG, "setRepeatAlarm: AlarmSet: " + alarmTime);
        
        AlarmManager manager = AlarmManagerProvider.getAlarmManager(context);

        PendingIntent operation = NotificationService.getReminderPendingIntent(context, id, name);

        manager.setRepeating(AlarmManager.RTC_WAKEUP, alarmTime, repeatTime, operation);

//        Calendar time = Calendar.getInstance();
//        time.setTimeInMillis(alarmTime);
//
//        int month = time.get(Calendar.MONTH) + 1;
//        int day = time.get(Calendar.DAY_OF_MONTH);
//        int hour = time.get(Calendar.HOUR_OF_DAY);
//        int mint = time.get(Calendar.MINUTE);
//
//        Toast.makeText(context, "Reminder is Added for: " + month + "/" + day + " " + hour + ":" + mint, Toast.LENGTH_SHORT).show();
    }

}
