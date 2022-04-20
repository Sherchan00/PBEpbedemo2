package com.sherchan.pbedemo2;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;

    //views from xml
    ImageView avatarIv;
    TextView nameTv, emailTv, phoneTv, emergencyTv;
    Button historyTv;

    ActionBar actionBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");

        //init views
        avatarIv = view.findViewById(R.id.avatarIv);
        emailTv = view.findViewById(R.id.emailTv);
        nameTv = view.findViewById(R.id.nameTv);
        phoneTv = view.findViewById(R.id.phoneTv);
        emergencyTv = view.findViewById(R.id.emergencyTv);
        historyTv = view.findViewById(R.id.historyTv);

        Query query = databaseReference.orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //check until required data get
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    //get data
                    String name = "" + ds.child("name").getValue();
                    String email = "" + ds.child("email").getValue();
                    String phone = "" + ds.child("phone").getValue();
                    String emergency = "" + ds.child("emergency").getValue();
                   // String history = "" + ds.child("history").getValue();

                    //set data
                    nameTv.setText(name);
                    emailTv.setText(email);
                    phoneTv.setText(phone);
                    emergencyTv.setText(emergency);
                    //historyTv.setText(history);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        historyTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionBar.setTitle("Fall History");
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.p, FallHistory.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack("") // name can be null
                        .commit();
                // FallHistory fragment1 =new FallHistory();
               // FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
               // ft.replace(R.id.p, fragment1, "").commit();
            }
        });

        return view;
    }
}