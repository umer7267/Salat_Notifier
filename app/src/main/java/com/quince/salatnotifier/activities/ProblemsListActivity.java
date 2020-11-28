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
import com.quince.salatnotifier.adapters.ProblemListAdapter;
import com.quince.salatnotifier.databinding.ActivityProblemsListBinding;
import com.quince.salatnotifier.model.HadeesModel;
import com.quince.salatnotifier.model.ProblemModel;
import com.quince.salatnotifier.utility.ConstantsUtilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProblemsListActivity extends AppCompatActivity {
    private static final String TAG = "ProblemsListActivity";
    private Context context = ProblemsListActivity.this;

    private ActivityProblemsListBinding binding;

    private List<ProblemModel> problemsList;
    private ProblemListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProblemsListBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        problemsList = new ArrayList<>();
        adapter = new ProblemListAdapter(context, problemsList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        binding.problemsLists.setLayoutManager(linearLayoutManager);
        binding.problemsLists.setAdapter(adapter);

        getListofProblems();
    }

    private void getListofProblems() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Fetching Problems...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ConstantsUtilities.API_BASE_URL + "problems", response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);

                if (jsonObject.getBoolean("status")){

                    JSONArray jsonArray = jsonObject.getJSONArray("problems");

                    for (int i=0; i<jsonArray.length(); i++){

                        JSONObject problem = jsonArray.getJSONObject(i);

                        ProblemModel problemModel = new ProblemModel();

                        problemModel.setTitle(problem.getString("title"));
                        problemModel.setDetails(problem.getString("detail"));
                        problemModel.setReference(problem.getString("reference"));

                        problemsList.add(problemModel);
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