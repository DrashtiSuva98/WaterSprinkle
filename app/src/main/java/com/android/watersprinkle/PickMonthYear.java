package com.android.watersprinkle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PickMonthYear extends AppCompatActivity {

    private Spinner month,year;
    private String[] spMonth ={"January","Februay","March","April","May","June","July","August","September","October","November","December"};
    private String[] spYear={"2019","2020","2021","2022"};
    private String selMonth,selYear,cust_uid;
    private FirebaseUser currentFirebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_month_year);


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

        month=findViewById(R.id.month);
        year=findViewById(R.id.year);

        cust_uid = getIntent().getStringExtra("cust_uid");
        currentFirebaseUser= FirebaseAuth.getInstance().getCurrentUser() ;
        if (currentFirebaseUser == null) {
            finish();
            startActivity(new Intent(getApplicationContext(), SignIn.class));
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spMonth);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        month.setAdapter(dataAdapter);
        month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selMonth=adapterView.getItemAtPosition(i).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, spYear);
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        year.setAdapter(dataAdapter1);
        year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selYear=adapterView.getItemAtPosition(i).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}});
    }

    public void Report(View view) {

        Intent i = new Intent(this,CustReportActivity.class);
        i.putExtra("cust_uid",cust_uid);
        i.putExtra("month",selMonth);
        i.putExtra("year",selYear);
        startActivity(i);

    }
}
