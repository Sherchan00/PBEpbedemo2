package com.sherchan.pbedemo2;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.ListAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Set;

import android.content.Context;

import androidx.core.app.ActivityCompat;

import android.content.pm.PackageManager;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;

public class HomeFragment extends Fragment {

    FirebaseUser device;
    Button mFall, mBon, mBoff, mScan, mConnect;
    ListView lv;
    FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference;
    BluetoothAdapter bluetoothAdapter;
    BroadcastReceiver mBroadcastReceiver;
    ArrayList<String> stringArrayList = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    Dialog dialog;
    private LocationRequest locationRequest;

    int requestCodeForEnable;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //path to store user data named "Users"
        reference = database.getReference("Device");
        mAuth = FirebaseAuth.getInstance();

        mFall = view.findViewById(R.id.mFall);
        mBon = view.findViewById(R.id.bOn);
        mBoff = view.findViewById(R.id.bOff);
        mScan = view.findViewById(R.id.pairedDevices);
        mConnect = view.findViewById(R.id.connect);
        lv = view.findViewById(R.id.list);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        requestCodeForEnable = 1;

        mFall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fallStatus = "Fall Detected";
                String deviceId = reference.push().getKey();
                String uid = mAuth.getUid();

              /*  locationRequest = LocationRequest.create();
                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                locationRequest.setInterval(5000);
                locationRequest.setFastestInterval(2000); */
                //Get current time
                String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

                //Get current Date
                String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

                Device devices = new Device(deviceId, fallStatus, currentTime, currentDate, uid);

             /*   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 3);

                    }
                    if(isGPSEnabled()) {

                    } else  {
                        turnOnGPS();
                    }
                } */
                //put data with hashmap in database
                reference.child(deviceId).setValue(devices);

                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle("Fall Detected");
                alertDialog.setMessage("Do You want to sms your emergency contact?");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                    String txtMessage = "The following number took a hard fall and ask for help";
                    String txtMobile = "8143158420";

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            SmsManager smgr = SmsManager.getDefault();
                            smgr.sendTextMessage(txtMobile, null, txtMessage, null, null);
                            Toast.makeText(getActivity(), "SMS Sent Successfully", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(getActivity(), "SMS Failed to Send, Please try again", Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        });

        mBon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bluetoothAdapter == null) {
                    Toast.makeText(getActivity().getApplicationContext(), "Bluetooth Not Supported", Toast.LENGTH_SHORT).show();
                } else {
                    if (!bluetoothAdapter.isEnabled()) {
                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableBtIntent, requestCodeForEnable);
                        Toast.makeText(getActivity().getApplicationContext(), "Bluetooth Turned ON", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });

        mBoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 3);
                }
                if (bluetoothAdapter.isEnabled()) {
                    bluetoothAdapter.disable();
                    Toast.makeText(getActivity().getApplicationContext(), "Bluetooth Turned OFF", Toast.LENGTH_SHORT).show();
                }

            }
        });

        mScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                       ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.BLUETOOTH_SCAN}, 3); }
             //   bluetoothAdapter.startDiscovery();
                Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
                ArrayList list = new ArrayList();
                if(pairedDevices.size()>0){
                    for(BluetoothDevice device: pairedDevices){
                        String devicename = device.getName();
                        String macAddress = device.getAddress();
                        list.add("Name: "+devicename+"MAC Address: "+macAddress);
                    }
                    lv = view.findViewById(R.id.list);
                    Adapter aAdapter = new ArrayAdapter(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, list);
                    lv.setAdapter((ListAdapter) aAdapter);
                }

            }
        });

        mConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

      //  IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
     //   getActivity().registerReceiver(myReceiver, intentFilter);

     //   arrayAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1,stringArrayList);
     //   lv.setAdapter(arrayAdapter);

        return view;
    }
  /*  BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 3); }
            String action = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                stringArrayList.add(device.getName());
                arrayAdapter.notifyDataSetChanged();
            }
        }
    }; */

  /*  private void turnOnGPS() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getActivity().getApplicationContext())
                .checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {

                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    Toast.makeText(getActivity(), "GPS is already tured on", Toast.LENGTH_SHORT).show();

                } catch (ApiException e) {

                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult(getActivity(), 2);
                            } catch (IntentSender.SendIntentException ex) {
                                ex.printStackTrace();
                            }
                            break;

                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            //Device does not have location
                            break;
                    }
                }
            }
        });
    } */

  /*  private boolean isGPSEnabled() {
        LocationManager locationManager = null;
        boolean isEnabled = false;

        if(locationManager == null) {
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        }
        isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
   return isEnabled;
    } */
}

