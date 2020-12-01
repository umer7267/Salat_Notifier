package com.quince.salatnotifier.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.quince.salatnotifier.R;
import com.quince.salatnotifier.model.MosqueModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MosquesListAdapter extends RecyclerView.Adapter<MosquesListAdapter.MosqueViewHolder> {
    private static final String TAG = "Mosques";

    private Context context;
    private List<MosqueModel> mosques;
    private Double curLat, curLng;

    public MosquesListAdapter(Context context, List<MosqueModel> mosques, Double curLat, Double curLng) {
        this.context = context;
        this.mosques = mosques;
        this.curLat = curLat;
        this.curLng = curLng;
    }

    @NonNull
    @Override
    public MosqueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MosqueViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_mosques_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MosqueViewHolder holder, int position) {
        MosqueModel mosque = mosques.get(position);

        holder.no.setText(String.valueOf(position+1));
        holder.name.setText(mosque.getName());
        holder.info.setText(String.format("%.1f", distance(curLat, curLng, mosque.getLat(), mosque.getLng())) + " Km");

        Log.d(TAG, "onBindViewHolder: " + mosque.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri = "http://maps.google.com/maps?saddr=" + curLat + "," + curLng + "&daddr=" + mosque.getLat() + "," + mosque.getLng();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                context.startActivity(intent);
            }
        });
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    @Override
    public int getItemCount() {
        return mosques.size();
    }

    public class MosqueViewHolder extends RecyclerView.ViewHolder {

        TextView no, name, info;

        public MosqueViewHolder(@NonNull View itemView) {
            super(itemView);

            no = itemView.findViewById(R.id.mosque_no);
            name = itemView.findViewById(R.id.mosque_name);
            info = itemView.findViewById(R.id.mosque_info);
        }
    }
}
