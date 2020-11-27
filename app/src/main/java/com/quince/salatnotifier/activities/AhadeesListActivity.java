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
import com.quince.salatnotifier.adapters.HadeesListAdapter;
import com.quince.salatnotifier.databinding.ActivityAhadeesListBinding;
import com.quince.salatnotifier.model.HadeesModel;
import com.quince.salatnotifier.model.SurahModel;
import com.quince.salatnotifier.utility.ConstantsUtilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AhadeesListActivity extends AppCompatActivity {
    private static final String TAG = "AhadeesListActivity";
    private Context context = AhadeesListActivity.this;

    private ActivityAhadeesListBinding binding;

    private List<HadeesModel> ahadees;
    private HadeesListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAhadeesListBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        ahadees = new ArrayList<>();
        adapter = new HadeesListAdapter(context, ahadees);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        binding.ahadeesLists.setLayoutManager(linearLayoutManager);
        binding.ahadeesLists.setAdapter(adapter);

        getAhadeesList();
    }

    private void getAhadeesList() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Fetching Ahadees...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ConstantsUtilities.API_BASE_URL + "ahadees", response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);

                if (jsonObject.getBoolean("status")){

                    JSONArray jsonArray = jsonObject.getJSONArray("ahadees");

                    for (int i=0; i<jsonArray.length(); i++){

                        JSONObject hadees = jsonArray.getJSONObject(i);

                        HadeesModel hadeesModel = new HadeesModel();

                        hadeesModel.setArabic(hadees.getString("arabic"));
                        hadeesModel.setEnglish(hadees.getString("english"));
                        hadeesModel.setUrdu(hadees.getString("urdu"));
                        hadeesModel.setReference(hadees.getString("reference"));

                        ahadees.add(hadeesModel);
                    }

                    progressDialog.dismiss();
                    adapter.notifyDataSetChanged();

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