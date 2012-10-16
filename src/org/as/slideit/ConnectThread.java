package org.as.slideit;

import java.io.IOException;
import java.io.OutputStream;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

class ConnectThread extends Thread {
    private BluetoothSocket mmSocket = null ;
 
    public ConnectThread(BluetoothDevice device) {

        BluetoothSocket tmp = null;
 
        try {
            tmp = device.createRfcommSocketToServiceRecord(Constants.MY_UUID);
        } catch (IOException e) { 
        	Log.e(Constants.TAG, "CONNERR: " + e.getMessage());
        }
        
        mmSocket = tmp;
        
    }
    
    public BluetoothSocket getSocket() {
    	return mmSocket;
    }
 
    public void run() {
 
        try {
            mmSocket.connect();
        } catch (IOException connectException) {
        	Log.e(Constants.TAG, "" + "CONNERR: " + connectException.getMessage());
            try {
                mmSocket.close();
            } catch (IOException closeException) { }
            return;
        }
 
        OutputStream tmpOut = null;
 
        try {
            tmpOut = mmSocket.getOutputStream();
        } catch (IOException e) { }
 
        try {
        	while(true) {
				tmpOut.write("Hello World!".getBytes());
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
 
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) { }
    }
    
}



/*Method m = null;
try {
	m = device.getClass().getMethod("createRfcommSocket", new Class[] { int.class });
} catch (NoSuchMethodException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
try {
	mmSocket = (BluetoothSocket) m.invoke(device, Integer.valueOf(1));
} catch (IllegalArgumentException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
} catch (IllegalAccessException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
} catch (InvocationTargetException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
} // 1==RFCOMM channel code */
