//
// Created by Mohsen on 5/13/2020.
//
#include <vector.h>
#include "utils.h"

#ifndef MAIN_FRAMEHANDLER_H
#define MAIN_FRAMEHANDLER_H

class FrameHandler {
private:
    vector<Frame> frames;
    Timer* timer;
public:
    FrameHandler() {
        timer = new Timer();
    }

};


class Frame {
private:
    int pic[30][30];
    int duration;
};

#endif //MAIN_FRAMEHANDLER_H
