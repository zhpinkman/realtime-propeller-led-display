#include <math.h>
#include "WiFi.h"
#include "AsyncUDP.h"

#include "defines.h"
#include "analogWriteHandler.h"
#include "LightSensor.h"
#include "UDPHandler.h"

#include "res/staticPics.h"

int ledChannels[] = {LEDC_CHANNEL_0, LEDC_CHANNEL_1, LEDC_CHANNEL_2, LEDC_CHANNEL_3, LEDC_CHANNEL_4, LEDC_CHANNEL_5,
                     LEDC_CHANNEL_6, LEDC_CHANNEL_7, LEDC_CHANNEL_8, LEDC_CHANNEL_9};
LightSensor *lightSensor;
UDPHandler *udpHandler;

void gotTouch() {
    lightSensor->restart();
}


void setLeds(long currentTimeInLoop, long loopTime) {
    double radian = ((float) currentTimeInLoop / (float) loopTime) * 2 * PI;
//  Serial.println(radian / PI * 180);
    double baseX = cos(radian) * (double) PIC_SIZE / 2 / (double) NUM_OF_LEDS;
    double baseY = sin(radian) * (double) PIC_SIZE / 2 / (double) NUM_OF_LEDS;
    for (int i = 0; i < NUM_OF_LEDS; i++) {
        int x = floor(baseX * (double) i) + PIC_SIZE / 2;
        int y = floor(baseY * (double) i) + PIC_SIZE / 2;
//    Serial.print("X: ");
//    Serial.print(x);
//    Serial.print(", Y: ");
//    Serial.println(y);
        int brightness = pic[y][x] * MAX_BRIGHTNESS / 255;
//    Serial.println(brightness);
        ledcAnalogWrite(ledChannels[i], brightness);
    }
}


void setup() {
    Serial.begin(115200);
    pinMode(LED_BUILTIN, OUTPUT);
    for (int i = 0; i < NUM_OF_LEDS; i++) {
        ledcSetup(ledChannels[i], LEDC_BASE_FREQ, LEDC_TIMER_13_BIT);
    }
    ledcAttachPin(LED_PIN0, LEDC_CHANNEL_0);
    ledcAttachPin(LED_PIN1, LEDC_CHANNEL_1);
    ledcAttachPin(LED_PIN2, LEDC_CHANNEL_2);
    ledcAttachPin(LED_PIN3, LEDC_CHANNEL_3);
    ledcAttachPin(LED_PIN4, LEDC_CHANNEL_4);
    ledcAttachPin(LED_PIN5, LEDC_CHANNEL_5);
    ledcAttachPin(LED_PIN6, LEDC_CHANNEL_6);
    ledcAttachPin(LED_PIN7, LEDC_CHANNEL_7);
    ledcAttachPin(LED_PIN8, LEDC_CHANNEL_8);
    ledcAttachPin(LED_PIN9, LEDC_CHANNEL_9);

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

    setLeds(currentTimeInLoop, loopTime);
//    for(int i = 0; i < NUM_OF_LEDS; i++){
//      ledcAnalogWrite(ledChannels[i], brightness);
//    }
    udpHandler->broadcast(String(lightSensor->getLoopTime()));
    delay(1000);

//      Serial.print("LoopCore: ");
//      Serial.println(xPortGetCoreID());
}
