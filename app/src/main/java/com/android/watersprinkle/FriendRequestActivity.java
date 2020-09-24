package com.android.watersprinkle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FriendRequestActivity extends AppCompatActivity {

    private DatabaseReference mRootRef;
    private DatabaseReference mFriendReqDatabase;
    private FirebaseUser mCurrent_user;
    private GridLayout maingrid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_request);

        maingrid=findViewById(R.id.grid);
        ImageView imageView = findViewById(R.id.requestgif);
        Glide.with(this).load(R.drawable.requestgif).into(imageView);

        final LinearLayout lm = findViewById(R.id.btnLayout);
        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mFriendReqDatabase = FirebaseDatabase.getInstance().getReference().child("Request");
        mCurrent_user = FirebaseAuth.getInstance().getCurrentUser();
        mRootRef = FirebaseDatabase.getInstance().getReference();


        mFriendReqDatabase.child(mCurrent_user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.hasChildren()){

                    for(DataSnapshot ds : dataSnapshot.getChildren()) {
                        LinearLayout ll = new LinearLayout(getApplicationContext());
                        ll.setOrientation(LinearLayout.HORIZONTAL);
                        ll.setPadding(10,0,0,0);
                        params.setMargins(30,30,0,0);

                        final String key = ds.getKey();
                        String request_type = ds.child("request_type").getValue(String.class);

                        final Button accept = new Button(getApplicationContext());
                        final Button delete = new Button(getApplicationContext());
                        final ImageView img = new ImageView(getApplicationContext());

                        String uri = "@drawable/defavatar";  // where myresource (without the extension) is the file
                        int imageResource = getResources().getIdentifier(uri, null,getApplicationContext().getPackageName());
                        Drawable res = getResources().getDrawable(imageResource);
                        img.setImageDrawable(res);
                        LinearLayout.LayoutParams imgparam = new LinearLayout.LayoutParams(200, 200);
                        img.setLayoutParams(imgparam);
                        img.setScaleType(ImageView.ScaleType.FIT_XY);

                        accept.setText("Accept");
                        accept.setTextColor(getApplicationContext().getResources().getColor(R.color.colorNavText));
                        accept.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.colorPrimary));
                        LinearLayout.LayoutParams acceptparam = new LinearLayout.LayoutParams(350, 110);
                        acceptparam.setMargins(20,35,0,0);
                        accept.setLayoutParams(acceptparam);

                        delete.setText("Delete");
                        delete.setTextColor(getApplicationContext().getResources().getColor(R.color.colorNavText));
                        delete.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.colorPrimary));
                        LinearLayout.LayoutParams deleteparam = new LinearLayout.LayoutParams(350, 110);
                        deleteparam.setMargins(15,35,0,0);
                        delete.setLayoutParams(deleteparam);

                        accept.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                final String currentDate = DateFormat.getDateTimeInstance().format(new Date());

                                Map friendsMap = new HashMap();
                                friendsMap.put("Request/" + mCurrent_user.getUid() + "/" + key, null);
                                friendsMap.put("Request/" + key + "/" + mCurrent_user.getUid(), null);

                                friendsMap.put("Customers/"+key+"/"+"dealerID",mCurrent_user.getUid());
                                friendsMap.put("Customers/"+ key + "/" + "timestamp",currentDate);

                                mRootRef.updateChildren(friendsMap, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                        if(databaseError == null){
                                            img.setVisibility(View.INVISIBLE);
                                            img.setEnabled(false);
                                            accept.setVisibility(View.INVISIBLE);
                                            accept.setEnabled(false);
                                            delete.setVisibility(View.INVISIBLE);
                                            delete.setEnabled(false);
                                        } else {
                                            String error = databaseError.getMessage();
                                            Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                            }
                        });

                        delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Map friendsMap = new HashMap();
                                friendsMap.put("Request/" + mCurrent_user.getUid() + "/" + key, null);
                                friendsMap.put("Request/" + key + "/" + mCurrent_user.getUid(), null);

                                mRootRef.updateChildren(friendsMap, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                        if(databaseError == null){
                                            img.setVisibility(View.INVISIBLE);
                                            img.setEnabled(false);
                                            accept.setVisibility(View.INVISIBLE);
                                            accept.setEnabled(false);
                                            delete.setVisibility(View.INVISIBLE);
                                            delete.setEnabled(false);
                                        } else {
                                            String error = databaseError.getMessage();
                                            Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        });
                        ll.addView(img);
                        ll.addView(accept);
                        ll.addView(delete);
                        ll.setLayoutParams(params);
                        lm.addView(ll);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}