package com.example.front_sample.utils.udp;

import android.util.Log;

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

import static java.lang.Math.min;

public class UDPServerThread extends Thread {
    private final static String TAG = "UDP";
    private final static int UDP_SIZE_LIMIT_BYTES = Config.UDP_SIZE_LIMIT_BYTES;
    private int androidPort, boardPort;
    private DatagramSocket socket;
    private boolean running;
    private List<int[][]> context = new ArrayList<>();

    UDPServerThread(int androidPort, int boardPort) {
        super();
        this.androidPort = androidPort;
        this.boardPort = boardPort;
        this.context.add(new int[128][128]);
    }

    void setRunning(boolean running) {
        this.running = running;
    }

    void setContext(List<int[][]> newContext) {
        this.context = newContext;
    }

    private void sendDatagramPacket(byte[] byteBuf, InetAddress address) throws IOException {
        for (int packetIndex = 0; packetIndex < byteBuf.length; packetIndex += UDP_SIZE_LIMIT_BYTES) {
            byte[] byteChunk = Arrays.copyOfRange(byteBuf, packetIndex, min(packetIndex + UDP_SIZE_LIMIT_BYTES, byteBuf.length));
            Log.e(TAG, Arrays.toString(byteChunk));
            DatagramPacket packet = new DatagramPacket(byteChunk, byteChunk.length, address, this.boardPort);
            socket.send(packet);
        }
    }

    @Override
    public void run() {

        running = true;

        try {
//            updateState("Starting UDP Server");
            socket = new DatagramSocket(this.androidPort);

//            updateState("UDP Server is running");
            Log.e(TAG, "UDP Server is running");

            while (running) {
                byte[] buf = new byte[100];

                // receive request
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);     //this code block the program flow

                String sentence = new String(packet.getData(), 0, packet.getLength());
                InetAddress address = packet.getAddress();
                int port = packet.getPort();
                Log.e(TAG, "Request from: " + address + ":" + port + " -> " + sentence + "\n");

                try {
                    int requestedFramesCount = Integer.parseInt(sentence);
                    if (requestedFramesCount > 0) {
                        for (int i = 0; i < requestedFramesCount; i++) {
                            int[][] angularFrame = Utils.squareToAngular(this.context.get(0));
                            byte[] prefix = new byte[3];
                            prefix[0] = (byte) 'F';
                            prefix[1] = (byte) i;
                            prefix[2] = (byte) angularFrame.length;
                            byte[] byteBuf = Utils.int2dArrToByteArr(angularFrame, prefix);
                            Log.e(TAG, Arrays.toString(byteBuf));
                            this.sendDatagramPacket(byteBuf, address);

                            if (this.context.size() > 1) {
                                this.context.remove(0);
                            }else{
                                break;
                            }
                        }
                    }
                }catch (Exception e){
//                    System.out.println("");
                }
            }

            Log.e(TAG, "UDP Server ended");

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        } finally {
            if (socket != null) {
                socket.close();
                Log.e(TAG, "socket.close()");
            }
        }
    }
}