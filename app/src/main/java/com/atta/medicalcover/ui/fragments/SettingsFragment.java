package com.atta.medicalcover.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.atta.medicalcover.R;
import com.atta.medicalcover.SessionManager;
import com.atta.medicalcover.ui.AuthActivity;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsFragment extends Fragment {

    View root;

    CardView profileCard, visitsCard, labTestCard, radiologyCard, allergyCard, surgeryCard,
            vaccineCard, bloodPressureCard, bloodGlucoseCard, familyHistoryCard, requestsCard;

    TextView nameTxt, membershipTxt, policyTxt;

    ImageView logout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_settings, container, false);

        initiateViews();


        return root;
    }


    private void initiateViews(){

        logout = root.findViewById(R.id.logoutImg);
        logout.setOnClickListener(view -> {

            FirebaseAuth.getInstance().signOut();

            SessionManager.getInstance(getContext()).logout();
            Intent intent = new Intent(getContext(), AuthActivity.class);

            // Closing all the Activities
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getActivity().startActivity(intent);
        });
        profileCard = root.findViewById(R.id.profileCard);
        profileCard.setOnClickListener(view -> Navigation.findNavController(view)
                .navigate(SettingsFragmentDirections.actionNavigationSettingsToNavigationProfile()));

        visitsCard = root.findViewById(R.id.visitsCard);
        visitsCard.setOnClickListener(view -> Navigation.findNavController(view)
                .navigate(SettingsFragmentDirections.actionNavigationSettingsToNavigationVisits(null, null)));

        requestsCard = root.findViewById(R.id.requestsCard);
        requestsCard.setOnClickListener(view -> Navigation.findNavController(view)
                .navigate(SettingsFragmentDirections.actionNavigationSettingsToRequestsFragment()));

        labTestCard = root.findViewById(R.id.labTestsCard);
        labTestCard.setOnClickListener(view -> Navigation.findNavController(view)
                .navigate(SettingsFragmentDirections.actionNavigationSettingsToLabTestsFragment()));

        radiologyCard = root.findViewById(R.id.radiologyCard);
        radiologyCard.setOnClickListener(view -> Navigation.findNavController(view)
                .navigate(SettingsFragmentDirections.actionNavigationSettingsToRadiologyFragment()));

        allergyCard = root.findViewById(R.id.allergyCard);
        allergyCard.setOnClickListener(view -> Navigation.findNavController(view)
                .navigate(SettingsFragmentDirections.actionNavigationSettingsToAllergyFragment()));


        surgeryCard = root.findViewById(R.id.surgeryCard);
        surgeryCard.setOnClickListener(view -> Navigation.findNavController(view)
                .navigate(SettingsFragmentDirections.actionNavigationSettingsToSurgeryFragment()));


        vaccineCard = root.findViewById(R.id.vaccineCard);
        vaccineCard.setOnClickListener(view -> Navigation.findNavController(view)
                .navigate(SettingsFragmentDirections.actionNavigationSettingsToVaccineFragment()));


        bloodPressureCard = root.findViewById(R.id.bloodPressureCard);
        bloodPressureCard.setOnClickListener(view -> Navigation.findNavController(view)
                .navigate(SettingsFragmentDirections.actionNavigationSettingsToBloodPressureFragment()));


        bloodGlucoseCard = root.findViewById(R.id.bloodGlucoseCard);
        bloodGlucoseCard.setOnClickListener(view -> Navigation.findNavController(view)
                .navigate(SettingsFragmentDirections.actionNavigationSettingsToBloodGlucoseFragment()));


        familyHistoryCard = root.findViewById(R.id.familyHistoryCard);
        familyHistoryCard.setOnClickListener(view -> Navigation.findNavController(view)
                .navigate(SettingsFragmentDirections.actionNavigationSettingsToFamilyHistoryFragment()));



        nameTxt = root.findViewById(R.id.usernameTxt);
        nameTxt.setText(SessionManager.getInstance(getContext()).getUsername());
        membershipTxt = root.findViewById(R.id.membershipNoTxt);
        membershipTxt.setText(SessionManager.getInstance(getContext()).getMembershipNo());
        policyTxt = root.findViewById(R.id.policyNoTxt);
        policyTxt.setText(SessionManager.getInstance(getContext()).getPolicyNo());
    }
}