// Arduino like analogWrite
// value has to be between 0 and valueMax
#include <stdint.h>

void ledcAnalogWrite(uint8_t channel, uint32_t value, uint32_t valueMax = 255) {
    // calculate duty, 8191 from 2 ^ 13 - 1
    uint32_t duty = (8191 / valueMax) * min(value, valueMax);
    // write duty to LEDC
    ledcWrite(channel, duty);
}
