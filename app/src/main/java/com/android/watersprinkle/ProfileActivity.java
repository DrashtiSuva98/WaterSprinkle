package com.android.watersprinkle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
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

import java.util.Arrays;
import java.util.List;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{


    private DatabaseReference databaseReference;
    private TextView profileCNameTextView, profileAddressTextView, profilePhonenoTextView, txtcy, txtst;
    private FirebaseAuth firebaseAuth;
    private ImageView profilePicImageView;
    private StorageReference storageReference;
    private TextView textViewemailname;
    private Button btnCname, btnPh, btnAdd, btnState, btnCity, btnLogout;
    String edselst, edselcy;
    Spinner etUserCity;
    private static final int GALL_PICK = 1;
    FirebaseUser user;
    private ProgressDialog progress;
    final List Guj1 = Arrays.asList("--Select--", "Ahemdabad", "Rajkot", "Surat", "Vadodara", "Jamanagar", "Anand", "Valsad", "Vapi");
    final List Mh1 = Arrays.asList("--Select--", "Borivali", "Andheri", "Kandivali", "Malad", "Vile Parle");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        final List Guj1 = Arrays.asList("--Select--", "Ahemdabad", "Rajkot", "Surat", "Vadodara", "Jamanagar", "Anand", "Valsad", "Vapi");
        final List Mh1 = Arrays.asList("--Select--", "Borivali", "Andheri", "Kandivali", "Malad", "Vile Parle");

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Dealer");
        profilePicImageView = findViewById(R.id.profile_pic_imageView);
        profilePicImageView.setOnClickListener(this);
        profileCNameTextView = findViewById(R.id.profile_cname_textView);
        profileAddressTextView = findViewById(R.id.profile_address_textView);
        profilePhonenoTextView = findViewById(R.id.profile_phoneno_textView);
        btnCname = findViewById(R.id.buttonEditCName);
        btnCname.setOnClickListener(this);
        btnPh = findViewById(R.id.buttonEditPhoneNo);
        btnPh.setOnClickListener(this);
        btnAdd = findViewById(R.id.buttonEditAddress);
        btnAdd.setOnClickListener(this);
        btnState = findViewById(R.id.buttonEditState);
        btnState.setOnClickListener(this);
        btnCity = findViewById(R.id.buttonEditCity);
        btnCity.setOnClickListener(this);
        btnLogout = findViewById(R.id.btn_log_out);
        btnLogout.setOnClickListener(this);
        txtcy = findViewById(R.id.profile_city_textView);
        txtst = findViewById(R.id.profile_state_textView);
        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        user = firebaseAuth.getCurrentUser();
        if (user == null) {
            startActivity(new Intent(getApplicationContext(), SignIn.class));
        }

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User userProfile = dataSnapshot.child(user.getUid()).getValue(User.class);
                profileCNameTextView.setText(userProfile.getCompanyName());
                profileAddressTextView.setText(userProfile.getAddress());
                profilePhonenoTextView.setText(userProfile.getMobileNo());
                txtst.setText(userProfile.getSelSt());
                edselst = txtst.getText().toString();
                txtcy.setText(userProfile.getSelCy());
                textViewemailname = findViewById(R.id.textViewEmailAdress);
                textViewemailname.setText(user.getEmail());
                String image = dataSnapshot.child(user.getUid()).child("image").getValue().toString();
                Picasso.get().load(image).placeholder(R.drawable.defavatar).into(profilePicImageView);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonEditCName:

                LayoutInflater inflater = getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.layout_custom_dialog_edit_cname, null);
                final EditText etCname = alertLayout.findViewById(R.id.et_cname);
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("Edit CompanyName");
                // this is set the view from XML inside AlertDialog
                alert.setView(alertLayout);
                // disallow cancel of AlertDialog on click of back button and outside touch
                alert.setCancelable(false);
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String cname = etCname.getText().toString();
                        databaseReference.child(user.getUid()).child("companyName").setValue(cname);
                        etCname.onEditorAction(EditorInfo.IME_ACTION_DONE);
                    }
                });
                AlertDialog dialog = alert.create();
                dialog.show();
                break;

            case R.id.buttonEditAddress:

                LayoutInflater inflater1 = getLayoutInflater();
                View alertLayout1 = inflater1.inflate(R.layout.layout_custom_dialog_edit_address, null);
                final EditText etAddress = alertLayout1.findViewById(R.id.et_address);
                AlertDialog.Builder alert1 = new AlertDialog.Builder(this);
                alert1.setTitle("Edit Address");
                // this is set the view from XML inside AlertDialog
                alert1.setView(alertLayout1);
                // disallow cancel of AlertDialog on click of back button and outside touch
                alert1.setCancelable(false);
                alert1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                alert1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String Address = etAddress.getText().toString();
                        databaseReference.child(user.getUid()).child("address").setValue(Address);
                        etAddress.onEditorAction(EditorInfo.IME_ACTION_DONE);
                    }
                });
                AlertDialog dialog1 = alert1.create();
                dialog1.show();
                break;

            case R.id.buttonEditPhoneNo:

                LayoutInflater inflater2 = getLayoutInflater();
                View alertLayout2 = inflater2.inflate(R.layout.layout_custom_dialog_edit_mob, null);
                final EditText etUserPhoneno = alertLayout2.findViewById(R.id.et_mob);
                AlertDialog.Builder alert2 = new AlertDialog.Builder(this);
                alert2.setTitle("Edit MobileNo");
                // this is set the view from XML inside AlertDialog
                alert2.setView(alertLayout2);
                // disallow cancel of AlertDialog on click of back button and outside touch
                alert2.setCancelable(false);
                alert2.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                alert2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String phoneno = etUserPhoneno.getText().toString();
                        databaseReference.child(user.getUid()).child("mobileNo").setValue(phoneno);
                        etUserPhoneno.onEditorAction(EditorInfo.IME_ACTION_DONE);
                    }
                });
                AlertDialog dialog2 = alert2.create();
                dialog2.show();
                break;

            case R.id.buttonEditState:

                String[] spState = {"--Select--", "Gujarat", "Maharashrta"};
                LayoutInflater inflater3 = getLayoutInflater();
                View alertLayout3 = inflater3.inflate(R.layout.layout_custom_dialog_edit_state, null);
                final Spinner etUserState = alertLayout3.findViewById(R.id.et_state);
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spState);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                etUserState.setAdapter(dataAdapter);
                etUserState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                        edselst = parent.getItemAtPosition(position).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
                AlertDialog.Builder alert3 = new AlertDialog.Builder(this);
                alert3.setTitle("Edit State");
                // this is set the view from XML inside AlertDialog
                alert3.setView(alertLayout3);
                // disallow cancel of AlertDialog on click of back button and outside touch
                alert3.setCancelable(false);
                alert3.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                alert3.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        databaseReference.child(user.getUid()).child("selSt").setValue(edselst);
                    }
                });
                AlertDialog dialog3 = alert3.create();
                dialog3.show();
                break;

            case R.id.buttonEditCity:

                LayoutInflater inflater4 = getLayoutInflater();
                View alertLayout4 = inflater4.inflate(R.layout.layout_custom_dialog_edit_city, null);
                etUserCity = alertLayout4.findViewById(R.id.et_city);
                if (edselst.equals("Gujarat")) {
                    ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Guj1);
                    dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    etUserCity.setAdapter(dataAdapter1);
                } else if (edselst.equals("Maharashrta")) {
                    ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Mh1);
                    dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    etUserCity.setAdapter(dataAdapter2);
                }
                etUserCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        edselcy = adapterView.getItemAtPosition(i).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
                AlertDialog.Builder alert4 = new AlertDialog.Builder(this);
                alert4.setTitle("Edit City");
                // this is set the view from XML inside AlertDialog
                alert4.setView(alertLayout4);
                // disallow cancel of AlertDialog on click of back button and outside touch
                alert4.setCancelable(false);
                alert4.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                alert4.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        databaseReference.child(user.getUid()).child("selCy").setValue(edselcy);
                    }
                });
                AlertDialog dialog4 = alert4.create();
                dialog4.show();
                break;

            case R.id.btn_log_out:
                FirebaseAuth.getInstance().signOut();
                Intent inent = new Intent(getApplicationContext(), SignIn.class);
                startActivity(inent);
                break;

            case R.id.profile_pic_imageView:
                Intent gallery = new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(gallery, "SELECT IMAGE"), GALL_PICK);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALL_PICK && resultCode == RESULT_OK) {
            Uri imguri = data.getData();
            CropImage.activity(imguri)
                    .setAspectRatio(1, 1)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                progress = new ProgressDialog(this);
                progress.setTitle("Uploading Image...");
                progress.setMessage("Please wait while we upload and process the image.");
                progress.setCanceledOnTouchOutside(false);
                progress.show();

                Uri resultUri = result.getUri();
                StorageReference filepath = storageReference.child("DealerImages").child(user.getUid() + ".jpg");
                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            Task<Uri> result = task.getResult().getMetadata().getReference().getDownloadUrl();
                            result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String photoStringLink = uri.toString();
                                    databaseReference.child(user.getUid()).child("image").setValue(photoStringLink).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                progress.dismiss();
                                                Toast.makeText(ProfileActivity.this, "Success Uploading", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                }
                            });
                        } else {
                            progress.dismiss();
                            Toast.makeText(ProfileActivity.this, "Error in Uploading", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Log.e("Img", String.valueOf(error));
            }
        }
    }
}