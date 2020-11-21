package com.quince.salatnotifier.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.quince.salatnotifier.MainActivity;
import com.quince.salatnotifier.R;
import com.quince.salatnotifier.databinding.ActivityNamazTimingBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NamazTimingActivity extends AppCompatActivity {
    private static final String TAG = "NamazTimingActivity";
    private Context context = NamazTimingActivity.this;

    private ActivityNamazTimingBinding binding;

    SimpleDateFormat timeIn12Hour = new SimpleDateFormat("hh:mm a", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNamazTimingBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Log.d(TAG, "onCreate: " + getIntent().getStringExtra("city"));

        binding.city.setText(getIntent().getStringExtra("city"));
        binding.fajarTiming.setText(getTimeIn12Hour(MainActivity.Fajr));
        binding.dhuhrTiming.setText(getTimeIn12Hour(MainActivity.Dhuhr));
        binding.asrTiming.setText(getTimeIn12Hour(MainActivity.Asr));
        binding.maghribTiming.setText(getTimeIn12Hour(MainActivity.Maghrib));
        binding.ishaTiming.setText(getTimeIn12Hour(MainActivity.Isha));
    }

    private String getTimeIn12Hour(String namaz) {
        String Time[] = namaz.split(":");

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(Time[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(Time[1]));

        Date date = new Date(calendar.getTimeInMillis());

        return timeIn12Hour.format(date);
    }
}