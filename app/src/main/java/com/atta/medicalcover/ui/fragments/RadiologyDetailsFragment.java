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
import com.atta.medicalcover.R;
import com.atta.medicalcover.RadiologyRecord;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RadiologyDetailsFragment extends Fragment {

    View root;

    RadiologyRecord radiologyRecord;

    RecyclerView imagesRecyclerView;

    ImagesURLAdapter imagesURLAdapter;

    ImageView expandedImageView;

    TextView titleTxt, resultText, doctorText, placeText, dateTxt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_radiology_details, container, false);

        radiologyRecord = RadiologyDetailsFragmentArgs.fromBundle(getArguments()).getRecord();

        initiateViews();

        return root;
    }
    private void initiateViews() {

        expandedImageView = root.findViewById(R.id.expanded_image);

        imagesRecyclerView = root.findViewById(R.id.img_recycler);
        imagesURLAdapter = new ImagesURLAdapter(getContext(), radiologyRecord.getImages(),
                null, this);
        imagesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false));

        imagesRecyclerView.setAdapter(imagesURLAdapter);

        resultText = root.findViewById(R.id.result);
        resultText.setText(radiologyRecord.getResult());
        titleTxt = root.findViewById(R.id.radio_title);
        titleTxt.setText(radiologyRecord.getTitle());
        doctorText = root.findViewById(R.id.test_doctor);
        doctorText.setText(radiologyRecord.getDoctorName());
        placeText = root.findViewById(R.id.test_place);
        placeText.setText(radiologyRecord.getCenterName());

        dateTxt = root.findViewById(R.id.test_date_tv);
        Date date = radiologyRecord.getTimestamp().toDate();

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