package org.as.slideit;

import java.io.IOException;
import java.util.Set;

import com.example.prova.R;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {
    
    private BluetoothAdapter mBluetoothAdapter;
    private ConnectThread ct;
    
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                
                 if(device.getName().contains("andrea"))
                     connectDevice(device);
                
            }
        }
    };
    
    private void connectDevice(BluetoothDevice device) {
        mBluetoothAdapter.cancelDiscovery();
        (ct = new ConnectThread(device)).start();
    }
    
    private void sendCommand(String command) {
        try {
            ct.getSocket().getOutputStream().write(command.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
        }
        
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);
        }
        
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

        if (pairedDevices.size() > 0) {
             for (BluetoothDevice device : pairedDevices) {
                 Log.i("DEVICE", "" + device.getName());
                 if(device.getName().contains("andrea"))
                     connectDevice(device);
             }
         }
         
         IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
         registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
    
         mBluetoothAdapter.startDiscovery();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    public void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(mReceiver);
    }
}
