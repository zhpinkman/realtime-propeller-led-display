package com.example.front_sample.utils.udp;

import android.util.Log;

import com.example.front_sample.activities.MainActivity;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Date;
import java.util.List;

public class UDPHandler {
    private final static int BOARD_PORT = 9000;
    private final static int ANDROID_PORT = 9001;
    UDPServerThread udpServerThread;

    public UDPHandler() {

    }

    public void startServer() {
        udpServerThread = new UDPServerThread(ANDROID_PORT, BOARD_PORT);
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

    public void setContext(List<int[][]> newContext){
        udpServerThread.setContext(newContext);
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


