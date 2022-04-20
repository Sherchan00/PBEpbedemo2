package com.sherchan.pbedemo2;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;


public class FallHistory extends Fragment {

    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;

    TextView timeTv, fallStatusTv, dateTv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

      //  databaseReference = firebaseDatabase.getReference("Device");

        View v = inflater.inflate(R.layout.fragment_fall_history, container, false);

      /*  fallStatusTv = v.findViewById(R.id.fallStatusTv);
        timeTv = v.findViewById(R.id.timeTv);
        dateTv = v.findViewById(R.id.dateTv);

        Query q = databaseReference.child(mAuth.getCurrentUser().getUid());
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //check until required data get
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    //get data
                    String fallStatus = "" + ds.child("fallStatus").getValue();
                    String currentTime = "" + ds.child("time").getValue();
                    String currentDate = "" + ds.child("date").getValue();

                    //set data
                    fallStatusTv.setText(fallStatus);
                    timeTv.setText(currentTime);
                    dateTv.setText(currentDate);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        }); */

        // Inflate the layout for this fragment
        return v;
    }
}