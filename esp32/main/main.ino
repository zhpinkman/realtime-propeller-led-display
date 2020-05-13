#include <math.h>
#include "WiFi.h"
#include "AsyncUDP.h"

#include "defines.h"
#include "analogWriteHandler.h"
#include "LightSensor.h"
#include "UDPHandler.h"
#include "LEDs.h"



LightSensor *lightSensor;
UDPHandler *udpHandler;
LEDs *leds;

void gotTouch() {
    lightSensor->restart();
}





void setup() {
    Serial.begin(115200);
    pinMode(LED_BUILTIN, OUTPUT);
    leds = new LEDs();
    leds->init();

    udpHandler = new UDPHandler();
    digitalWrite(LED_BUILTIN, HIGH);
    udpHandler->startUDPServer();
//    udpHandler->startBroadcastTask();
    digitalWrite(LED_BUILTIN, LOW);

    lightSensor = new LightSensor(LDR_SENSOR_PIN);
    touchAttachInterrupt(T5, gotTouch, threshold);  // T5 = PIN D12
}

void loop() {
    lightSensor->updateLight();
    long loopTime = lightSensor->getLoopTime();
    long currentTimeInLoop = lightSensor->getCurrentTimeInLoop();
    // set the brightness on LEDC channel 0
//    int brightness = 0;
//    if (currentTimeInLoop % (int(loopTime / 4) + 1) == 0) {
//        brightness = 10;
//    }

    leds->setLeds(currentTimeInLoop, loopTime);
//    for(int i = 0; i < NUM_OF_LEDS; i++){
//      ledcAnalogWrite(ledChannels[i], brightness);
//    }
    udpHandler->broadcast(String(lightSensor->getLoopTime()));
    delay(1000);

//      Serial.print("LoopCore: ");
//      Serial.println(xPortGetCoreID());
}
