package com.example.front_sample.utils.udp;

import android.util.Log;

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

public class UDPServerThread extends Thread{
    private final static String TAG = "UDP";
    private int androidPort, boardPort;
    private DatagramSocket socket;
    private boolean running;
    private List<int[][]> context = new ArrayList<>();

    UDPServerThread(int androidPort, int boardPort) {
        super();
        this.androidPort = androidPort;
        this.boardPort = boardPort;
        this.context.add(new int[25][25]);
    }

    void setRunning(boolean running){
        this.running = running;
    }

    void setContext(List<int[][]> newContext) {
        this.context = newContext;
    }

    @Override
    public void run() {

        running = true;

        try {
//            updateState("Starting UDP Server");
            socket = new DatagramSocket(this.androidPort);

//            updateState("UDP Server is running");
            Log.e(TAG, "UDP Server is running");

            while(running){
                byte[] buf = new byte[100];

                // receive request
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);     //this code block the program flow

                String sentence = new String(packet.getData(), 0, packet.getLength());
                InetAddress address = packet.getAddress();
                int port = packet.getPort();
                Log.e(TAG, "Request from: " + address + ":" + port + " -> " + sentence + "\n");

                if(!sentence.contains("Got")) {
                    System.out.println("HERE");
                    // send the response to the client at "address" and "port"
                    String dString = System.currentTimeMillis() + "\n"
                            + "Your address " + ((InetAddress) address).toString() + ":" + String.valueOf(port);
                    byte[] byteBuf = Utils.int2dArrToByteArr(this.context.get(0));
                    if(this.context.size() > 1){
                        this.context.remove(0);
                    }
                    System.out.println(Arrays.toString(byteBuf));
                    buf = dString.getBytes();
                    packet = new DatagramPacket(byteBuf, byteBuf.length, address, this.boardPort);
                    socket.send(packet);
//                    socket.send(packet);
                }
            }

            Log.e(TAG, "UDP Server ended");

        } catch (Exception e){
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        } finally {
            if(socket != null){
                socket.close();
                Log.e(TAG, "socket.close()");
            }
        }
    }
}