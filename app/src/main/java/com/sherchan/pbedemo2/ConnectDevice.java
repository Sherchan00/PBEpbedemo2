package com.sherchan.pbedemo2;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class ConnectDevice {

  /*  private Context context;
    private Handler handler;

    private BluetoothAdapter bluetoothAdapter;

    private ConnectThread connectThread;
    private AcceptThread acceptThread;

    private final UUID APP_UUID = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
    private final String APP_NAME = "PBE_DEVICE";

    public static final int STATE_NONE = 0;
    public static final int STATE_LISTINING = 1;
    public static final int STATE_CONNECTING = 2;
    public static final int STATE_CONNECTED = 3;

    private int state;

    public ConnectDevice(Context context, Handler handler) {
        this.context = context;
        this.handler = handler;

        state = STATE_NONE;
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public int getState() {
        return state;
    }

    public synchronized void setState(int state) {
        this.state = state;
        handler.obtainMessage(HomeFragment.MESSAGE_STATE_CHANGED, state, -1).sendToTarget();
    }

    public synchronized void start() {
        if (connectThread != null) {
            connectThread.cancle();
            connectThread = null;
        }

        if (acceptThread == null) {
            acceptThread = new AcceptThread();
            acceptThread.start();
        }

        setState(STATE_LISTINING);
    }

    public synchronized void stop() {
        if (connectThread != null) {
            connectThread.cancle();
            connectThread = null;
        }

        if (acceptThread == null) {
           // acceptThread.cancle();
            acceptThread = null;
        }

        setState(STATE_NONE);
    }

    public void connect(BluetoothDevice device) {
        if (state == STATE_CONNECTED) {
            connectThread.cancle();
            connectThread = null;
        }
        connectThread = new ConnectThread(device);
        connectThread.start();

        setState(STATE_CONNECTING);
    }

    public class AcceptThread extends Thread {

        private BluetoothServerSocket serverSocket;

        public AcceptThread() {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 3); }
            BluetoothServerSocket tmp = null;
            try {
                tmp = bluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(APP_NAME, APP_UUID);
            } catch (IOException e) {
                Log.e("Accept->Constructor", e.toString());
            }
            serverSocket = tmp;
        }

        public void run() {
            BluetoothSocket socket = null;
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                Log.e("Accept->Run", e.toString());
                try {
                    serverSocket.close();
                } catch (IOException e1) {
                    Log.e("Accept->Close", e.toString());

                }
            }
            if (socket != null) {
                switch (state) {
                    case STATE_LISTINING:
                    case STATE_CONNECTING:
                        connect(socket.getRemoteDevice());
                        break;
                    case STATE_NONE:
                    case STATE_CONNECTED:
                        try {
                            socket.close();
                        } catch (IOException e) {
                            Log.e("Accept->CloseSocket", e.toString());

                        }
                        break;
                }
            }
        }

        public void cancle() {
            try {
                serverSocket.close();
            } catch (IOException e) {
                Log.e("Accept->Constructor", e.toString());

            }
        }
    }

    private class ConnectThread extends Thread {
        private final BluetoothSocket socket;
        private final BluetoothDevice device;

        public ConnectThread(BluetoothDevice device) {
            this.device = device;

            BluetoothSocket tmp = null;
            try {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 3); }
                tmp = device.createRfcommSocketToServiceRecord(APP_UUID);
            } catch (IOException e) {
                Log.e("Connect->Constructor", e.toString());
            }
            socket = tmp;
        }

        public void run() {
            try {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 3); }
                socket.connect();
            } catch (IOException e) {
                Log.e("Connect->Run", e.toString());
                try {
                    socket.close();
                } catch (IOException e1) {
                    Log.e("Connect->CloseSocket", e.toString());

                }
            }

            synchronized (ConnectDevice.this) {
                connectThread = null;
            }

            connect(device);
        }

        public void cancle() {
            try {
                socket.close();
            }catch (IOException e) {
                Log.e("Conncet->Cancle", e.toString());
            }
        }
    }

    private synchronized void connectionFailed() {
        Message message = handler.obtainMessage(HomeFragment.MESSAGE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString(HomeFragment.TOAST, "can't connect to this device");
        message.setData(bundle);
        handler.sendMessage(message);

        ConnectDevice.this.start();
    }

    private synchronized void connected(BluetoothDevice device) {
if(connectThread!= null) {
    connectThread.cancle();
    connectThread = null;
}
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 3); }
Message message = handler.obtainMessage(HomeFragment.MESSAGE_DEVICE_NAME);
Bundle bundle = new Bundle();
bundle.putString(HomeFragment.DEVICE_NAME, device.getName());
message.setData(bundle);
handler.sendMessage(message);

setState(STATE_CONNECTED);
    } */
}
