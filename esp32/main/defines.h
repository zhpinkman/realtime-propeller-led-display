/*
 LEDC Software Fade

 This example shows how to software fade LED
 using the ledcWrite function.

 Code adapted from original Arduino Fade example:
 https://www.arduino.cc/en/Tutorial/Fade

 This example code is in the public domain.
 */

#ifndef DEFINES_H
#define DEFINES_H

#define NUM_OF_LEDS 10

// use first channel of 16 channels (started from zero)
#define LEDC_CHANNEL_0     0
#define LEDC_CHANNEL_1     1
#define LEDC_CHANNEL_2     2
#define LEDC_CHANNEL_3     3
#define LEDC_CHANNEL_4     4
#define LEDC_CHANNEL_5     5
#define LEDC_CHANNEL_6     6
#define LEDC_CHANNEL_7     7
#define LEDC_CHANNEL_8     8
#define LEDC_CHANNEL_9     9

// use 13 bit precission for LEDC timer
#define LEDC_TIMER_13_BIT  13

// use 5000 Hz as a LEDC base frequency
#define LEDC_BASE_FREQ     5000

// fade LED PIN (replace with LED_BUILTIN constant for built-in LED)
#define LED_PIN0           32
#define LED_PIN1           33
#define LED_PIN2           25
#define LED_PIN3           22
#define LED_PIN4           21
#define LED_PIN5           19
#define LED_PIN6           18
#define LED_PIN7           5
#define LED_PIN8           4
#define LED_PIN9           23

#define LDR_SENSOR_PIN     34 // use pins 32 and above when WiFi is ON

#define LED_BUILTIN 2

#define TOUCH_THRESHOLD 40
#define LDR_DIFFERENTIAL_THRESHOLD 400
#define PI 3.14159265
#define MAX_BRIGHTNESS 255  // 0 <= <= 255
#define BRIGHTNESS_STEPS 255  // 1 <= <= 255
#define BRIGHTNESS_CHANGE_FACTOR 3  // pow of value (brightness change factor difference in dark and bright values)

#define MAX_FRAMES_ARRAY_LEN 31  // Caution Mohsen: this amount * MAX_DEGREE * NUM_OF_LEDS will be allocated. (SRAM has 512KiB) (32 will not work)
#define MAX_DEGREE 360  // How accurate frames are in terms of angle (NUM_OF_LEDS determines linear accuracy)  // Caution Mohsen: this amount times frames array size will be allocated. (SRAM has 512KiB)

#define MAX_FRAME_WIDTH 30  // Just for square pictures mode

// NETWORK
#define SSID "D-Link"
#define PASSWORD "shapanhamed"
#define BOARD_PORT 9000
#define ANDROID_PORT 9001

#define MAX_NUM_OF_REQUESTING_FRAMES 10  // Receiving frames window size
#define REQUESTING_FRAMES_INTERVAL 1000  // minimum interval between 2 consecutive request

#endif //DEFINES_H


    
