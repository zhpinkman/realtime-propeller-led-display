#include <math.h>
#include "WiFi.h"
#include "AsyncUDP.h"

#include "defines.h"
#include "analogWriteHandler.h"
#include "LightSensor.h"
#include "FrameHandler.h"
#include "UDPServer.h"
#include "LEDs.h"




LightSensor *lightSensor;
UDPServer *udpServer;
LEDs *leds;
FrameHandler* frameHandler;

void gotTouch() {
    lightSensor->restart();
}




//UDPBroadcast *udpBroadcast;
void setup() {
    Serial.begin(115200);
    pinMode(LED_BUILTIN, OUTPUT);
    leds = new LEDs();
    leds->init();

    frameHandler = new FrameHandler();

    udpServer = new UDPServer(frameHandler);

    digitalWrite(LED_BUILTIN, HIGH);
    udpServer->startUDPServer();
    digitalWrite(LED_BUILTIN, LOW);

    lightSensor = new LightSensor(LDR_SENSOR_PIN);
    touchAttachInterrupt(T5, gotTouch, TOUCH_THRESHOLD);  // T5 = PIN D12

//    udpBroadcast = new UDPBroadcast();
}

long oldLoopTime = 0;
void loop() {
    frameHandler->updateFrames();
    lightSensor->updateLight();
    long loopTime = lightSensor->getLoopTime();
    long currentTimeInLoop = lightSensor->getCurrentTimeInLoop();
    leds->setLedsByAngularFrame(currentTimeInLoop, loopTime, frameHandler->getCurrentFrame(), frameHandler->getCurrentFrameAngleAccuracy());
    // set the brightness on LEDC channel 0
//    int brightness = 0;
//    if (currentTimeInLoop % (int(loopTime / 4) + 1) == 0) {
//        brightness = 10;
//    }

//      Serial.print("LoopCore: ");
//      Serial.println(xPortGetCoreID());

//    if(abs(oldLoopTime - loopTime) > 0){
//        udpBroadcast->broadcast(String("loop Time:") + String(loopTime) + String("ms"));
//        oldLoopTime = loopTime;
//    }
}
