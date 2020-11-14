package com.quince.salatnotifier;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hassanjamil.hqibla.CompassActivity;
import com.hassanjamil.hqibla.Constants;
import com.hassanjamil.hqibla.GPSTracker;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.quince.salatnotifier.activities.Mosques;
import com.quince.salatnotifier.databinding.ActivityMainBinding;
import com.quince.salatnotifier.utility.ConstantsUtilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.view.View.INVISIBLE;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Context context = MainActivity.this;

    ActivityMainBinding binding;

    private static final String[] REQUESTED_PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    GPSTracker gps;
    Double lat, lng;

    public static String Fajr, Dhuhr, Asr, Maghrib, Isha;

    SimpleDateFormat timeIn12Hour = new SimpleDateFormat("hh:mm a", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        setContentView(view);

        getLocationPermission();

        binding.mosquesNearMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartMosquesActivity();
            }
        });

        binding.qiblaDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQiblaDirectionActivity();
            }
        });
    }

    private void getTodaysNamazTimings() {

        String URL = ConstantsUtilities.NAMAZ_TIMING + "?address=" + lat + ", " + lng;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);

                if (jsonObject.getInt("code") == 200){
                    UpdateUI(jsonObject.getJSONObject("data"));
                } else {
                    hideUI();
                }

            } catch (JSONException e) {
                Log.d(TAG, "getTodaysNamazTimings: JSON: " + e.getMessage());
            }
        }, error -> {
            Log.d(TAG, "getTodaysNamazTimings: Error: " + error.getMessage());
            Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
        });

        Log.d(TAG, "getTodaysNamazTimings: URL: " + stringRequest.getUrl());
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void UpdateUI(JSONObject response) {
        try {
            //Setting Dates -----Start------

            JSONObject date = response.getJSONObject("date");
            binding.englishFullDate.setText(date.getString("readable"));

            JSONObject islamic = date.getJSONObject("hijri");

            binding.islamicDate.setText(islamic.getString("day"));

            String islamicMonthYear = islamic.getJSONObject("month").getString("en") + ", " + islamic.getString("year");

            binding.islamicMonthYear.setText(islamicMonthYear);

            //Setting Dates -----End------

            //Namaz Timings -----Start------

            JSONObject timings = response.getJSONObject("timings");

            Fajr = timings.getString("Fajr");
            Dhuhr = timings.getString("Dhuhr");
            Asr = timings.getString("Asr");
            Maghrib = timings.getString("Maghrib");
            Isha = timings.getString("Isha");

            binding.currentNamaz.setText(getCurrentNamaz());

            binding.nextNamaz.setText(getNextNamaz());

            //Namaz Timings -----End------

        } catch (JSONException e){
            Log.d(TAG, "UpdateUI: " + e.getMessage());
        }

    }

    private String getCurrentNamaz() {

        long currentTime = System.currentTimeMillis();

        if (currentTime >= getNamazTimeinMillis(Fajr) && currentTime < getNamazTimeinMillis(Dhuhr))
            return "Fajr";
        else if (currentTime >= getNamazTimeinMillis(Dhuhr) && currentTime < getNamazTimeinMillis(Asr))
            return "Dhuhr";
        else if (currentTime >= getNamazTimeinMillis(Asr) && currentTime < getNamazTimeinMillis(Maghrib))
            return "Asr";
        else if (currentTime >= getNamazTimeinMillis(Maghrib) && currentTime < getNamazTimeinMillis(Asr))
            return "Maghrib";
        else
            return "Isha";
    }

    private long getNamazTimeinMillis(String namaz) {
        String Time[] = namaz.split(":");

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(Time[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(Time[1]));

        return calendar.getTimeInMillis();
    }

    private String getNextNamaz() {
        String current = binding.currentNamaz.getText().toString();

        if (current.equals("Fajr"))
            return "Next is Dhuhr " + getTimeIn12Hour(Dhuhr);
        else if (current.equals("Dhuhr"))
            return "Next is Asr " + getTimeIn12Hour(Asr);
        else if (current.equals("Asr"))
            return "Next is Maghrib " + getTimeIn12Hour(Maghrib);
        else if (current.equals("Maghrib"))
            return "Next is Isha " + getTimeIn12Hour(Isha);
        else
            return "Next is Fajr " + getTimeIn12Hour(Fajr);
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

    private void hideUI() {
    }

    private void startQiblaDirectionActivity() {
        Intent intent = new Intent(MainActivity.this, CompassActivity.class);
        intent.putExtra(Constants.TOOLBAR_TITLE, "Qibla Direction");		// Toolbar Title
        intent.putExtra(Constants.TOOLBAR_BG_COLOR, "#DC3C46");		// Toolbar Background color
        intent.putExtra(Constants.TOOLBAR_TITLE_COLOR, "#FFFFFF");	// Toolbar Title color
        intent.putExtra(Constants.COMPASS_BG_COLOR, "#DC3C46");		// Compass background color
        intent.putExtra(Constants.ANGLE_TEXT_COLOR, "#FFFFFF");		// Angle Text color
        intent.putExtra(Constants.DRAWABLE_DIAL, R.drawable.dial);	// Your dial drawable resource
        intent.putExtra(Constants.DRAWABLE_QIBLA, R.drawable.qibla); 	// Your qibla indicator drawable resource
        intent.putExtra(Constants.FOOTER_IMAGE_VISIBLE, View.VISIBLE);	// Footer World Image visibility
        intent.putExtra(Constants.LOCATION_TEXT_VISIBLE, View.VISIBLE); // Location Text visibility
        startActivity(intent);
    }

    private void StartMosquesActivity() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Dexter.withContext(this)
                    .withPermissions(REQUESTED_PERMISSIONS)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {

                            // check if all permissions are granted
                            if (report.areAllPermissionsGranted()) {
                                Intent intent = new Intent(context, Mosques.class);
                                startActivity(intent);
                            }

                            // check for permanent denial of any permission
                            if (report.isAnyPermissionPermanentlyDenied()) {
                                // show alert dialog navigating to Settings
                                showSettingsDialog();
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    })
                    .check();
        }
    }

    private void getLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Dexter.withContext(this)
                    .withPermissions(REQUESTED_PERMISSIONS)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {

                            // check if all permissions are granted
                            if (report.areAllPermissionsGranted()) {
                                fetch_GPS();
                            }

                            // check for permanent denial of any permission
                            if (report.isAnyPermissionPermanentlyDenied()) {
                                // show alert dialog navigating to Settings
                                showSettingsDialog();
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    })
                    .check();
        }
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.permission_title));
        builder.setMessage(getResources().getString(R.string.permission_text));
        builder.setPositiveButton(getResources().getString(R.string.goto_settings), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.location_negative), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    public void fetch_GPS() {

        gps = new GPSTracker(this);

        if(gps.getLocation() != null){
            lat = gps.getLocation().getLatitude();
            lng = gps.getLocation().getLongitude();

            Log.d(TAG, "fetch_GPS: " + lat + ", " + lng);

            getTodaysNamazTimings();

        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();

            // qiblatIndicator.isShown(false);
//            qiblatIndicator.setVisibility(INVISIBLE);
//            qiblatIndicator.setVisibility(View.GONE);
//            tvAngle.setText(getResources().getString(R.string.pls_enable_location));
//            tvYourLocation.setText(getResources().getString(R.string.pls_enable_location));
            /*if (item != null) {
                item.setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.gps_off));

            }*/
            // Toast.makeText(getApplicationContext(), "Please enable Location first and Restart Application", Toast.LENGTH_LONG).show();
        }
    }
}