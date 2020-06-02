package com.example.front_sample.utils.udp;

import android.util.Log;

import com.example.front_sample.activities.MainActivity;
import com.example.front_sample.config.Config;
import com.example.front_sample.utils.Utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class UDPHandler {
    private UDPServerThread udpServerThread;
    private List<int[][]> angularContext = new ArrayList<>();
    private String log = "";

    public UDPHandler() {
        this.angularContext.add(new int[Config.MAX_DEGREE][Config.NUM_OF_LEDS]);
    }

    public void startServer() {
        udpServerThread = new UDPServerThread(Config.ANDROID_PORT, Config.BOARD_PORT, this);
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

    public synchronized List<int[][]> getAngularContext() {
        return this.angularContext;
    }

    public synchronized boolean popFirstAngularContext() {  // returns false if context size is 1
        if (this.angularContext.size() > 1) {
            this.angularContext.remove(0);
            return true;
        }else{
            return false;
        }
    }

    public synchronized void setAngularContext(List<int[][]> newAngularContext){
        this.angularContext = newAngularContext;
    }

    public synchronized void setAngularContext(int[][] angularPic){
        List<int[][]> newContext = new ArrayList<>();
        newContext.add(angularPic);
        this.angularContext = newContext;
    }

    public synchronized void setSquareContext(List<int[][]> newSquareContext){
        List<int[][]> newAngularContext = new ArrayList<>();
        for (int[][] ctx:newSquareContext) {
            newAngularContext.add(Utils.squareToAngular(ctx));
        }
        this.angularContext = newAngularContext;
    }

    public synchronized void setSquareContext(int[][] squarePic) {
        List<int[][]> newAngularContext = new ArrayList<>();
        newAngularContext.add(Utils.squareToAngular(squarePic));
        this.angularContext = newAngularContext;
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

    public synchronized String getLogChange() {
        String logCopy = log;
        log = "";
        return logCopy;
    }

    synchronized void log(String newLog) {
        log = log.concat("\n" + newLog);
        Log.e("UDP", newLog);
    }
}


