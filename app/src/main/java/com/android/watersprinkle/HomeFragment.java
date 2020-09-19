package com.android.watersprinkle;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
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

public class HomeFragment extends Fragment {

    private DatabaseReference mRootRef;
    private DatabaseReference mFriendReqDatabase;
    private FirebaseUser mCurrent_user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        final LinearLayout lm = rootView.findViewById(R.id.btnLayout);
       final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
               ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mFriendReqDatabase = FirebaseDatabase.getInstance().getReference().child("Request");
        mCurrent_user = FirebaseAuth.getInstance().getCurrentUser();
        mRootRef = FirebaseDatabase.getInstance().getReference();


        FloatingActionButton fab = rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mFriendReqDatabase.child(mCurrent_user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.hasChildren()){

                    for(DataSnapshot ds : dataSnapshot.getChildren()) {
                        LinearLayout ll = new LinearLayout(getContext());
                        ll.setOrientation(LinearLayout.HORIZONTAL);
                        ll.setPadding(10,0,0,0);
                        params.setMargins(30,30,0,0);

                        final String key = ds.getKey();
                        String request_type = ds.child("request_type").getValue(String.class);

                        final Button accept = new Button(getContext());
                        final Button delete = new Button(getContext());
                        final ImageView img = new ImageView(getContext());

                        String uri = "@drawable/defavatar";  // where myresource (without the extension) is the file
                        int imageResource = getResources().getIdentifier(uri, null,getActivity().getPackageName());
                        Drawable res = getResources().getDrawable(imageResource);
                        img.setImageDrawable(res);
                        LinearLayout.LayoutParams imgparam = new LinearLayout.LayoutParams(200, 200);
                        img.setLayoutParams(imgparam);
                        img.setScaleType(ImageView.ScaleType.FIT_XY);

                        accept.setText("Accept");
                        accept.setTextColor(getActivity().getResources().getColor(R.color.colorNavText));
                        accept.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimary));
                        LinearLayout.LayoutParams acceptparam = new LinearLayout.LayoutParams(350, 110);
                        acceptparam.setMargins(20,35,0,0);
                        accept.setLayoutParams(acceptparam);

                        delete.setText("Delete");
                        delete.setTextColor(getActivity().getResources().getColor(R.color.colorNavText));
                        delete.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimary));
                        LinearLayout.LayoutParams deleteparam = new LinearLayout.LayoutParams(350, 110);
                        deleteparam.setMargins(15,35,0,0);
                        delete.setLayoutParams(deleteparam);

                        accept.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                final String currentDate = DateFormat.getDateTimeInstance().format(new Date());

                                Map friendsMap = new HashMap();
                                friendsMap.put("Friends/" + mCurrent_user.getUid() + "/" + key + "/date", currentDate);
                                friendsMap.put("Friends/" + key + "/"  + mCurrent_user.getUid() + "/date", currentDate);

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
                                            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
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
                                            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
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


        return rootView;

    }




}
