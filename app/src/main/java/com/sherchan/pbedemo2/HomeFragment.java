package com.sherchan.pbedemo2;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.widget.ListAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

import android.content.Context;

import androidx.core.app.ActivityCompat;

import android.content.pm.PackageManager;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.internal.location.zzz;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
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
 //   private Context context;
 //   private ConnectDevice connDevice;

    FirebaseUser device;
    Button mFall, mBon, mBoff, mScan, mLocation;
    ListView lv;
    FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference;
    BluetoothAdapter bluetoothAdapter;
    BroadcastReceiver mBroadcastReceiver;
    ArrayList<String> stringArrayList = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    Dialog dialog;
    public LocationRequest locationRequest;

 /*   public final int LOCATION_PERMISSION_REQUEST = 101;
    private final int SELECT_DEVICE = 102;

    public static final int MESSAGE_STATE_CHANGED = 0;
    public static final int MESSAGE_READ = 1;
    public static final int MESSAGE_WRITE = 2;
    public static final int MESSAGE_DEVICE_NAME = 3;
    public static final int MESSAGE_TOAST = 4;

    public static final String DEVICE_NAME = "deviceName";
    public static final String TOAST = "toast";

    private String connectedDevice;
    public double lon;
    public double lat;

    TextView t1tv, t2tv; */

  /*  private Handler handler = new Handler(new Handler.Callback() {
       @Override
        public boolean handleMessage(@NonNull Message message) {
            switch (message.what) {
                case MESSAGE_STATE_CHANGED:
                    switch (message.arg1) {
                        case ConnectDevice.STATE_NONE:
                            setState("Not Connected");
                            break;
                        case ConnectDevice.STATE_LISTINING:
                            setState("Not Connected");
                            break;
                        case ConnectDevice.STATE_CONNECTING:
                            setState("Connecting...");

                            break;
                        case ConnectDevice.STATE_CONNECTED:
                            setState("Connected");
                            break;
                    }
                    break;
                case MESSAGE_READ:
                    break;
                case MESSAGE_WRITE:
                    break;
                case MESSAGE_DEVICE_NAME:
                    connectedDevice = message.getData().getString(DEVICE_NAME);
                    Toast.makeText(context, connectedDevice, Toast.LENGTH_SHORT).show();
                    break;
                case MESSAGE_TOAST:
                    Toast.makeText(context, message.getData().getString(TOAST), Toast.LENGTH_SHORT).show();
                    break;
            }
            return false;
        }
    });

    private void setState(CharSequence subtitle) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(subtitle);
    }

    int requestCodeForEnable;
    FusedLocationProviderClient fusedLocationProviderClient;
*/
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
       // mLocation = view.findViewById(R.id.connect);
        lv = view.findViewById(R.id.list);
      /*  t1tv = view.findViewById(R.id.t1);
        t2tv = view.findViewById(R.id.t2);

        fusedLocationProviderClient = LocationServices
                .getFusedLocationProviderClient(
                        getActivity().getApplicationContext()); */

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        int requestCodeForEnable = 1;

     //   connDevice = new ConnectDevice(context, handler);

     /*   mLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        getLocation();
                    }
                    else {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                    }
            }
        });
*/

        mFall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fallStatus = "Fall Detected";
                String uid = mAuth.getUid();
                String deviceId = reference.push().getKey();
                //Get current time
                String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

                //Get current Date
                String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

                Device devices = new Device(deviceId, fallStatus, currentTime, currentDate, uid);

                //put data with hashmap in database
                reference.child(deviceId).setValue(devices);

                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle("Fall Detected");
                alertDialog.setMessage("Do You want to sms your emergency contact?");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                    String txtMessage = "The following number holder took a hard fall and has asked for help";
                    String txtMobile = "8143158420";
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (getActivity().checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                                try {
                                    SmsManager smgr = SmsManager.getDefault();
                                    smgr.sendTextMessage(txtMobile, null, txtMessage, null, null);
                                    Toast.makeText(getActivity(), "SMS Sent Successfully", Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    Toast.makeText(getActivity(), "SMS Failed to Send, Please try again", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 1);
                            }
                        }
                        dialog.dismiss();
                    }

                });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Toast.makeText(getActivity(), "Happy to hear your Good", Toast.LENGTH_SHORT).show();

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
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.BLUETOOTH_SCAN}, 3);
                }
                //   bluetoothAdapter.startDiscovery();
                Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
                ArrayList list = new ArrayList();
                if (pairedDevices.size() > 0) {
                    for (BluetoothDevice device : pairedDevices) {
                        String devicename = device.getName();
                        String macAddress = device.getAddress();
                        list.add("Name: " + devicename + "MAC Address: " + macAddress);
                    }
                    lv = view.findViewById(R.id.list);
                    Adapter aAdapter = new ArrayAdapter(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, list);
                    lv.setAdapter((ListAdapter) aAdapter);
                }

            }
        });
        return view;
    }
  /*  @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        String address = data.getStringExtra("deviceAddress");
        connDevice.connect(bluetoothAdapter.getRemoteDevice(address));
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(connDevice!=null) {
            connDevice.stop();
        }
    }
   private void getLocation(){
       if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
           ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
       }
       fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
           @Override
           public void onComplete(@NonNull Task<Location> task) {
               Location location = task.getResult();
               if (location != null) {
                   Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());

                   try {
                       List<Address> addresses = geocoder.getFromLocation(
                               location.getLatitude(), location.getLongitude(), 1);
                       t1tv.setText(Html.fromHtml("Latitude: "+ addresses.get(0).getLatitude()));
                       t2tv.setText(Html.fromHtml("Longitude: "+ addresses.get(0).getLongitude()));
                   } catch (IOException e) {
                       e.printStackTrace();
                   }
               }
           }
       });
   }*/
}

