//
// Created by Mohsen on 5/13/2020.
//
#include "utils.h"
#include "UDPHandler.h"

#ifndef MAIN_FRAMEHANDLER_H
#define MAIN_FRAMEHANDLER_H

class Frame {
private:
    int pic[MAX_FRAME_WIDTH][MAX_FRAME_WIDTH];
    int duration;
    int width;

public:
    void constructFrame(int _pic[MAX_FRAME_WIDTH][MAX_FRAME_WIDTH], int _duration, int _width){
        memcpy(pic, _pic, sizeof (int) * MAX_FRAME_WIDTH * MAX_FRAME_WIDTH);
        duration = _duration;
        width = _width;
    }

    int getDuration() {
        return duration;
    }

    int (*getPic())[MAX_FRAME_WIDTH] {
        return pic;
    }

    int getWidth() {
        return width;
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
        frames[0].constructFrame(crossPic, 10000, 30);
        requestTimer = new Timer();
        frameTimer = new Timer();
        requestTimer->start();
        frameTimer->start();
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

    int (*getCurrentFrame())[MAX_FRAME_WIDTH] {
        return frames[currentFrameIndex].getPic();
    }

    int getCurrentFrameWidth() {
        return frames[currentFrameIndex].getWidth();
    }

};




#endif //MAIN_FRAMEHANDLER_H
