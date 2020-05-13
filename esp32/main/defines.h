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
#define LEDC_BASE_FREQ     10000

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

#define LDR_SENSOR_PIN     15

#define LED_BUILTIN 2

int threshold = 40;
#define PI 3.14159265
#define MAX_BRIGHTNESS 150

#define MAX_FRAMES_ARRAY_LEN 30
#define MAX_FRAME_WIDTH 30  // Caution Mohsen: this amount times frames array size will be allocated. (SRAM has 512KiB)

// NETWORK
#define BOARD_PORT 9000
#define ANDROID_PORT 9001


#endif //DEFINES_H


    
