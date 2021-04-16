package com.atta.medicalcover.ui.fragments;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.atta.medicalcover.R;
import com.atta.medicalcover.SessionManager;

public class SettingsFragment extends Fragment {

    View root;

    CardView profileCard, visitsCard;

    TextView nameTxt, membershipTxt, policyTxt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_settings, container, false);

        initiateViews();


        return root;
    }


    private void initiateViews(){
        profileCard = root.findViewById(R.id.profileCard);
        profileCard.setOnClickListener(view -> Navigation.findNavController(view)
                .navigate(SettingsFragmentDirections.actionNavigationSettingsToNavigationProfile()));

        visitsCard = root.findViewById(R.id.visitsCard);
        visitsCard.setOnClickListener(view -> Navigation.findNavController(view)
                .navigate(SettingsFragmentDirections.actionNavigationSettingsToNavigationVisits()));


        nameTxt = root.findViewById(R.id.usernameTxt);
        nameTxt.setText(SessionManager.getInstance(getContext()).getUsername());
        membershipTxt = root.findViewById(R.id.membershipNoTxt);
        membershipTxt.setText(SessionManager.getInstance(getContext()).getMembershipNo());
        policyTxt = root.findViewById(R.id.policyNoTxt);
        policyTxt.setText(SessionManager.getInstance(getContext()).getPolicyNo());
    }
}