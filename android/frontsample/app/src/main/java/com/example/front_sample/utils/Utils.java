package com.example.front_sample.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Build;

import androidx.annotation.RequiresApi;

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

    public static byte[] int2dArrToByteArr(int[][] intArr) throws Exception {
        int arrLen = intArr.length;
        if(arrLen != intArr[0].length){
            throw new Exception("Input array must be a square!");
        } else if(arrLen > 255){
            throw new Exception("input size exceeds 255!");
        }

        byte[] result = new byte[arrLen * arrLen + 1];
        int prefixLen = 2;
        result[0] = (byte) 'F';
        result[1] = (byte) arrLen;
        for (int i = 0; i < arrLen; i++) {
            for (int j = 0; j < arrLen; j++) {
                result[prefixLen + i + j] = (byte) intArr[i][j];
            }
        }
        return result;
    }

    public static int bytesToUnsigned(byte b) {
        return b & 0xFF;
    }
}
