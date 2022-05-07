package com.sherchan.pbedemo2;

import android.app.Activity;
import android.content.Context;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ProfileFragment extends Fragment {

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;

    //views from xml
    ImageView avatarIv;
    TextView nameTv,fullnameTv, emailTv, phoneTv, emergencyTv;
    Button historyTv, mUpdate;

    String name, fullname;

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
        fullnameTv = view.findViewById(R.id.fullnameTv);
        emailTv = view.findViewById(R.id.emailTv);
        nameTv = view.findViewById(R.id.nameTv);
        phoneTv = view.findViewById(R.id.phoneTv);
        emergencyTv = view.findViewById(R.id.emergencyTv);
        mUpdate = view.findViewById(R.id.update);


        Query query = databaseReference.orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //check until required data get
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    //get data
                    name = "" + ds.child("name").getValue();
                    fullname = "" + ds.child("full name").getValue();
                    String email = "" + ds.child("email").getValue();
                    String phone = "" + ds.child("phone").getValue();
                    String emergency = "" + ds.child("emergency").getValue();

                    //set data
                    nameTv.setText(name);
                    fullnameTv.setText(name);
                    emailTv.setText(email);
                    phoneTv.setText(phone);
                    emergencyTv.setText(emergency);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        avatarIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"add profile pic", Toast.LENGTH_SHORT).show();
            }
        });

        mUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fullnameTv.getText().toString().isEmpty() || emailTv.getText().toString().isEmpty() || phoneTv.getText().toString().isEmpty()
                || emergencyTv.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(),"add profile pic", Toast.LENGTH_SHORT).show();
                    return;
                }

                String email = emailTv.getText().toString();
                user.updateEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        databaseReference.child(user.getUid());
                        Map<String, Object> edited = new HashMap<>();
                        edited.put("email",email);
                        edited.put("full name",fullnameTv.getText().toString());
                        edited.put("phone",phoneTv.getText().toString());
                        edited.put("emergency",emergencyTv.getText().toString());
                        databaseReference.updateChildren(edited);
                        Toast.makeText(getActivity(),"Profile Updated", Toast.LENGTH_SHORT).show();
                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(),e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });

        return view;
    }

}