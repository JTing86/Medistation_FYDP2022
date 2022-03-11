#include "Date.h"

const uint8_t days[] = {31,28,31,30,31,30,31,31,30,31,30,31};

Date::Date(uint16_t year, uint8_t month, uint8_t day) {
    this->year = year;
    this->month = month;
    this->day = day;
}

void Date::addDay() {
    if(day + 1 > days[month-1]) {
      month += 1;
      day = 1;

      if (month > 12) {
          month = 1;
          year += 1;
      }
    }
    else {
        day += 1;
    }
}

void Date::subDay() {
    if(day - 1 < 1) {
      month -= 1;
      day = days[month-1];

      if (month < 1) {
          month = 12;
          year -= 1;
      }
    }
    else {
        day -= 1;
    }
}

String Date::toString() {
    String month_str;
    String day_str;

    if(month < 10) {
        month_str = "0" + String(month);
    }
    else {
        month_str = String(month);
    }

    if(day < 10) {
        day_str = "0" + String(day);
    }
    else {
        day_str = String(day);
    }

    return String(year) + "-" + month_str + "-" + day_str;
}