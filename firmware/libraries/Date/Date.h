#include <TimeLib.h>
// #include <Arduino.h>
#include "WString.h"

class Date{
  public:
    Date(uint16_t year, uint8_t month, uint8_t day);

    void addDay();
    void subDay();
    String toString();
  
  private:
    uint16_t year;
    uint8_t month;
    uint8_t day;
};