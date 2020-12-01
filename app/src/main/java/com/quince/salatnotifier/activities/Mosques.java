package com.quince.salatnotifier.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.quince.salatnotifier.R;
import com.quince.salatnotifier.adapters.MosquesListAdapter;
import com.quince.salatnotifier.databinding.ActivityMosquesBinding;
import com.quince.salatnotifier.model.MosqueModel;
import com.quince.salatnotifier.utility.ConstantsUtilities;
import com.quince.salatnotifier.utility.GetLocation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Mosques extends AppCompatActivity {
    private static final String TAG = "Mosques";
    private Context context = Mosques.this;
    private Activity activity = this;

    ActivityMosquesBinding binding;
    ProgressDialog progressDialog;

    GetLocation locationTracker;
    Double lat, lng;

    private List<MosqueModel> mosques;
    private MosquesListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMosquesBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mosques = new ArrayList<>();

        locationTracker = new GetLocation(context, activity);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage(getResources().getString(R.string.finding));
        progressDialog.show();

    }

    public void getAllMosques(Double lat, Double lng) {

        this.lat = lat;
        this.lng = lng;

        Log.d(TAG, "getAllMosques: " + lat + " , " + lng);

        String URL = ConstantsUtilities.BASE_NEARBY_API + "location=" + lat + ", " + lng + "&radius=" + ConstantsUtilities.RADIUS + "&type=" + ConstantsUtilities.TYPE + "&key=" + ConstantsUtilities.API_KEY;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, response -> {
            progressDialog.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(response);
                mosques.clear();

                Log.d(TAG, "getAllMosques: " + jsonObject);

                JSONArray results = jsonObject.getJSONArray("results");

                for (int i=0; i<results.length()-1; i++){

                    JSONObject singleMosque = results.getJSONObject(i);

                    MosqueModel mosque = new MosqueModel();

                    mosque.setName(singleMosque.getString("name"));
                    mosque.setLat(singleMosque.getJSONObject("geometry").getJSONObject("location").getDouble("lat"));
                    mosque.setLng(singleMosque.getJSONObject("geometry").getJSONObject("location").getDouble("lng"));

                    mosques.add(mosque);

                    Log.d(TAG, "getAllMosques: " + mosque.getName() + ": " + mosque.getLat() + ", " + mosque.getLng());
                }

                initializeList();

            } catch (JSONException e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            Log.d(TAG, "getAllMosques: ");
            progressDialog.dismiss();
            Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
        });

        Log.d(TAG, "getAllMosques: URL: " + stringRequest.getUrl());
       //Volley.newRequestQueue(this).add(stringRequest);
    }

    private void initializeList() {
        Log.d(TAG, "initializeList: ");
        adapter = new MosquesListAdapter(context, mosques, lat, lng);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        binding.mosqueLists.setLayoutManager(linearLayoutManager);
        binding.mosqueLists.setAdapter(adapter);
    }
}