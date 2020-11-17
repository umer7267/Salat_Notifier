package com.quince.salatnotifier.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.quince.salatnotifier.R;
import com.quince.salatnotifier.adapters.SurahListAdapter;
import com.quince.salatnotifier.databinding.ActivitySurahListBinding;
import com.quince.salatnotifier.model.SurahModel;
import com.quince.salatnotifier.utility.ConstantsUtilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SurahListActivity extends AppCompatActivity {
    private static final String TAG = "SurahListActivity";
    private Context context = SurahListActivity.this;

    private ActivitySurahListBinding binding;

    private List<SurahModel> listOfSurahs;
    private SurahListAdapter surahsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySurahListBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        listOfSurahs = new ArrayList<>();
        surahsAdapter = new SurahListAdapter(context, listOfSurahs);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        binding.surahLists.setLayoutManager(linearLayoutManager);
        binding.surahLists.setAdapter(surahsAdapter);

        getSurahsList();
    }

    private void getSurahsList() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Fetching Surahs...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, ConstantsUtilities.SURAH_LIST_BASE, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getInt("code")==200){
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    for (int i=0; i<jsonArray.length(); i++){
                        JSONObject surah = jsonArray.getJSONObject(i);

                        SurahModel surahModel = new SurahModel();

                        surahModel.setNumber(surah.getInt("number"));
                        surahModel.setArName(surah.getString("name"));
                        surahModel.setEnName(surah.getString("englishName"));
                        surahModel.setNoOfAyahs(surah.getInt("numberOfAyahs"));
                        surahModel.setRevelationType(surah.getString("revelationType"));

                        listOfSurahs.add(surahModel);
                    }

                    progressDialog.dismiss();
                    surahsAdapter.notifyDataSetChanged();

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