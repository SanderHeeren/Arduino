package com.example.arduino;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    static final UUID mUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void zetAan(View view) {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        System.out.println(bluetoothAdapter.getBondedDevices());

        BluetoothDevice hc05Device = bluetoothAdapter.getRemoteDevice("98:D3:51:FD:AE:B5");
        System.out.println(hc05Device.getName());

        int count = 0;
        BluetoothSocket bluetoothSocket = null;
        do {
            try {
                bluetoothSocket = hc05Device.createRfcommSocketToServiceRecord(mUUID);
                System.out.println(bluetoothSocket);
                bluetoothSocket.connect();
                System.out.println(bluetoothSocket.isConnected());
            } catch (IOException e) {
                e.printStackTrace();
            }
            count++;
        } while (!bluetoothSocket.isConnected() && count < 5);

        try {
            OutputStream output = bluetoothSocket.getOutputStream();
            output.write(1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        InputStream input = null;
        try {
            input = bluetoothSocket.getInputStream();
            input.skip(input.available());

            for (int i = 0; i < 30; i++) {
                byte b = (byte) input.read();
                System.out.println((char) b);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            bluetoothSocket.close();
            System.out.println(bluetoothSocket.isConnected());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}