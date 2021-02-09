package com.atta.medicalcover.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.atta.medicalcover.Clinic;
import com.atta.medicalcover.DatesAdapter;
import com.atta.medicalcover.Doctor;
import com.atta.medicalcover.R;
import com.atta.medicalcover.SessionManager;
import com.atta.medicalcover.SlotsAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class BookingFragment extends Fragment {

    View root;

    RecyclerView daysRecyclerView, morningSlotsRecyclerView, afternoonSlotsRecyclerView, eveningSlotsRecyclerView;

    TextView doctorName, clinicName, selectedDateTv, morningSlotsTv, afternoonSlotsTv, eveningSlotsTv;

    ImageView doctorImage;

    Clinic clinic;

    Doctor doctor;

    ArrayList<String> dates = new ArrayList<>();
    ArrayList<String> morningSlots, afternoonSlots, eveningSlots ;
    String selectedDate;

    FirebaseFirestore db;

    private static final String TAG = "BookingFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root =  inflater.inflate(R.layout.fragment_booking, container, false);

        db = FirebaseFirestore.getInstance();

        clinic = BookingFragmentArgs.fromBundle(getArguments()).getClinic();
        doctor = BookingFragmentArgs.fromBundle(getArguments()).getDoctor();

        morningSlots = clinic.getMorningSlots();
        afternoonSlots = clinic.getAfternoonSlots();
        eveningSlots = clinic.getEveningSlots();

        daysRecyclerView = root.findViewById(R.id.daysRecyclerView);
        selectedDateTv = root.findViewById(R.id.selected_date_tv);

        doctorName = root.findViewById(R.id.doctor_name_tv);
        doctorName.setText(doctor.getName());

        doctorImage = root.findViewById(R.id.doctor_image);

        if (doctor.getImage() != null) {

            Picasso.get()
                    .load(doctor.getImage())
                    .resize(50, 50)
                    .centerCrop()
                    .into(doctorImage);


        }

        for (int i = clinic.getAvailableAfter(); i<clinic.getAvailableAfter()+7; i++){

            Calendar c = Calendar.getInstance();
            c.add(Calendar.DATE, i);
            Date date = c.getTime();
            String pattern = "EEE, dd MMM";
            SimpleDateFormat format = new SimpleDateFormat(pattern, new Locale("en", "US"));
            dates.add(format.format(date));
        }

        showDatesRecycler();

        clinicName = root.findViewById(R.id.clinic_tv);
        clinicName.setText(clinic.getName());


        morningSlotsTv = root.findViewById(R.id.morning_slots_tv);
        String morningSlotsSize = clinic.getMorningSlots().size() + " slots";
        morningSlotsTv.setText(morningSlotsSize);
        afternoonSlotsTv = root.findViewById(R.id.afternoon_slots_tv);
        String afternoonSlotsSize = clinic.getAfternoonSlots().size() + " slots";
        afternoonSlotsTv.setText(afternoonSlotsSize);
        eveningSlotsTv = root.findViewById(R.id.evening_slots_tv);
        String eveningSlotsSize = clinic.getEveningSlots().size() + " slots";
        eveningSlotsTv.setText(eveningSlotsSize);
        morningSlotsRecyclerView = root.findViewById(R.id.morning_slots_recyclerView);
        afternoonSlotsRecyclerView = root.findViewById(R.id.afternoon_slots_recyclerView);
        eveningSlotsRecyclerView = root.findViewById(R.id.evening_slots_recyclerView);

        return root;
    }


    private void showDatesRecycler() {

        DatesAdapter datesAdapter = new DatesAdapter(dates, this, getContext());

        daysRecyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));


        daysRecyclerView.setAdapter(datesAdapter);


    }


    private void showSlotsRecycler(RecyclerView recyclerView, ArrayList<String> data) {

        SlotsAdapter myAdapter = new SlotsAdapter(data, this, getContext());

        recyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));


        recyclerView.setAdapter(myAdapter);


    }

    public void setSelectedDate(String date){
        selectedDate = date;
        selectedDateTv.setText(selectedDate);

        checkAppointment();

    }

    public void checkAppointment(){
        db.collection("Appointments").whereEqualTo("clinicId", clinic.getId())
                .whereEqualTo("date", selectedDate)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()){
                            for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                                Log.d(TAG, "Cached document data: " + documentSnapshot.getData());


                                morningSlots.removeIf(time -> documentSnapshot.getData().containsValue(time));
                                afternoonSlots.removeIf(time -> documentSnapshot.getData().containsValue(time));
                                eveningSlots.removeIf(time -> documentSnapshot.getData().containsValue(time));

                            }

                        }


                        showSlotsRecycler(morningSlotsRecyclerView, morningSlots);
                        showSlotsRecycler(afternoonSlotsRecyclerView, afternoonSlots);
                        showSlotsRecycler(eveningSlotsRecyclerView, eveningSlots);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }


    public void addAppointments(String timeSlot) {
        Map<String, Object> appointment = new HashMap<>();
        appointment.put("clinicId", clinic.getId());
        appointment.put("date", selectedDate);
        appointment.put("doctorId", doctor.getId());
        appointment.put("timeSlot", timeSlot);
        appointment.put("userId", SessionManager.getInstance(getContext()).getUserId());
        db.collection("Appointments").add(appointment)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getContext(), "Appointment booked", Toast.LENGTH_SHORT).show();

                        Navigation.findNavController(getActivity(), R.id.nav_host_fragment)
                                .navigate(BookingFragmentDirections.actionBookingFragmentToNavigationHome());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "something wrong", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}