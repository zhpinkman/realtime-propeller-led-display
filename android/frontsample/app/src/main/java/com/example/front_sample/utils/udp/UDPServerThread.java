package com.example.front_sample.utils.udp;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Date;

public class UDPServerThread extends Thread{
    private final static String TAG = "UDP";
    int serverPort;
    DatagramSocket socket;

    boolean running;

    public UDPServerThread(int serverPort) {
        super();
        this.serverPort = serverPort;
    }

    public void setRunning(boolean running){
        this.running = running;
    }

    @Override
    public void run() {

        running = true;

        try {
//            updateState("Starting UDP Server");
            socket = new DatagramSocket(serverPort);

//            updateState("UDP Server is running");
            Log.e(TAG, "UDP Server is running");

            while(running){
                byte[] buf = new byte[256];

                // receive request
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);     //this code block the program flow

                // send the response to the client at "address" and "port"
                InetAddress address = packet.getAddress();
                int port = packet.getPort();

                String sentence = new String( packet.getData(), 0,
                        packet.getLength() );
                Log.e(TAG, "Request from: " + address + ":" + port + " -> " + sentence + "\n");

                String dString = new Date().toString() + "\n"
                        + "Your address " + ((InetAddress) address).toString() + ":" + String.valueOf(port);
                buf = dString.getBytes();
                packet = new DatagramPacket(buf, buf.length, address, port);
                socket.send(packet);

            }

            Log.e(TAG, "UDP Server ended");

        } catch (SocketException e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if(socket != null){
                socket.close();
                Log.e(TAG, "socket.close()");
            }
        }
    }
}