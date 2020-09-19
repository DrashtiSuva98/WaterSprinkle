package com.android.watersprinkle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import id.zelory.compressor.Compressor;

public class SetUpProfile extends AppCompatActivity  {

    ProgressDialog progressDialog;
    private static final String TAG = SetUpProfile.class.getSimpleName();
    private Button btnSave;
    private TextView textViewemailname;
    private EditText editTextCompantName;
    private EditText editTextAddress;
    private EditText editTextPhoneNo;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseUser currentFirebaseUser;
    private ImageView profileImageView;
    private FirebaseStorage firebaseStorage;
    Uri resultUri;
    private static int PICK_IMAGE = 123;
    private StorageReference storageReference;
    String photoStringLink;
    Spinner state,city;
    String selSt,selCy;
    String[] spState ={"--Select--","Gujarat","Maharashrta"};
    String[] Guj= {"--Select--","Ahemdabad","Rajkot","Surat","Vadodara","Jamanagar","Anand","Valsad","Vapi"};
    String[] Mh = {"--Select--","Borivali","Andheri","Kandivali","Malad","Vile Parle"};
    CropImage.ActivityResult result;

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==PICK_IMAGE && resultCode==RESULT_OK) {
            Uri imguri =data.getData();
            CropImage.activity(imguri)
                    .setAspectRatio(1,1)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                Picasso.get().load(resultUri).into(profileImageView);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_profile);

        currentFirebaseUser= FirebaseAuth.getInstance().getCurrentUser() ;
        if (currentFirebaseUser == null) {
            finish();
            startActivity(new Intent(getApplicationContext(), SignIn.class));
        }
        state= findViewById(R.id.State);
        city=findViewById(R.id.City);
        editTextCompantName = (EditText) findViewById(R.id.EditTextCompanyName);
        editTextAddress = (EditText) findViewById(R.id.EditTextAddress);
        editTextPhoneNo = (EditText) findViewById(R.id.EditTextPhoneNo);
        btnSave = (Button) findViewById(R.id.btnSaveButton);
        textViewemailname = (TextView) findViewById(R.id.textViewEmailAdress);
        textViewemailname.setText(currentFirebaseUser.getEmail());
        profileImageView = findViewById(R.id.update_imageView);
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();


        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileIntent = new Intent();
                profileIntent.setType("image/*");
                profileIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(profileIntent, "Select Image."), PICK_IMAGE);
            }
        });

       ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spState);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        state.setAdapter(dataAdapter);
        state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                selSt = parent.getItemAtPosition(position).toString();
                if (selSt.equals("Gujarat")){
                    ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, Guj);
                    dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    city.setAdapter(dataAdapter1);
                }
                else if(selSt.equals("Maharashrta")){
                    ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, Mh);
                    dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    city.setAdapter(dataAdapter2);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }});

        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selCy = adapterView.getItemAtPosition(i).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference();
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = new ProgressDialog(SetUpProfile.this);
                progressDialog.setMessage("Please wait while we upload your data.."); // Setting Message
                progressDialog.setTitle("Uploading.."); // Setting Title
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
                progressDialog.show(); // Display Progress Dialog
                progressDialog.setCancelable(false);
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            sendUserData();
                            Thread.sleep(5000);
                            startActivity(new Intent(SetUpProfile.this,MainActivity.class));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                    }
                }).start();
            }
        });
    }

    private void sendUserData()  {

        String CompanyName= editTextCompantName.getText().toString();
        String Address= editTextAddress.getText().toString();
        String MobileNo=editTextPhoneNo.getText().toString();
        User user=new User(CompanyName,Address,MobileNo,selSt,selCy);
        mFirebaseDatabase.child("Dealer").child(currentFirebaseUser.getUid()).setValue(user);

        if (resultUri == null) {

            StorageReference filepath=storageReference.child("DealerImages").child(currentFirebaseUser.getUid()+".jpg");
            filepath.putBytes(getByteArray(profileImageView)).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful()){
                        Task<Uri> result = task.getResult().getMetadata().getReference().getDownloadUrl();
                        result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                photoStringLink = uri.toString();
                                mFirebaseDatabase.child("Dealer").child(currentFirebaseUser.getUid()).child("image").setValue(photoStringLink).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                        }
                                    }
                                });
                            }
                        });
                    }else{
                        Toast.makeText(getApplicationContext(),"Error in Uploading",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        else{
        resultUri = result.getUri();

            StorageReference filepath=storageReference.child("DealerImages").child(currentFirebaseUser.getUid()+".jpg");
                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){
                            Task<Uri> result = task.getResult().getMetadata().getReference().getDownloadUrl();
                            result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                     photoStringLink = uri.toString();
                                    mFirebaseDatabase.child("Dealer").child(currentFirebaseUser.getUid()).child("image").setValue(photoStringLink).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                           }
                                        }
                                    });
                                }
                            });
                        }else{

                            Toast.makeText(getApplicationContext(),"Error in Uploading",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }}


    public byte[] getByteArray(ImageView imageView){
        // Get the data from an ImageView as bytes
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = imageView.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        return data;
    }

}