//
// Created by Mohsen on 5/13/2020.
//

#ifndef MAIN_UTILS_H
#define MAIN_UTILS_H

class Timer {
private:
    unsigned long startTime;
public:
    void start() {
        startTime = millis();
    }

    unsigned long getElapsedTime(){
        return millis() - startTime;
    }
};

int bytesToUnsigned(byte b) {
    int result = (int) b;
    return result;
}

#endif //MAIN_UTILS_H
