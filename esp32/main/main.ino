#include "defines.hpp"
#include "analogWriteHandler.hpp"


bool ledOn = true;
long loopTime = 0;

int lightInit;  // initial value
int lightVal;   // light reading
long oldTime = 0;
void updateLight() {
    lightVal = analogRead(LDR_SENSOR_PIN); // read the current light levels
//    Serial.println(lightVal);
    if(!ledOn && (lightVal - lightInit > 500)){
      long now = millis();
      if(now - oldTime > 60){
        loopTime = now - oldTime;
        oldTime = now;
      }
      Serial.println(loopTime);
    }
    ledOn = (lightVal - lightInit > 500);
}

void gotTouch(){
   lightInit = analogRead(LDR_SENSOR_PIN);
}

void setup() {
    Serial.begin(9600);
    // Setup timer and attach timer to a led pin
    ledcSetup(LEDC_CHANNEL_0, LEDC_BASE_FREQ, LEDC_TIMER_13_BIT);
    ledcAttachPin(LED_PIN0, LEDC_CHANNEL_0);
    ledcAttachPin(LED_PIN1, LEDC_CHANNEL_1);
    ledcAttachPin(LED_PIN2, LEDC_CHANNEL_2);
    ledcAttachPin(LED_PIN3, LEDC_CHANNEL_3);
    ledcAttachPin(LED_PIN4, LEDC_CHANNEL_4);
    ledcAttachPin(LED_PIN5, LEDC_CHANNEL_5);
    ledcAttachPin(LED_PIN6, LEDC_CHANNEL_6);
    
    lightInit = analogRead(LDR_SENSOR_PIN);
    
    touchAttachInterrupt(T5, gotTouch, threshold);  // T5 = PIN D12
}

long timer = 0;
void loop() {
    timer = millis() - oldTime;
    // set the brightness on LEDC channel 0
    if (timer%(int(loopTime/4)+1) == 0) {
//    ledcAnalogWrite(LEDC_CHANNEL_0, brightness);
//        Serial.println(int(float(timer)/float(loopTime+1) * 200));
//        int brightness = int(float(timer)/float(loopTime+1) * 150);
        int brightness = 255;
        ledcAnalogWrite(LEDC_CHANNEL_0, brightness);
        ledcAnalogWrite(LEDC_CHANNEL_1, brightness);
        ledcAnalogWrite(LEDC_CHANNEL_2, brightness);
        ledcAnalogWrite(LEDC_CHANNEL_3, brightness);
        ledcAnalogWrite(LEDC_CHANNEL_4, brightness);
        ledcAnalogWrite(LEDC_CHANNEL_5, brightness);
        ledcAnalogWrite(LEDC_CHANNEL_6, brightness);
    } else {
        ledcAnalogWrite(LEDC_CHANNEL_0, 0);
        ledcAnalogWrite(LEDC_CHANNEL_1, 0);
        ledcAnalogWrite(LEDC_CHANNEL_2, 0);
        ledcAnalogWrite(LEDC_CHANNEL_3, 0);
        ledcAnalogWrite(LEDC_CHANNEL_4, 0);
        ledcAnalogWrite(LEDC_CHANNEL_5, 0);
        ledcAnalogWrite(LEDC_CHANNEL_6, 0);
    }
    // wait for 30 milliseconds to see the dimming effect
//  readHallVal();
    updateLight();
//  delay(5);
}
