package com.android.watersprinkle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AllCustomerActivity extends AppCompatActivity implements RecycleItemClickListener {

    private RecyclerView recyclerView;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private AllCustomerRecycleAdapter adapter;
    private List<Customer> artistList;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_customer);

   //     getSupportActionBar().setTitle("All Users");
    //    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        if (user == null) {
            startActivity(new Intent(getApplicationContext(), SignIn.class));
        }
        recyclerView = findViewById(R.id.recycler2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        artistList = new ArrayList<>();
        adapter = new AllCustomerRecycleAdapter(this, artistList);
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
        Intent i = new Intent(this,PickMonthYear.class);
        i.putExtra("cust_uid", city.getUid());
        startActivity(i);

    }
}