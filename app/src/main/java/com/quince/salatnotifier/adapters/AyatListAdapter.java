package com.quince.salatnotifier.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.quince.salatnotifier.R;
import com.quince.salatnotifier.model.AyatModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AyatListAdapter extends RecyclerView.Adapter<AyatListAdapter.AyatViewHolder> {

    private Context context;
    private List<AyatModel> ayatLists;

    public AyatListAdapter(Context context, List<AyatModel> ayatLists) {
        this.context = context;
        this.ayatLists = ayatLists;
    }

    @NonNull
    @Override
    public AyatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AyatViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_ayat_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AyatViewHolder holder, int position) {
        AyatModel ayat = ayatLists.get(position);

        holder.no.setText(String.valueOf(position+1));
        holder.arabic.setText(ayat.getArabic());
        holder.urdu.setText(ayat.getTranslation());
    }

    @Override
    public int getItemCount() {
        return ayatLists.size();
    }

    public class AyatViewHolder extends RecyclerView.ViewHolder{

        TextView no, arabic, urdu;

        public AyatViewHolder(@NonNull View view) {
            super(view);

            no = view.findViewById(R.id.ayat_no);
            arabic = view.findViewById(R.id.ayat_arabic);
            urdu = view.findViewById(R.id.urdu_translation);
        }
    }
}
