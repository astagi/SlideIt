package org.as.slideit;

import java.io.IOException;
import java.util.Set;

import org.as.slideit.R;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
    
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothSocket mmSocket;
    
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
        (new PrepareBluetooth(device)).execute("");
    }
    
    private void sendCommand(int cmd) {
        try {
        	mmSocket.getOutputStream().write(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void initGraphics() {
    	Button F5 = (Button)this.findViewById(R.id.F5);
    	Button Ffw = (Button)this.findViewById(R.id.FFW);
    	Button Rew = (Button)this.findViewById(R.id.REW);
    	Button Esc = (Button)this.findViewById(R.id.ESC);

    	
    	F5.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				sendCommand(0xA0);
				
			}
    		
    	});
    	
    	Ffw.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				sendCommand(0xA1);
				
			}
    		
    	});
    	
    	Rew.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				sendCommand(0xA2);
				
			}
    		
    	});
    	
    	Esc.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				sendCommand(0xA3);
				
			}
    		
    	});
    	
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
    
         //mBluetoothAdapter.startDiscovery();
         
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
    
    private class PrepareBluetooth extends AsyncTask<String, Void, String> {

        

		private BluetoothDevice blu_device;

		public PrepareBluetooth(BluetoothDevice dev) {
            blu_device = dev;

            BluetoothSocket tmp = null;

            try {
                tmp = blu_device.createRfcommSocketToServiceRecord(Constants.MY_UUID);
            } catch (IOException e) {
                Log.e(Constants.TAG, "CONNERR: " + e.getMessage());
            }

            mmSocket = tmp;

            try {
                mmSocket.connect();
            } catch (IOException connectException) {
                Log.e(Constants.TAG, "" + "CONNERR: " + connectException.getMessage());
                try {
                    mmSocket.close();
                } catch (IOException closeException) { }
            }
        }

        protected String doInBackground(String... urls) {


            return null;

        }

        protected void onPostExecute(String result) {
            initGraphics();
        }
    }
}
