package com.atta.medicalcover.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.atta.medicalcover.ImagesURLAdapter;
import com.atta.medicalcover.LabTestAdapter;
import com.atta.medicalcover.LabTestRecord;
import com.atta.medicalcover.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LabTestDetailsFragment extends Fragment {

    View root;

    LabTestRecord labTestRecord;

    RecyclerView testsRecyclerView, imagesRecyclerView;

    LabTestAdapter labTestAdapter;

    ImagesURLAdapter imagesURLAdapter;

    ImageView expandedImageView;

    TextView noteText, doctorText, placeText, dateTxt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_lab_test_details, container, false);

        labTestRecord = LabTestDetailsFragmentArgs.fromBundle(getArguments()).getRecord();

        initiateViews();

        return root;
    }

    private void initiateViews() {

        expandedImageView = root.findViewById(R.id.expanded_image);

        imagesRecyclerView = root.findViewById(R.id.img_recycler);
        imagesURLAdapter = new ImagesURLAdapter(getContext(), labTestRecord.getImages(),
                this, null);
        imagesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false));

        imagesRecyclerView.setAdapter(imagesURLAdapter);


        testsRecyclerView = root.findViewById(R.id.recyclerView2);
        labTestAdapter = new LabTestAdapter(labTestRecord.getLabTests(), getContext(), true);
        testsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));

        testsRecyclerView.setAdapter(labTestAdapter);


        noteText = root.findViewById(R.id.result);
        noteText.setText(labTestRecord.getNote());
        doctorText = root.findViewById(R.id.test_doctor);
        doctorText.setText(labTestRecord.getDoctorName());
        placeText = root.findViewById(R.id.test_place);
        placeText.setText(labTestRecord.getLabName());


        dateTxt = root.findViewById(R.id.test_date_tv);
        Date date = labTestRecord.getTimestamp().toDate();

        String pattern = "EEE, dd MMM";
        SimpleDateFormat format = new SimpleDateFormat(pattern, new Locale("en", "US"));
        dateTxt.setText(format.format(date));

    }

    public void zoomImageFromThumb(String image) {
        Picasso.get()
                .load(image)
                .into(expandedImageView);
        expandedImageView.setVisibility(View.VISIBLE);
        expandedImageView.setOnClickListener(v ->
                expandedImageView.setVisibility(View.GONE)
                );
    }
}