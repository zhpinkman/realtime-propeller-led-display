package com.example.front_sample.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.front_sample.config.Config;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class Utils {
    /**
     * Get IP address from first non-localhost interface
     *
     * @param useIPv4 true=return ipv4, false=return ipv6
     * @return address or empty string
     */
    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':') < 0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim < 0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) {
        } // for now eat exceptions
        return "";
    }

    public static byte[] int2dArrToByteArr(int[][] intArr, byte[] prefix) throws Exception {
        int height = intArr.length;
        int width = intArr[0].length;
//        if(arrLen != intArr[0].length){
//            throw new Exception("Input array must be a square!");
//        } else if(arrLen > 255){
//            throw new Exception("input size exceeds 255!");
//        }

        byte[] result = new byte[height * width + 1];
        for (int i = 0; i < prefix.length; i++) {
            result[i] = prefix[i];
        }
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                result[prefix.length + i + j] = (byte) intArr[i][j];
            }
        }
        return result;
    }

    public static int[][] squareToAngular(int[][] a) {
        int[][] angularResult = new int[Config.MAX_DEGREE][Config.NUM_OF_LEDS];

        for (int degree = 0; degree < 360; degree++) {
            double baseX = Math.cos(Math.toRadians(degree)) * (double) a.length / 2 / (double) Config.NUM_OF_LEDS;
            double baseY = Math.sin(Math.toRadians(degree)) * (double) a.length / 2 / (double) Config.NUM_OF_LEDS;
//            System.out.print(baseX);
//            System.out.print(", ");
//            System.out.println(baseY);
            for (int i = 0; i < Config.NUM_OF_LEDS; i++) {
                int x = (int) (Math.floor(baseX * i) + a.length / 2);
                int y = (int) (Math.floor(baseY * i) + a.length / 2);
//                System.out.print(x);
//                System.out.print(", ");
//                System.out.println(y);

//                if(degree < 180)
//                    angularResult[degree][i] = degree % 180 * 255 / 180;
//                if(degree >= 180)
//                    angularResult[degree][i] = 255 - (degree % 180 * 255 / 180);

                angularResult[degree][i] = a[y][x];
            }
        }

        return angularResult;
    }

    public static int bytesToUnsigned(byte b) {
        return b & 0xFF;
    }
}
