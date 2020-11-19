package com.quince.salatnotifier.notifications;

import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;

import com.quince.salatnotifier.R;
import com.quince.salatnotifier.activities.Mosques;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import static com.quince.salatnotifier.utility.ConstantsUtilities.CHANNEL_ID;

public class NotificationSchedular extends JobService {
    private static final String TAG = "NotificationSchedular";
    private Context context = NotificationSchedular.this;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        SendNotification();
        return true;
    }

    private void SendNotification() {
        new Thread(() -> {
            Intent intent = new Intent(context, Mosques.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_mosque)
                    .setContentTitle("Asr Namaz Time")
                    .setContentText("Click to Find Mosques Near You and Pray the Salat")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    // Set the intent that will fire when the user taps the notification
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

            // notificationId is a unique int for each notification that you must define
            notificationManager.notify(0, builder.build());
        }).start();
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return true;
    }
}
