package com.example.front_sample.utils.udp;

import android.util.Log;

import com.example.front_sample.config.Config;
import com.example.front_sample.types.FrameType;
import com.example.front_sample.utils.Utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

import static java.lang.Math.min;

public class UDPServerThread extends Thread {
    private final static String TAG = "UDP";
    private final static int UDP_SIZE_LIMIT_BYTES = Config.UDP_SIZE_LIMIT_BYTES;
    private int androidPort, boardPort;
    private DatagramSocket socket;
    private boolean running;
    private UDPHandler udpHandlerParent;
    private InetAddress boardAddress;

    UDPServerThread(int androidPort, int boardPort, UDPHandler udpHandlerParent) {
        super();
        this.androidPort = androidPort;
        this.boardPort = boardPort;
        this.udpHandlerParent = udpHandlerParent;
    }

    void setRunning(boolean running) {
        this.running = running;
        if (!running) {
            if (socket != null) {
                socket.close();
                Log.e(TAG, "socket.close()");
            }
        }
    }


    private synchronized void sendDatagramPacket(byte[] byteBuf, InetAddress address, byte[] prefix, FrameType frameType) throws IOException {
        Log.v(TAG, String.valueOf(byteBuf.length));
        for (int packetIndex = 0; packetIndex < byteBuf.length; packetIndex += UDP_SIZE_LIMIT_BYTES) {
//            Log.e(TAG, String.valueOf(packetIndex));
            if (packetIndex + UDP_SIZE_LIMIT_BYTES >= byteBuf.length) {
                if (frameType == FrameType.VIDEO) {
                    prefix[0] = 'E';  // END OF FRAME COMMAND
                } else if (frameType == FrameType.PICTURE) {
                    prefix[0] = 'I';  // END OF IMMEDIATE PICTURE COMMAND
                }
            }
            byte[] byteChunk = Arrays.copyOfRange(byteBuf, packetIndex, min(packetIndex + UDP_SIZE_LIMIT_BYTES, byteBuf.length));
            byteChunk = Utils.concatArrays(prefix, byteChunk);
            Log.v(TAG, Arrays.toString(byteChunk));
            DatagramPacket packet = new DatagramPacket(byteChunk, byteChunk.length, address, this.boardPort);
            socket.send(packet);
        }
        this.udpHandlerParent.log("Sent data: " + Arrays.toString(prefix) + Arrays.toString(Arrays.copyOfRange(byteBuf, 0, 5)) + "... (" + byteBuf.length + "B)");
    }

    private byte[] preparePrefix(int frameNumber, int angularFrameLength, int frameDuration) {
        byte[] prefix = new byte[4];
        prefix[0] = (byte) 'F';  // FRAME COMMAND
        prefix[1] = (byte) frameNumber;  // FRAME NUMBER 0 TO NUMBER REQUESTED - 1
//        prefix[2] = (byte) angularFrameLength;  // USUALLY 360

        // & 0xFF=255 masks all but the lowest eight bits
        prefix[2] = (byte) (frameDuration & 0xFF);  // frame duration max 16 bit = 65536
        prefix[3] = (byte) ((frameDuration >> 8) & 0xFF);  // higher bits
        return prefix;
    }

    public void sendPictureImmediately(int[][] angularFrame) throws Exception {
//        System.out.println(Arrays.toString(angularFrame[0]));
        byte[] byteBuf = Utils.int2dArrToByteArr(angularFrame);
        Log.v(TAG, Arrays.toString(byteBuf));
        byte[] prefix = this.preparePrefix(0, angularFrame.length, udpHandlerParent.getFrameDuration());
        if (this.boardAddress == null)
            throw new Exception("Not received any request from board yet to know it's address");
        this.sendDatagramPacket(byteBuf, this.boardAddress, prefix, FrameType.PICTURE);
    }

    @Override
    public void run() {

        running = true;

        try {
//            updateState("Starting UDP Server");
            socket = new DatagramSocket(this.androidPort);

//            updateState("UDP Server is running");
            Log.v(TAG, "UDP Server is running");

            while (running) {
                byte[] buf = new byte[100];

                // receive request
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);     //this code block the program flow

                String sentence = new String(packet.getData(), 0, packet.getLength());
                InetAddress address = packet.getAddress();
                this.boardAddress = address;
                int port = packet.getPort();
                this.udpHandlerParent.log("Request from: " + address + ":" + port + " -> " + sentence);

                try {
                    int requestedFramesCount = Integer.parseInt(sentence);
                    if (requestedFramesCount > 0) {
                        for (int i = 0; i < requestedFramesCount; i++) {
                            int[][] angularFrame = udpHandlerParent.getCurrentFrame();
//                            System.out.println(Arrays.toString(angularFrame[0]));
                            byte[] byteBuf = Utils.int2dArrToByteArr(angularFrame);
                            Log.v(TAG, Arrays.toString(byteBuf));
                            byte[] prefix = this.preparePrefix(i, angularFrame.length, udpHandlerParent.getFrameDuration());
                            this.sendDatagramPacket(byteBuf, address, prefix, FrameType.VIDEO);

                            if (!this.udpHandlerParent.nextFrame()) {
                                break;  // size of context is 1 and can't pop
                            }
                        }
                    }
                } catch (Exception e) {
//                    System.out.println("");
                }
            }

            Log.v(TAG, "UDP Server ended");

        } catch (Exception e) {
            udpHandlerParent.log(e.getMessage());
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