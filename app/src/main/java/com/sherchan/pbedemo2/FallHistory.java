package com.sherchan.pbedemo2;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;
import java.util.ArrayList;

public class FallHistory extends Fragment {

    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;

    RecyclerView recyclerView;
    Adapter adapter;
    ArrayList<Device> fallDetail = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fall_history, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Device");
        FirebaseUser user = firebaseAuth.getCurrentUser();

        recyclerView = v.findViewById(R.id.FallHistory);
        recyclerView.setHasFixedSize(true);

        // Setting the layout as Linear for vertical orientation to have swipe behavior
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        fallDetail = new ArrayList<>();
        // Sending reference and data to Adapter
        adapter = new Adapter(getActivity(),fallDetail);
        recyclerView.setAdapter(adapter);

        // Setting Adapter to RecyclerView

       // Query query = databaseReference.orderByChild("fallStatus");
        databaseReference.orderByChild("uid").addValueEventListener(new ValueEventListener() {
        //databaseReference.orderByChild("uid").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds : snapshot.getChildren()) {
                    Device fallStatus = ds.getValue(Device.class);
                    Device device = ds.getValue(Device.class);
                    fallDetail.add(fallStatus);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        // Inflate the layout for this fragment
        return v;
    }
}