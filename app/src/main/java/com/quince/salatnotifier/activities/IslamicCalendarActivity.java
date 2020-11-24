package com.quince.salatnotifier.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.quince.salatnotifier.R;
import com.quince.salatnotifier.databinding.ActivityIslamicCalendarBinding;

public class IslamicCalendarActivity extends AppCompatActivity {
    private static final String TAG = "IslamicCalendarActivity";
    private Context context = IslamicCalendarActivity.this;

    private ActivityIslamicCalendarBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIslamicCalendarBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


    }
}