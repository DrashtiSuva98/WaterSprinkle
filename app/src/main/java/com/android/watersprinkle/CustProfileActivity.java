package com.android.watersprinkle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class CustProfileActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private TextView txtcustnm, txtcustemail,txtcuststate,txtcustcity,txtcustmob,txtcustadd;
    private ImageView profilePicImageView;
    private StorageReference storageReference;
    String custKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cust_profile);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Customers");
        storageReference = FirebaseStorage.getInstance().getReference();
        profilePicImageView = findViewById(R.id.custPImg);
        txtcustnm=findViewById(R.id.custPName);
        txtcustemail=findViewById(R.id.custPEmail);
        txtcustmob=findViewById(R.id.custPMobile);
        txtcuststate=findViewById(R.id.custPSate);
        txtcustcity=findViewById(R.id.custPCity);
        txtcustadd=findViewById(R.id.custPAddress);

        custKey= getIntent().getStringExtra("custKey");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Customer userProfile = dataSnapshot.child(custKey).getValue(Customer.class);
                txtcustemail.setText(userProfile.getEmail());
                txtcustnm.setText(userProfile.getCustName());
                txtcustmob.setText(userProfile.getCustMobile());
                txtcuststate.setText(userProfile.getCustState());
                txtcustcity.setText(userProfile.getCustCity());
                txtcustadd.setText(userProfile.getCustAddress());
                String image = dataSnapshot.child(custKey).child("image").getValue().toString();
                Picasso.get().load(image).placeholder(R.drawable.defavatar).into(profilePicImageView);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}