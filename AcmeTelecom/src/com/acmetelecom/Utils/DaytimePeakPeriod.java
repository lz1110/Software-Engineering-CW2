package com.acmetelecom.Utils;

import org.joda.time.DateTime;

import java.util.Calendar;
import java.util.Date;

public class DaytimePeakPeriod {

    public boolean offPeak(DateTime time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time.toDate());
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        return hour < 7 || hour >= 19;
    }
}
