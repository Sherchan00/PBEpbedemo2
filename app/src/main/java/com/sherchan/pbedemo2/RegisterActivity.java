package com.sherchan.pbedemo2;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    //Views
    EditText mEmailEt, mPasswordEt, mNameEt, mPhoneEt, mEmergencyEt;
    Button mRegisterBtn;
    TextView mHaveAccountTv;

    //progressbar to display while register user
    ProgressDialog progressDialog;

    //instance of FirebaseAuth
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Actionbar and its title
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Create Account");

        //enable back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        //init
        mEmailEt = findViewById(R.id.emailEt);
        mPasswordEt = findViewById(R.id.passwordEt);
        mRegisterBtn = findViewById(R.id.registerBtn);
        mHaveAccountTv = findViewById(R.id.have_accountTv);
        mNameEt = findViewById(R.id.nameEt);
        mEmergencyEt = findViewById(R.id.emergencyEt);
        mPhoneEt = findViewById(R.id.phoneEt);

        //Init Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering User...");




        //handle register btn click
        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mNameEt.getText().toString().trim();
                String email = mEmailEt.getText().toString().trim();
                String password = mPasswordEt.getText().toString().trim();
                String phone = mPhoneEt.getText().toString().trim();
                String emergency = mEmergencyEt.getText().toString().trim();

                //validate
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    mEmailEt.setError("Invalid EMail");
                    mEmailEt.setFocusable(true);
                }

                else if(password.length()<6){
                    mPasswordEt.setError("Password length less than 6");
                    mPasswordEt.setFocusable(true);
                }
                else if(name.isEmpty()){
                    mNameEt.setError("TextField is required");
                    mNameEt.setFocusable(true);

                }
                else if(phone.isEmpty() || phone.length()<10){
                    mPhoneEt.setError("Invalid TextField");
                    mPhoneEt.setFocusable(true);

                }
                else if(emergency.isEmpty() || emergency.length()<10){
                    mEmergencyEt.setError("Invalid TextField");
                    mEmergencyEt.setFocusable(true);

                }
                else {
                    registerUser(name, email, password, phone, emergency);
                }

            }
        });

        //handle login textview click listner
        mHaveAccountTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }

    private void registerUser(String name, String email, String password, String phone, String emergency){
        //email and password pattern is valid, show progress dialog and start registering user
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            progressDialog.dismiss();
                            FirebaseUser user = mAuth.getCurrentUser();

                            String email = user.getEmail();
                            String uid = user.getUid();
                            String name = mNameEt.getText().toString();
                            String phone = mPhoneEt.getText().toString();
                            String emergency = mEmergencyEt.getText().toString();

                            //using HashMap
                            HashMap<Object, String> hashMap = new HashMap<>();

                            //put info in hashmap
                            hashMap.put("name", name);
                            hashMap.put("email", email);
                            hashMap.put("uid", uid);
                            hashMap.put("phone", phone);
                            hashMap.put("emergency", emergency);
                            hashMap.put("image", ""); //add later

                            //firebase database instance
                            FirebaseDatabase database = FirebaseDatabase.getInstance();

                            //path to store user data named "Users"
                            DatabaseReference reference = database.getReference("Users");

                            //put data with hashmap in database
                            reference.child(uid).setValue(hashMap);

                            Toast.makeText(RegisterActivity.this, "Registered..\n"+user.getEmail(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, DashBoardActivity.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(RegisterActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();   //goto previous activity
        return super.onSupportNavigateUp();
    }
}