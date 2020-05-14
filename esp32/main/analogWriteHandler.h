// Arduino like analogWrite
// value has to be between 0 and valueMax
#include <stdint.h>

void ledcAnalogWrite(uint8_t channel, uint32_t value, uint32_t valueMax = 255) {
    // calculate duty, 8191 from 2 ^ 13 - 1
    value = floor(value * BRIGHTNESS_STEPS / 255) * 255 / BRIGHTNESS_STEPS;  // determine value based on number of steps
    
    uint32_t normalizedValue = floor((double) pow(value, BRIGHTNESS_CHANGE_FACTOR) / pow(255, BRIGHTNESS_CHANGE_FACTOR - 1));  // use pow to increase change of brightness in brighter values (due to led hardware characteristics)
//    Serial.println(normalizedValue);
    
    double dutyDouble = ((double) 8191 / (double) valueMax) * (double) min(normalizedValue, valueMax) * (double) MAX_BRIGHTNESS / (double) 255;
//    uint32_t duty = (8191 / valueMax) * min(value, valueMax);
    uint32_t duty = (uint32_t) dutyDouble;
    // write duty to LEDC
    ledcWrite(channel, duty);
}
