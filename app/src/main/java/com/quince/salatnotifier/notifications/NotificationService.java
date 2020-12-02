package com.quince.salatnotifier.notifications;

import android.app.IntentService;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;

import com.quince.salatnotifier.R;
import com.quince.salatnotifier.activities.Mosques;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import static com.quince.salatnotifier.utility.ConstantsUtilities.CHANNEL_ID;

public class NotificationService extends IntentService {
    private static final String TAG = "NotificationSchedular";

    /**
     * @deprecated
     */
    public NotificationService() {
        super(TAG);
    }

    public static PendingIntent getReminderPendingIntent(Context context, int id, String name) {

        Log.d(TAG, "getReminderPendingIntent: AlarmSet: " + name);

        Intent action = new Intent(context, NotificationService.class);
        action.putExtra("id", id);
        action.putExtra("name", name);

        return PendingIntent.getService(context, id, action, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        int id = intent.getIntExtra("id", 0);
        String name = intent.getStringExtra("name");
        String description = "Click here to find near by mosques to offer your prayer on Time.";

        Log.d(TAG, "onHandleIntent: AlarmSet: " + id);

        Intent action = new Intent(this, Mosques.class);

        PendingIntent operation = TaskStackBuilder.create(this)
                .addNextIntentWithParentStack(action)
                .getPendingIntent(id, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder note = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(name + " Name Time")
                .setContentText(description)
                .setSmallIcon(R.drawable.ic_mosque)
                .setWhen(System.currentTimeMillis())
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(operation)
                .setAutoCancel(true)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setCategory(NotificationCompat.CATEGORY_REMINDER);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(id, note.build());
    }
}
