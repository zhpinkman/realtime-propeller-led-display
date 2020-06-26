

class LightSensor {
private:
    int sensorPin;
    int lightInit;
    int oldTime = 0;
    int loopTime = 0;
    bool isInLight = false;

public:
    LightSensor(int _sensorPin) {
        sensorPin = _sensorPin;
        lightInit = analogRead(sensorPin);
    }

    void restart() {
        lightInit = analogRead(sensorPin);
    }

    void updateLight() {
        int lightVal = analogRead(sensorPin); // read the current light levels
//        Serial.println(lightVal);
        if (!isInLight && (lightVal - lightInit > LDR_DIFFERENTIAL_THRESHOLD)) {
            long now = millis();
            if (now - oldTime > 60) {
                loopTime = now - oldTime;
                oldTime = now;
            }
        }
        isInLight = (lightVal - lightInit > LDR_DIFFERENTIAL_THRESHOLD);
    }

    float getLoopTime() {
        return loopTime;
    }

    float getCurrentTimeInLoop() {
        if(loopTime != 0)
          return (millis() - oldTime) % loopTime;
        else
          return 0;
    }

};
