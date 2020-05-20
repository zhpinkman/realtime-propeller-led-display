//
// Created by Mohsen on 5/20/2020.
//

#ifndef MAIN_UDPSERVER_H
#define MAIN_UDPSERVER_H

#include <WiFi.h>
#include <HTTPClient.h>
#include "utils.h"
#include "FrameHandler.h"

class UDPServer {
private:
    AsyncUDP udp;
//    const char * ssid = "DM-JoinMe";
//    const char * password = "87654321";

    const char * ssid = SSID;
    const char * password = PASSWORD;

    int currentDownloadingFrameIndex = 0;
    int currentRequestedFramesCount = 0;

    FrameHandler* frameHandler;

public:
    UDPServer(FrameHandler* _frameHandler){
        frameHandler = _frameHandler;
    }

    void startUDPServer() {
        WiFi.mode(WIFI_STA);
        WiFi.begin(ssid, password);
        if (WiFi.waitForConnectResult() != WL_CONNECTED) {
            Serial.println("WiFi Failed");
            while (1) {
                delay(1000);
                Serial.println(WiFi.waitForConnectResult());
                if (WiFi.waitForConnectResult() == WL_CONNECTED) {
                    break;
                }
            }
        }
        if (udp.listen(BOARD_PORT)) {
            Serial.print("UDP Listening on IP: ");
            Serial.println(WiFi.localIP());
            udp.onPacket([&](AsyncUDPPacket packet) {
//                Serial.print(String(millis()));
//                Serial.println("ms - ANSWER RECEIVED");
//
//                Serial.print("UDPCore: ");
//                Serial.println(xPortGetCoreID());
//
                Serial.print("UDP Packet Type: ");
                Serial.print(packet.isBroadcast() ? "Broadcast" : packet.isMulticast() ? "Multicast" : "Unicast");
                Serial.print(", From: ");
                Serial.print(packet.remoteIP());
                Serial.print(":");
                Serial.print(packet.remotePort());
                Serial.print(", To: ");
                Serial.print(packet.localIP());
                Serial.print(":");
                Serial.print(packet.localPort());
                Serial.print(", Length: ");
                Serial.print(packet.length());
                Serial.print(", Data: ");
                Serial.write(packet.data(), packet.length());
                Serial.println();

                parseCommand(packet.data(), packet.length());

                //reply to the client
//                packet.printf("Got %u bytes of data", packet.length());
            });
        } else {
            Serial.println("Failed starting UDP server!");
        }
    }

    void parseCommand(byte packetData[], int packetLength) {
        unsigned char command = packetData[0];
        int frameNumber = packetData[1];
        int frameDuration = (int(packetData[3]) << 8) + int(packetData[2]);
//        Serial.println(frameNumber);
//        Serial.println(frameDuration);
        if (command == 'F') {
//            frameHandler->addToFrameHandler(to2dArr(packetData, 4), frameDuration);
        }
        if (command == 'E') {
//            Serial.write(packetData, packetLength);
//            Serial.println();
        }
    }

    byte (*to2dArr(int a))[NUM_OF_LEDS]  {
        byte result[MAX_DEGREE][NUM_OF_LEDS];
//        memcpy(result, input + prefixLength, sizeof (byte) * MAX_DEGREE * NUM_OF_LEDS);
        return result;
    }



};


#endif //MAIN_UDPSERVER_H
