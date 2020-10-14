package com.android.watersprinkle;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class DeliverySelectCustomer extends AppCompatActivity implements RecycleItemClickListener {

    private FirebaseAuth firebaseAuth;
    FirebaseUser user;
    private RecyclerView recyclerView;
    private CustRecycleAdapter adapter;
    private List<Customer> artistList;
    private DatabaseReference mFirebaseDatabase;
   // private String formattedDate;

//   LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_selectcustomer);

     //   linearLayout = findViewById(R.id.chklayout);

/*        final LayoutInflater factory = getLayoutInflater();
        chkview = factory.inflate(R.layout.customer_single_layout, null);
        chk = (CheckBox)chkview.findViewById(R.id.chkcust);
        chk.setText("heey");
  */

     //   getSupportActionBar().setTitle("All Users");
      //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);

     //   getSupportActionBar().setDisplayUseLogoEnabled(true);
      ///  getSupportActionBar().setHomeAsUpIndicator(R.drawable.logo);// set drawable icon

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        if (user == null) {
            startActivity(new Intent(getApplicationContext(), SignIn.class));
        }

        recyclerView = findViewById(R.id.recycler1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        artistList = new ArrayList<>();
        adapter = new CustRecycleAdapter(this, artistList);
        recyclerView.setAdapter(adapter);
        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference();

        Query query = FirebaseDatabase.getInstance().getReference("Customers")
                .orderByChild("dealerID")
                .equalTo(user.getUid());

        query.addListenerForSingleValueEvent(valueEventListener);

        adapter.setClickListener(this);

    }
    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            artistList.clear();
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    final Customer artist = snapshot.getValue(Customer.class);
                    final String key= snapshot.getKey();
                     artist.setUid(key);

                  /*  DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Orders").child(user.getUid()).child(key);
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot1) {
                            arrayDateList.clear();
                            for(DataSnapshot datas: dataSnapshot1.getChildren()){
                               // String getdate=datas.child("date").getValue().toString();
                                //Log.e("Date : ",getdate + " Key :"+key+ "formate" +formattedDate);

                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });*/
                artistList.add(artist);
              }
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    };

    @Override
    public void onClick(View view, int position) {

        final Customer city = artistList.get(position);
        Intent i = new Intent(this,DeliveryActivity.class);
        i.putExtra("cust_uid", city.getUid());
      //  Log.e("hello",city.getUid());
        startActivity(i);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent goToMainActivity = new Intent(getApplicationContext(), MainActivity.class);
        goToMainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(goToMainActivity);
    }
}