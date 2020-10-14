package com.android.watersprinkle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

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

public class CustReportActivity extends AppCompatActivity implements RecycleItemClickListener{

    private String cust_uid,month,year,submonth,finalDate;
    private FirebaseAuth firebaseAuth;
    FirebaseUser user;
    private ReportAdapterRecycle adapter;
    private List<Customer> artistList;
    private RecyclerView recyclerView;
    TextView total;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cust_report);

        cust_uid = getIntent().getStringExtra("cust_uid");
        month = getIntent().getStringExtra("month");
        year = getIntent().getStringExtra("year");
        total=findViewById(R.id.rpttotal);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        if (user == null) {
            startActivity(new Intent(getApplicationContext(), SignIn.class));
        }

        submonth = month.substring(0,3);
        finalDate = year+ "-" + submonth;
    //   Log.e("Data",finalDate);
      //  FirebaseDatabase.getInstance().getReference("Orders").child(user.getUid()).child(cust_uid);
        recyclerView = findViewById(R.id.recycler3);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        artistList = new ArrayList<>();
        adapter = new ReportAdapterRecycle(this, artistList);
        recyclerView.setAdapter(adapter);

        //finalDate = 2020-Sep
        Query query = FirebaseDatabase.getInstance().getReference("Orders").child(user.getUid()).child(cust_uid)
                .orderByChild("date")
                .startAt(finalDate)
                .endAt(finalDate+"\uf8ff");
        query.addListenerForSingleValueEvent(valueEventListener);
        adapter.setClickListener(this);
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            artistList.clear();
            if (dataSnapshot.exists()) {
                float amt=0;
                for (DataSnapshot datas : dataSnapshot.getChildren()) {
                    final Customer artist = datas.getValue(Customer.class);

                    float total= datas.child("total").getValue(Float.class);
                          amt = amt + total;

                 /*   String getdate=datas.child("date").getValue().toString();
                    String type=datas.child("bottleType").getValue().toString();
                    String price=datas.child("bottlePrice").getValue().toString();
                    String qty=datas.child("quantity").getValue().toString();
                    String total=datas.child("total").getValue().toString();

                    Log.e("Data : ",getdate +" " + type+" " +price+" " +qty+" " +total);
*/

                            /*   DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Orders").child(user.getUid()).child(cust_uid);
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot1) {
                            for(DataSnapshot datas: dataSnapshot1.getChildren()){
                               String getdate=datas.child("date").getValue().toString();
                                String type=datas.child("bottleType").getValue().toString();
                                String price=datas.child("bottlePrice").getValue().toString();
                                String qty=datas.child("quantity").getValue().toString();
                                String total=datas.child("total").getValue().toString();

                                Log.e("Data : ",getdate +" " + type+" " +price+" " +qty+" " +total);

                            }

                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

*/                 artistList.add(artist);
                }total.setText(Float.toString(amt));
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    };
    @Override
    public void onClick(View view, int position) {

    }
}