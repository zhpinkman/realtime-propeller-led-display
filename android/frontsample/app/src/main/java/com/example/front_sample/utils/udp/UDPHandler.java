package com.example.front_sample.utils.udp;

import android.util.Log;

import com.example.front_sample.activities.MainActivity;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Date;

public class UDPHandler {
    private int broadcastPort = 9000;
    static final int UdpServerPORT = 9000;
    UDPServerThread udpServerThread;


    public UDPHandler() {

    }

    public void startServer() {
        udpServerThread = new UDPServerThread(UdpServerPORT);
        udpServerThread.start();
    }

    public void stopServer() throws Exception {
        if(udpServerThread != null){
            udpServerThread.setRunning(false);
            udpServerThread = null;
        }else{
            throw new Exception("Server is not running!");
        }
    }

//    private void updateState(final String state){
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                textViewState.setText(state);
//            }
//        });
//    }

//    private void updatePrompt(final String prompt){
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                textViewPrompt.append(prompt);
//            }
//        });
//    }

    public void receiveBroadcast() {

    }
}


