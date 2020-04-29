//
// Created by Mohsen on 4/29/2020.
//

#ifndef ESP32_UDPHANDLER_H
#define ESP32_UDPHANDLER_H

class UDPHandler {
private:
    AsyncUDP udp;
    //const char * ssid = "DM-JoinMe";
    //const char * password = "87654321";
    
    const char * ssid = "D-Link";
    const char * password = "shapanhamed";

    static void BroadcastTaskCode(void *pvParameters) {
        AsyncUDP broadcastUDP;
        Serial.print("Task1 running on core ");
        Serial.println(xPortGetCoreID());

        for (;;) {
            Serial.print(String(millis()));
            Serial.println("ms");
            broadcastUDP.broadcastTo(String(millis()).c_str(), 9000);
            Serial.print(String(millis()));
            Serial.println("ms");
            delay(1000);
        }
        vTaskDelete( NULL );
    }

public:
    UDPHandler(){
      
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
        if (udp.listen(8000)) {
            Serial.print("UDP Listening on IP: ");
            Serial.println(WiFi.localIP());
            udp.onPacket([](AsyncUDPPacket packet) {

                Serial.print("UDPCore: ");
                Serial.println(xPortGetCoreID());

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
                //reply to the client
                packet.printf("Got %u bytes of data", packet.length());
            });
        } else {
            Serial.println("Failed starting UDP server!");
        }
    }

    void startBroadcastTask() {
        TaskHandle_t BroadcastTask;
        xTaskCreatePinnedToCore(
                BroadcastTaskCode,   /* Task function. */
                "Task1",     /* name of task. */
                10000,       /* Stack size of task */
                NULL,        /* parameter of the task */
                1,           /* priority of the task */
                &BroadcastTask,      /* Task handle to keep track of created task */
                0);          /* pin task to core 0 */
    }
};

#endif //ESP32_UDPHANDLER_H
