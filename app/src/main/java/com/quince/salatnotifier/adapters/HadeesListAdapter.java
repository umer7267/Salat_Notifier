package com.quince.salatnotifier.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.quince.salatnotifier.R;
import com.quince.salatnotifier.activities.ReciteHadeesActivity;
import com.quince.salatnotifier.activities.ReciteSurahActivity;
import com.quince.salatnotifier.model.HadeesModel;
import com.quince.salatnotifier.model.SurahModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HadeesListAdapter extends RecyclerView.Adapter<HadeesListAdapter.HadeesViewHolder> {

    private Context context;
    private List<HadeesModel> ahadees;

    public HadeesListAdapter(Context context, List<HadeesModel> ahadees) {
        this.context = context;
        this.ahadees = ahadees;
    }

    @NonNull
    @Override
    public HadeesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HadeesViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_hadees_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HadeesViewHolder holder, int position) {
        HadeesModel hadees = ahadees.get(position);

        holder.hadees_no.setText(String.valueOf(position+1));
        holder.hadees_ar.setText(hadees.getArabic());
        holder.hadees_en.setText(hadees.getEnglish());
        holder.hadees_ur.setText(hadees.getUrdu());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ReciteHadeesActivity.class);
                intent.putExtra("number", position+1);
                intent.putExtra("hadees", hadees);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ahadees.size();
    }

    public class HadeesViewHolder extends RecyclerView.ViewHolder {

        TextView hadees_no, hadees_ar, hadees_en, hadees_ur;

        public HadeesViewHolder(@NonNull View view) {
            super(view);

            hadees_no = view.findViewById(R.id.hadees_no);
            hadees_ar = view.findViewById(R.id.hadees_ar);
            hadees_en = view.findViewById(R.id.hadees_en);
            hadees_ur = view.findViewById(R.id.hadees_ur);
        }
    }

}
