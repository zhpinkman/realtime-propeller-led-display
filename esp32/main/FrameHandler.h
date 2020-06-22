//
// Created by Mohsen on 5/13/2020.
//

#include "defines.h"
#include "utils.h"
#include "UDPBroadcast.h"
#include "res/staticPics.h"

#ifndef MAIN_FRAMEHANDLER_H
#define MAIN_FRAMEHANDLER_H

class Frame {
private:
    byte pic[MAX_DEGREE][NUM_OF_LEDS];  // Angular Frame
    int duration;
    int angleAccuracy; // 0 < < MAX_DEGREE

public:
    void constructFrame(byte _pic[MAX_DEGREE][NUM_OF_LEDS], int _duration, int _angleAccuracy = MAX_DEGREE) {
        memcpy(pic, _pic, sizeof(byte) * MAX_DEGREE * NUM_OF_LEDS);
        duration = _duration;
        angleAccuracy = _angleAccuracy;
        if (duration > 5000) {
            duration = 5000;
        }
    }

    int getDuration() {
        return duration;
    }

    byte (*getPic())[NUM_OF_LEDS] {
        return pic;
    }

    int getAngleAccuracy() {
        return angleAccuracy;
    }

    void deletePic() {

    }
};

class FrameHandler {
private:
    Frame frames[MAX_FRAMES_ARRAY_LEN];
    int framesArrLen = 1;
    int currentFrameIndex = 0;
    int receivingMaxWaitTime = 1000;

    Timer *requestTimer, *frameTimer;
    UDPBroadcast *udpBroadcast;

    void requestNewFrames() {
        int requestingFramesCount = min(MAX_FRAMES_ARRAY_LEN - framesArrLen, MAX_NUM_OF_REQUESTING_FRAMES);
        udpBroadcast->broadcast(String(requestingFramesCount));
    }

    int nextFrameIndex() {
//        Serial.print("Frame change:");
//        Serial.println((currentFrameIndex + 1) % MAX_FRAMES_ARRAY_LEN);
        return (currentFrameIndex + 1) % MAX_FRAMES_ARRAY_LEN;
    }

    int nextFrameToAddIndex() {
        return (currentFrameIndex + framesArrLen) % MAX_FRAMES_ARRAY_LEN;
    }

public:
    FrameHandler() {
        udpBroadcast = new UDPBroadcast();
        frames[0].constructFrame(grayscale360, 10000);
        requestTimer = new Timer();
        frameTimer = new Timer();
        requestTimer->start();
        frameTimer->start();
    }

    void addFrame(byte frame[MAX_DEGREE][NUM_OF_LEDS], int duration) {
        if (framesArrLen >= MAX_FRAMES_ARRAY_LEN) {
            slowDownReceiving();
        } else {
//            Serial.print("add as frame number -> ");
//            Serial.print(nextFrameToAddIndex());
//            Serial.print(", Duration = ");
//            Serial.println(duration);
            
            speedUpReceiving();
            frames[nextFrameToAddIndex()].constructFrame(frame, duration);
            framesArrLen++;
            requestTimer->start();
            //        Serial.println(frame[10][10]);
        }
    }

    void addFrameImmediately(byte frame[MAX_DEGREE][NUM_OF_LEDS], int duration) {
        framesArrLen = 1;
        currentFrameIndex = 0;
        frames[0].constructFrame(frame, duration);
        requestTimer->start();
    }

    void speedUpReceiving() {
        this->receivingMaxWaitTime -= 10;
        if (this->receivingMaxWaitTime < 100) {
            this->receivingMaxWaitTime = 100;
        }
    }

    void slowDownReceiving() {
        this->receivingMaxWaitTime * 2;
    }

    void updateFrames() {
        if (frameTimer->getElapsedTime() > frames[currentFrameIndex].getDuration()) {
            if (framesArrLen > 1) {
                frames[currentFrameIndex].deletePic();
                currentFrameIndex = nextFrameIndex();
                framesArrLen--;
                frameTimer->start();
            }
//            Serial.print("FramesArrLen = ");
//            Serial.println(framesArrLen);
//            Serial.println(currentFrameIndex);

        }

        if (framesArrLen < 20 && requestTimer->getElapsedTime() > 1000) {
            requestTimer->start();
            requestNewFrames();
        }
    }

    byte (*getCurrentFrame())[NUM_OF_LEDS] {
        return frames[currentFrameIndex].getPic();
    }

    int getCurrentFrameAngleAccuracy() {
        return frames[currentFrameIndex].getAngleAccuracy();
    }

};


#endif //MAIN_FRAMEHANDLER_H
