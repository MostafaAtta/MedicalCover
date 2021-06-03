package com.atta.medicalcover.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.atta.medicalcover.R;
import com.atta.medicalcover.Surgery;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SurgeryDetailsFragment extends Fragment {

    View root;

    Surgery surgery;

    TextView titleTxt, resultText, doctorText, placeText, dateTxt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_surgery_details, container, false);

        surgery = SurgeryDetailsFragmentArgs.fromBundle(getArguments()).getRecord();

        initiateViews();

        return root;
    }


    private void initiateViews() {

        resultText = root.findViewById(R.id.result);
        resultText.setText(surgery.getNote());
        titleTxt = root.findViewById(R.id.radio_title);
        titleTxt.setText(surgery.getTitle());
        doctorText = root.findViewById(R.id.test_doctor);
        doctorText.setText(surgery.getDoctorName());
        placeText = root.findViewById(R.id.test_place);
        placeText.setText(surgery.getPlaceName());

        dateTxt = root.findViewById(R.id.test_date_tv);
        Date date = surgery.getTimestamp().toDate();

        String pattern = "EEE, dd MMM";
        SimpleDateFormat format = new SimpleDateFormat(pattern, new Locale("en", "US"));
        dateTxt.setText(format.format(date));

    }
}