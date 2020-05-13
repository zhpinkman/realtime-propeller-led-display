//
// Created by Mohsen on 5/13/2020.
//
#include "defines.h"
#include "res/staticPics.h"

#ifndef MAIN_LEDS_H
#define MAIN_LEDS_H

int ledChannels[] = {LEDC_CHANNEL_0, LEDC_CHANNEL_1, LEDC_CHANNEL_2, LEDC_CHANNEL_3, LEDC_CHANNEL_4, LEDC_CHANNEL_5,
                         LEDC_CHANNEL_6, LEDC_CHANNEL_7, LEDC_CHANNEL_8, LEDC_CHANNEL_9};
                         
class LEDs {
private:
    

public:
    void init(){
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
            if(brightness < 0 || brightness > 255){
              brightness = 5;
            }
//    Serial.println(brightness);
            ledcAnalogWrite(ledChannels[i], brightness);
        }
    }
};

#endif //MAIN_LEDS_H
