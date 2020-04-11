#include "defines.hpp"
#include "analogWriteHandler.hpp"
#include "LightSensor.hpp"

int ledChannels[] = {LEDC_CHANNEL_0, LEDC_CHANNEL_1, LEDC_CHANNEL_2, LEDC_CHANNEL_3, LEDC_CHANNEL_4, LEDC_CHANNEL_5, LEDC_CHANNEL_6, LEDC_CHANNEL_7, LEDC_CHANNEL_8, LEDC_CHANNEL_9};
LightSensor* lightSensor;

void gotTouch() {
    lightSensor->restart();
}

void setup() {
    Serial.begin(9600);
    // Setup timer and attach timer to a led pin
    
    for(int i = 0; i < NUM_OF_LEDS; i++){
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


    lightSensor = new LightSensor(LDR_SENSOR_PIN);
    touchAttachInterrupt(T5, gotTouch, threshold);  // T5 = PIN D12
}

long timer = 0;

void loop() {
    lightSensor->updateLight();
    long loopTime = lightSensor->getLoopTime();
    timer = lightSensor->getCurrentTimeInLoop();
    // set the brightness on LEDC channel 0
    int brightness = 0;
    if (timer % (int(loopTime / 4) + 1) == 0) {
        brightness = 10;
    }

    for(int i = 0; i < NUM_OF_LEDS; i++){
      ledcAnalogWrite(ledChannels[i], brightness);
    }
    //  delay(5);
}
