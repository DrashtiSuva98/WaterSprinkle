package com.android.watersprinkle;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DeliveryActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    private TextView date;
    private Spinner type,price,ltr;
    private EditText qty;
    private String[] spType ={"Sealed pack water","Mineral water bottle","Alkaline water bottle","Distilled water bottle","Purified water bottle","Flavored water bottle"};
    private String[] spPrice={"15","20","25","30","35","40","45","50"};
    private String[] spltr={"10L","15L","20L","25L","20L"};
    private String selType,selPrice,cust_uid,selLtr;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseUser currentFirebaseUser;
    String finaldate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);

        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int heigth= dm.heightPixels;
        getWindow().setLayout((int)(width*.9),(int)(heigth*.5));
        WindowManager.LayoutParams params =getWindow().getAttributes();
        params.gravity= Gravity.CENTER;
        params.x=0;
        params.y=-20;
        getWindow().setAttributes(params);


        date=findViewById(R.id.cuntdate);
        type=findViewById(R.id.Btype);
        price=findViewById(R.id.Bprice);
        qty=findViewById(R.id.qty);
        ltr=findViewById(R.id.ltr);

          cust_uid = getIntent().getStringExtra("cust_uid");
        currentFirebaseUser= FirebaseAuth.getInstance().getCurrentUser() ;
        if (currentFirebaseUser == null) {
            finish();
            startActivity(new Intent(getApplicationContext(), SignIn.class));
        }

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MMM-dd", Locale.getDefault());
        finaldate = df1.format(c);
        date.setText(formattedDate);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spType);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(dataAdapter);
        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selType=adapterView.getItemAtPosition(i).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, spPrice);
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        price.setAdapter(dataAdapter1);
        price.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selPrice=adapterView.getItemAtPosition(i).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}});
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, spltr);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ltr.setAdapter(dataAdapter2);
        ltr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selLtr=adapterView.getItemAtPosition(i).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}});

    }

    public void insertDelivery(View view) {
        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference();
        String q =qty.getText().toString();
        int Total = Integer.parseInt(selPrice) * Integer.parseInt(q);
        final Customer cust=new Customer(finaldate,selType,selLtr,Integer.parseInt(selPrice),Integer.parseInt(q),Total);
        progressDialog = new ProgressDialog(DeliveryActivity.this);
        progressDialog.setMessage("Please wait while we record your delivery.."); // Setting Message
        progressDialog.setTitle("Recording.."); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
        new Thread(new Runnable() {
            public void run() {
                try {
                  //  mFirebaseDatabase.child("Orders").child(currentFirebaseUser.getUid()).child(cust_uid).setValue(cust);
                    mFirebaseDatabase.child("Orders").child(currentFirebaseUser.getUid()).child(cust_uid).push().setValue(cust);
                    Thread.sleep(700);
                    startActivity(new Intent(DeliveryActivity.this,DeliverySelectCustomer.class));
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }
        }).start();
    }
}