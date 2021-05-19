package com.atta.medicalcover.ui.fragments;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.atta.medicalcover.Doctor;
import com.atta.medicalcover.ImagesAdapter;
import com.atta.medicalcover.LabTest;
import com.atta.medicalcover.LabTestAdapter;
import com.atta.medicalcover.R;
import com.atta.medicalcover.SessionManager;
import com.atta.medicalcover.TestCenter;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.esafirm.imagepicker.model.Image;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class NewLabTestFragment extends Fragment {

    View root;

    private static final int PICK_IMAGE_REQUEST = 234;

    ArrayList<LabTest> labTests;
    List<Bitmap> imagesBitmap = new ArrayList<>();
    List<Uri> paths = new ArrayList<Uri>();
    List<String> imagesName = new ArrayList<>();
    ArrayList<String> uploadUrls = new ArrayList();

    RecyclerView testsRecyclerView, imagesRecyclerView;

    LabTestAdapter labTestAdapter;

    ImageView addLabTest, selectFile, selectedImg, calenderImg;

    TextInputEditText noteText;

    AutoCompleteTextView doctorText, placeText;

    TextView dateTv;

    Button saveBtn;

    private Uri filePath;

    FirebaseStorage storage;

    FirebaseFirestore db;

    String uploadUrl;

    Date date;

    Timestamp timestamp;

    Calendar myCalendar;

    DatePickerDialog.OnDateSetListener dateSetListener;


    ArrayList<Doctor> doctors = new ArrayList<>();
    ArrayList<String> doctorsName = new ArrayList<>();
    Doctor selectedDoctor = new Doctor();

    ArrayList<TestCenter> testCenters = new ArrayList<>();
    ArrayList<String> labsName = new ArrayList<>();
    TestCenter selectedCenter = new TestCenter();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root =  inflater.inflate(R.layout.fragment_new_lab_test, container, false);

        initiateViews();

        return root;
    }

    private void initiateViews() {
        labTests = new ArrayList<>();

        labTests.add(new LabTest());

        storage = FirebaseStorage.getInstance();
        db = FirebaseFirestore.getInstance();

        getDoctors();

        getPlaces();

        dateTv = root.findViewById(R.id.test_date_tv);
        date = new Date();
        String pattern = "EEE, dd MMM";
        SimpleDateFormat format = new SimpleDateFormat(pattern, new Locale("en", "US"));
        dateTv.setText("Date: " + format.format(date));


        myCalendar = Calendar.getInstance();

        dateSetListener = (view, year, monthOfYear, dayOfMonth) -> {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            String myFormat = "MMMM d, yyyy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            date = myCalendar.getTime();
            dateTv.setText(sdf.format(myCalendar.getTime()));
        };


        calenderImg = root.findViewById(R.id.calender_img);
        calenderImg.setOnClickListener(view ->
                viewCalender()
                );

        imagesRecyclerView = root.findViewById(R.id.img_recycler);

        testsRecyclerView = root.findViewById(R.id.recyclerView2);
        labTestAdapter = new LabTestAdapter(labTests, getContext());
        testsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));

        testsRecyclerView.setAdapter(labTestAdapter);

        noteText= root.findViewById(R.id.radio_result);
        doctorText = root.findViewById(R.id.test_doctor);
        placeText = root.findViewById(R.id.test_place);

        addLabTest = root.findViewById(R.id.addLabTestImg);
        addLabTest.setOnClickListener(view -> {

            labTests.add(new LabTest());
            labTestAdapter.notifyDataSetChanged();
        });

        //selectedImg = root.findViewById(R.id.img_recycler);

        selectFile = root.findViewById(R.id.selectFileImg);
        selectFile.setOnClickListener(v -> selectFile());

        saveBtn = root.findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(v -> {
            if (paths.size() != 0){
                for (int i = 0; i < paths.size(); i++) {
                    uploadFile(i);
                }
            }else {
                saveRecord();
            }

        });
    }

    private void viewCalender() {


        new DatePickerDialog(getContext(), dateSetListener, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();

    }


    public void selectFile(){
        ImagePicker.create(this)
                .returnMode(ReturnMode.NONE) // set whether pick and / or camera action should return immediate result or not.
                .folderMode(true) // folder mode (false by default)
                .toolbarFolderTitle("Folder") // folder selection title
                .toolbarImageTitle("Tap to select") // image selection title
                .toolbarArrowColor(Color.BLACK) // Toolbar 'up' arrow color
                .multi() // multi mode (default mode)
                .limit(3) // max images can be selected (99 by default)
                .showCamera(true) // show camera or not (true by default)
                .enableLog(false) // disabling log
                .start();
    }



    private void selectFile2() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                selectedImg.setImageBitmap(bitmap);
                selectedImg.setVisibility(View.VISIBLE);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            // Get a list of picked images
            List<Image> imageList = ImagePicker.getImages(data);
            data.getData();

            if (!imageList.isEmpty()|| imageList != null){


                if (imagesBitmap.size() == 3){
                    imagesBitmap.clear();
                }
                for (int i = 0; i < imageList.size(); i++){
                    imagesBitmap.add(getBitmapFromPath(imageList.get(i).getPath()));
                    paths.add(Uri.parse("file://" + imageList.get(i).getPath()));
                    imagesName.add(imageList.get(i).getName());
                }

                if (imagesBitmap.size() == 3){
                    //addImages.setVisibility(View.GONE);
                }
                ImagesAdapter myAdapter = new ImagesAdapter(getContext(), imagesBitmap,
                        this, null);
                imagesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                        LinearLayoutManager.HORIZONTAL, false));
                imagesRecyclerView.setAdapter(myAdapter);
            }

        }
    }

    private Bitmap getBitmapFromPath(String filePath) {

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        return BitmapFactory.decodeFile(filePath,bmOptions);

    }

    public void removeImage(int i){
        paths.remove(i);
        imagesName.remove(i);
    }

    private void uploadFile(int i) {
        //if there is a file to upload
        if (paths.size() != 0) {
            //displaying a progress dialog while upload is going on
            final ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            StorageReference ref = storage.getReference().child("Lab Tests/"+ imagesName.get(i));
            ref.putFile(paths.get(i))
                    .addOnSuccessListener(taskSnapshot -> {
                        //if the upload is successfull
                        //hiding the progress dialog
                        progressDialog.dismiss();
                        ref.getDownloadUrl().addOnSuccessListener(uri -> {
                            uploadUrls.add(uri.toString());
                            Log.d("FB storage", uri.toString());

                            saveRecord();

                        });

                        //and displaying a success toast
                        //Toast.makeText(getContext(), storage.getReference(ref.getPath()).toString(), Toast.LENGTH_LONG).show();
                    })
                    .addOnFailureListener(exception -> {
                        //if the upload is not successfull
                        //hiding the progress dialog
                        progressDialog.dismiss();

                        Log.e("FB storage", exception.getMessage());

                        //and displaying error message
                        Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                    })
                    .addOnProgressListener(taskSnapshot -> {
                        //calculating progress percentage
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                        //displaying percentage in progress dialog
                        progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                    });
        }
        //if there is not any file
        else {
            //you can display an error toast
        }
    }

    private void saveRecord() {

        if (uploadUrls.size() != paths.size()){
            return;
        }


        timestamp = new Timestamp(date);

        labTests = labTestAdapter.getData();
        String doctorName = doctorText.getText().toString().trim();
        if (selectedDoctor.getName() != null){
            if (!doctorName.equals(selectedDoctor.getName())){
                selectedDoctor = new Doctor();
            }
        }
        String placeName = placeText.getText().toString().trim();
        if (selectedCenter.getName() != null){
            if (!placeName.equals(selectedCenter.getName())){
                selectedCenter = new TestCenter();
            }
        }


        Map<String, Object> labTestRecord = new HashMap<>();
        labTestRecord.put("labTests", labTests);
        labTestRecord.put("note", noteText.getText().toString().trim());
        labTestRecord.put("doctorId", selectedDoctor.getId());
        labTestRecord.put("labId", selectedCenter.getId());
        labTestRecord.put("doctorName", doctorText.getText().toString().trim());
        labTestRecord.put("labName", placeText.getText().toString().trim());
        labTestRecord.put("timestamp", timestamp);
        labTestRecord.put("images", uploadUrls);
        labTestRecord.put("userId", SessionManager.getInstance(getContext()).getUserId());

        db.collection("LabTest Records").document().set(labTestRecord)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();

                    Navigation.findNavController(getActivity(), R.id.nav_host_fragment)
                            .navigate(NewLabTestFragmentDirections.actionNewLabTestFragmentToNavigationHome());
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "An error", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    public void getDoctors(){

        db.collection("Doctors")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()){


                        for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            Doctor doctor = documentSnapshot.toObject(Doctor.class);
                            doctor.setId(documentSnapshot.getId());
                            doctors.add(doctor);
                            doctorsName.add(doctor.getName());
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                                android.R.layout.select_dialog_item, doctorsName);
                        doctorText.setThreshold(1);//will start working from first character
                        doctorText.setAdapter(adapter);
                        doctorText.setOnItemClickListener((parent, view, position, id) ->
                                selectedDoctor = doctors.get(position)
                        );


                    }else {
                        Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show());
    }


    public void getPlaces(){

        db.collection("Services")
                .whereEqualTo("type", "Laboratory")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()){


                        for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            TestCenter testCenter = documentSnapshot.toObject(TestCenter.class);
                            testCenter.setId(documentSnapshot.getId());
                            testCenters.add(testCenter);
                            labsName.add(testCenter.getName());
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                                android.R.layout.select_dialog_item, labsName);
                        placeText.setThreshold(1);//will start working from first character
                        placeText.setAdapter(adapter);
                        placeText.setOnItemClickListener((parent, view, position, id) ->
                                selectedCenter = testCenters.get(position)
                        );

                    }else {
                        Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show());
    }

}