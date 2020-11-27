package com.quince.salatnotifier.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.quince.salatnotifier.R;
import com.quince.salatnotifier.databinding.ActivityReciteHadeesBinding;
import com.quince.salatnotifier.model.HadeesModel;

public class ReciteHadeesActivity extends AppCompatActivity {
    private static final String TAG = "ReciteHadeesActivity";
    private Context context = ReciteHadeesActivity.this;

    private ActivityReciteHadeesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReciteHadeesBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Intent intent = getIntent();

        binding.hadeesNo.setText(String.valueOf(intent.getIntExtra("number", 1)));

        HadeesModel hadees = (HadeesModel) intent.getSerializableExtra("hadees");

        binding.hadeesAr.setText(hadees.getArabic());
        binding.hadeesEn.setText(hadees.getEnglish());
        binding.hadeesUr.setText(hadees.getUrdu());
        binding.reference.setText(hadees.getReference());
    }
}