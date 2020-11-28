package com.quince.salatnotifier.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.quince.salatnotifier.R;
import com.quince.salatnotifier.activities.ProblemDetailActivity;
import com.quince.salatnotifier.activities.ReciteHadeesActivity;
import com.quince.salatnotifier.model.ProblemModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProblemListAdapter extends RecyclerView.Adapter<ProblemListAdapter.ProblemViewHolder> {

    private Context context;
    private List<ProblemModel> problemsList;

    public ProblemListAdapter(Context context, List<ProblemModel> problemsList) {
        this.context = context;
        this.problemsList = problemsList;
    }

    @NonNull
    @Override
    public ProblemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProblemViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_problem_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProblemViewHolder holder, int position) {
        ProblemModel problem = problemsList.get(position);

        holder.problem_no.setText(String.valueOf(position+1));
        holder.title.setText(problem.getTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProblemDetailActivity.class);
                intent.putExtra("number", position+1);
                intent.putExtra("problem", problem);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return problemsList.size();
    }

    public class ProblemViewHolder extends RecyclerView.ViewHolder {

        TextView problem_no, title;

        public ProblemViewHolder(@NonNull View itemView) {
            super(itemView);

            problem_no = itemView.findViewById(R.id.problem_no);
            title = itemView.findViewById(R.id.problem_title);
        }
    }
}
