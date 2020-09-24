package com.android.watersprinkle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private GridLayout maingrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        maingrid=findViewById(R.id.grid);
        ImageView imageView = findViewById(R.id.imageView);
        Glide.with(this).load(R.drawable.delivery).into(imageView);
    setSingleEvent(maingrid);

/*
    for push notifications

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel ch =new NotificationChannel("water","sprinle", NotificationManager.IMPORTANCE_DEFAULT);
            ch.setDescription("heya");
            NotificationManager nm=getSystemService(NotificationManager.class);
            nm.createNotificationChannel(ch);

            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if (!task.isSuccessful()) {
                                Log.e("main", "getInstanceId failed", task.getException());
                                return;
                            }
                            // Get new Instance ID token
                            String token = task.getResult().getToken();

                            // Log and toast
                            //  String msg = getString(R.string.msg_token_fmt, token);
                            Log.e("token",token);
                            Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
                        }
                    });
        }
*/


        auth = FirebaseAuth.getInstance();
       authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    startActivity(new Intent(MainActivity.this, SignIn.class));
                    finish();
                }
            }
        };


    }
    private void setSingleEvent(GridLayout mainGrid) {
        for (int i = 0; i < mainGrid.getChildCount(); i++) {
            CardView cardView = (CardView) mainGrid.getChildAt(i);
            final int finalI = i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(finalI==0) {
                        Intent intent = new Intent(MainActivity.this, FriendRequestActivity.class);
                        startActivity(intent);
                    }else if(finalI==1){
                        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                        startActivity(intent);
                    }else if(finalI==2){
                        Intent intent = new Intent(MainActivity.this, AllCustomerActivity.class);
                        startActivity(intent);
                    }else if(finalI==3){
                        Intent intent = new Intent(MainActivity.this, DeliverySelectCustomer.class);
                        startActivity(intent);
                    }
                }
            });
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }


    @Override
    public void onBackPressed() {

       /* Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
       if (!(fragment instanceof IOnBackPressed) || !((IOnBackPressed) fragment).onBackPressed()) {
            super.onBackPressed();
        }*/

    /*    int fragments= getSupportFragmentManager().getBackStackEntryCount();
        if (fragments == 1) {
            new AlertDialog.Builder(this)
                    .setMessage("Are you sure you want to exit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        } else {
                super.onBackPressed();
            }
*/
        }

    }