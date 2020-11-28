package com.quince.salatnotifier.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.quince.salatnotifier.R;
import com.quince.salatnotifier.databinding.ActivityProblemDetailBinding;
import com.quince.salatnotifier.model.ProblemModel;

public class ProblemDetailActivity extends AppCompatActivity {
    private static final String TAG = "ProblemDetailActivity";
    private Context context = ProblemDetailActivity.this;

    private ActivityProblemDetailBinding binding;

    private ProblemModel problem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProblemDetailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Intent intent = getIntent();

        binding.problemNo.setText(String.valueOf(intent.getIntExtra("number", 0)));

        problem = (ProblemModel) intent.getSerializableExtra("problem");

        binding.problemTitle.setText(problem.getTitle());
        binding.problemDetail.setText(problem.getDetails());
        binding.reference.setText(problem.getReference());

    }
}