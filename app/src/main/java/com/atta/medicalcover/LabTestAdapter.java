package com.atta.medicalcover;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LabTestAdapter extends RecyclerView.Adapter<LabTestAdapter.MyViewHolder> {

    private ArrayList<LabTest> labTests;
    private Context context;

    public LabTestAdapter(ArrayList<LabTest> data, Context context) {

        this.labTests = data;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;

        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.new_lab_test_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        final LabTest labTest = labTests.get(position);

        holder.testName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                labTest.setTest(text);
            }
        });

        holder.testNormal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                labTest.setNormal(text);
            }
        });

        holder.testResult.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                labTest.setResult(text);
            }
        });
    }

    @Override
    public int getItemCount() {
        return labTests.size();
    }

    public ArrayList<LabTest> getData(){
        return labTests;
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView testName, testResult, testNormal;

        MyViewHolder(View view) {
            super(view);
            testName = view.findViewById(R.id.test_name);
            testResult = view.findViewById(R.id.test_result);
            testNormal = view.findViewById(R.id.test_normal);

        }
    }


}
