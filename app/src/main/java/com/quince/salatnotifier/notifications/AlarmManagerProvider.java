package com.quince.salatnotifier.notifications;

import android.app.AlarmManager;
import android.content.Context;
import android.util.Log;

class AlarmManagerProvider {
    private static final String TAG = "AlarmManagerProvider";

    private static AlarmManager sAlarmManager;

    /*package*/ static synchronized AlarmManager getAlarmManager(Context context){
        Log.d(TAG, "getAlarmManager: ");
        if (sAlarmManager == null){
            Log.d(TAG, "getAlarmManager: null");
            sAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        }
        return sAlarmManager;
    }
}
