package com.example.aman.pccontrol.connect;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.aman.pccontrol.CallbackReceiver;
import com.example.aman.pccontrol.MainActivity;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;



public abstract class MakeConnection extends AsyncTask<Void, Void, Socket> implements CallbackReceiver {

    String ipAddress, port;
    Context context;
    Socket clientSocket;

	MakeConnection(String ipAddress, String port, Context context) {
	    this.ipAddress = ipAddress;
	    this.port = port;
	    this.context = context;
	}

	@Override
	protected Socket doInBackground(Void... params) {
		try {
			int portNumber = Integer.parseInt(port);
			SocketAddress socketAddress = new InetSocketAddress(ipAddress, portNumber);
			clientSocket = new Socket();
			// 3s timeout
			clientSocket.connect(socketAddress, 100000);
            Log.d("Socket Connected", "doInBackground: ");
            MainActivity.objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
            Log.d("Input Stream", "doInBackground: obtained");
            MainActivity.objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
		} catch(Exception e) {
			e.printStackTrace();
			clientSocket = null;
		}
        Log.d("Client Socket", "doInBackground: Socket Retruned");
        return clientSocket;

	}
    
	protected void onPostExecute(Socket clientSocket) {
		receiveData(clientSocket);
	}

	@Override
	public abstract void receiveData(Object result);
}