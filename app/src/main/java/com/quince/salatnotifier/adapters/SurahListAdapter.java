package com.quince.salatnotifier.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.quince.salatnotifier.R;
import com.quince.salatnotifier.activities.ReciteSurahActivity;
import com.quince.salatnotifier.model.SurahModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SurahListAdapter extends RecyclerView.Adapter<SurahListAdapter.SurahItemViewHolder> {

    private Context context;
    private List<SurahModel> listOfSurahs;

    public SurahListAdapter(Context context, List<SurahModel> listOfSurahs) {
        this.context = context;
        this.listOfSurahs = listOfSurahs;
    }

    @NonNull
    @Override
    public SurahItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SurahItemViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_surah_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SurahItemViewHolder holder, int position) {
        SurahModel surah = listOfSurahs.get(position);

        holder.surah_no.setText(String.valueOf(surah.getNumber()));
        holder.surah_name_en.setText(surah.getEnName());
        holder.surah_name_ar.setText(surah.getArName());
        holder.surah_info.setText(surah.getRevelationType() + " - " + surah.getNoOfAyahs() + " Ayahs");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ReciteSurahActivity.class);
                intent.putExtra("number", surah.getNumber());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listOfSurahs.size();
    }

    public class SurahItemViewHolder extends RecyclerView.ViewHolder {

        TextView surah_no, surah_name_en, surah_info, surah_name_ar;

        public SurahItemViewHolder(@NonNull View view) {
            super(view);

            surah_no = view.findViewById(R.id.surah_no);
            surah_name_en = view.findViewById(R.id.surah_name_en);
            surah_info = view.findViewById(R.id.surah_info);
            surah_name_ar = view.findViewById(R.id.surah_name_ar);
        }
    }
}
