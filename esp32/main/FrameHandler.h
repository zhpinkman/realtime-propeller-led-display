//
// Created by Mohsen on 5/13/2020.
//

#include "defines.h"
#include "utils.h"
#include "UDPHandler.h"

#ifndef MAIN_FRAMEHANDLER_H
#define MAIN_FRAMEHANDLER_H

class Frame {
private:
    byte pic[MAX_DEGREE][NUM_OF_LEDS];  // Angular Frame
    int duration;
    int angleAccuracy; // 0 < < MAX_DEGREE

public:
    void constructFrame(byte _pic[MAX_DEGREE][NUM_OF_LEDS], int _duration, int _angleAccuracy = MAX_DEGREE){
        memcpy(pic, _pic, sizeof (byte) * MAX_DEGREE * NUM_OF_LEDS);
        duration = _duration;
        angleAccuracy = _angleAccuracy;
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

    Timer *requestTimer, *frameTimer;
    UDPHandler* udpHandler;

    void requestNewFrames() {
        udpHandler->broadcast(String(MAX_FRAMES_ARRAY_LEN - framesArrLen));
    }

    int nextFrameIndex() {
        return (currentFrameIndex + 1) % MAX_FRAMES_ARRAY_LEN;
    }

public:
    FrameHandler(UDPHandler* _udpHandler) {
        udpHandler = _udpHandler;
        frames[0].constructFrame(grayscale360, 10000);
        requestTimer = new Timer();
        frameTimer = new Timer();
        requestTimer->start();
        frameTimer->start();
    }

    void addFrame(byte frame[MAX_DEGREE][NUM_OF_LEDS], int duration) {
        nextFrameIndex();
        frames[currentFrameIndex].constructFrame(frame, duration);
    }

    void updateFrames() {
        if(frameTimer->getElapsedTime() > frames[currentFrameIndex].getDuration()){
            if(framesArrLen > 1){
                frames[currentFrameIndex].deletePic();
                currentFrameIndex = nextFrameIndex();
            }
        }

        if(framesArrLen < 20 && requestTimer->getElapsedTime() > 5000) {
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
