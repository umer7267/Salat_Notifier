package com.quince.salatnotifier.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.quince.salatnotifier.R;
import com.quince.salatnotifier.adapters.AyatListAdapter;
import com.quince.salatnotifier.databinding.ActivityReciteSurahBinding;
import com.quince.salatnotifier.model.AyatModel;
import com.quince.salatnotifier.model.SurahModel;
import com.quince.salatnotifier.utility.ConstantsUtilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ReciteSurahActivity extends AppCompatActivity {
    private static final String TAG = "ReciteSurahActivity";
    private Context context = ReciteSurahActivity.this;

    private ActivityReciteSurahBinding binding;

    private List<AyatModel> ayats;
    private AyatListAdapter ayatsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReciteSurahBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        ayats = new ArrayList<>();
        ayatsAdapter = new AyatListAdapter(context, ayats);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        binding.ayatsList.setLayoutManager(linearLayoutManager);
        binding.ayatsList.setAdapter(ayatsAdapter);

        Intent intent = getIntent();

        getAyatsofSurahNo(intent.getIntExtra("number", 1));

    }

    private void getAyatsofSurahNo(int number) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Fetching Surah " + number + "...");
        progressDialog.show();

        String URL = ConstantsUtilities.SURAH_LIST_BASE + "/" + number + ConstantsUtilities.SURAH_WITH_TRANSLATION;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getInt("code")==200){
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    JSONObject Arabic = jsonArray.getJSONObject(0);
                    JSONObject Translation = jsonArray.getJSONObject(1);

                    JSONArray ayatsArabic = Arabic.getJSONArray("ayahs");
                    JSONArray ayatsUrdu = Translation.getJSONArray("ayahs");

                    for (int i=0; i<ayatsArabic.length(); i++){

                        JSONObject ayatArabicDetail = ayatsArabic.getJSONObject(i);
                        JSONObject ayatUrduDetail = ayatsUrdu.getJSONObject(i);

                        AyatModel ayat = new AyatModel();
                        ayat.setArabic(ayatArabicDetail.getString("text"));
                        ayat.setTranslation(ayatUrduDetail.getString("text"));
                        
                        ayats.add(ayat);

                    }

                    progressDialog.dismiss();
                    ayatsAdapter.notifyDataSetChanged();

                } else {
                    progressDialog.dismiss();
                    Toast.makeText(context, "Unexpected Error!", Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e){
                progressDialog.dismiss();
                e.printStackTrace();
            }
        }, error -> {
            progressDialog.dismiss();
            Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
        });

        Volley.newRequestQueue(this).add(stringRequest);
    }
}