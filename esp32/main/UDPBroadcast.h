//
// Created by Mohsen on 4/29/2020.
//

#ifndef ESP32_UDPBROADCAST_H
#define ESP32_UDPBROADCAST_H

#include <WiFi.h>
#include <HTTPClient.h>
#include "utils.h"

AsyncUDP singleBroadcastUDP;

class UDPBroadcast {
private:
    AsyncUDP udp;
//    const char * ssid = "DM-JoinMe";
//    const char * password = "87654321";
    
    const char * ssid = SSID;
    const char * password = PASSWORD;
    
    static void BroadcastForeverTaskCode(void *pvParameters) {
        char* msg2 = (char*)pvParameters;
        AsyncUDP broadcastUDP;
        Serial.print("Task1 running on core ");
        Serial.println(xPortGetCoreID());

        for (;;) {
            Serial.println(msg2);
            Serial.print(String(millis()));
            Serial.println("ms - START BROADCAST");
            broadcastUDP.broadcastTo(String(millis()).c_str(), ANDROID_PORT);

//            long m = millis();
//            HTTPClient http;
//            http.begin("http://192.168.1.7:8080/"); //HTTP
//            // start connection and send HTTP header
//            int httpCode = http.GET();
//            Serial.print(String(millis()-m));
//            Serial.println("ms");
//            // httpCode will be negative on error
//            if(httpCode > 0) {
//                // file found at server
//                if(httpCode == HTTP_CODE_OK) {
//                    String payload = http.getString();
//                    Serial.print(sizeof(payload));
//                    Serial.println("B");
//                    Serial.println(payload);
//                }
//            } else {
//                Serial.println(http.errorToString(httpCode).c_str());
//            }
//            http.end();
            
            delay(3000);
        }
        vTaskDelete( NULL );
    }

    static void BroadcastTaskCode(void *pvParameters) {
        char* msg = (char*)pvParameters;
        
//        Serial.print("BroadcastTaskCode running on core ");
//        Serial.println(xPortGetCoreID());

        
        Serial.print(String(millis()));
        Serial.print("ms - START BROADCAST: ");
        Serial.println(msg);
        singleBroadcastUDP.broadcastTo(msg, ANDROID_PORT);
//        broadcastUDP.close();
        
        vTaskDelete( NULL );
    }

    static void broadcastCode(void *pvParameters) {
      
      
    }

public:
    UDPBroadcast(){
    }

    void startBroadcastTask() {
        const char* msg = "Message sending on Broadcast";
        TaskHandle_t BroadcastForeverTask;
        xTaskCreatePinnedToCore(
                BroadcastForeverTaskCode,   /* Task function. */
                "Task1",     /* name of task. */
                10000,       /* Stack size of task */
//                NULL,        /* parameter of the task */
                (void*)msg,
                1,           /* priority of the task */
                &BroadcastForeverTask,      /* Task handle to keep track of created task */
                0);          /* pin task to core 0 */
    }

    void broadcast(String messageStr) {
        char messageCopyCharPointer[messageStr.length()];
        messageStr.toCharArray(messageCopyCharPointer, messageStr.length()+1);
        TaskHandle_t BroadcastTask;
        xTaskCreatePinnedToCore(
                BroadcastTaskCode,   /* Task function. */
                "broadcastTask",     /* name of task. */
                10000,       /* Stack size of task */
//                NULL,        /* parameter of the task */
                (void*)messageCopyCharPointer,
                1,           /* priority of the task */
                &BroadcastTask,      /* Task handle to keep track of created task */
                0);          /* pin task to core 0 */
    }
};



#endif //ESP32_UDPBROADCAST_H
