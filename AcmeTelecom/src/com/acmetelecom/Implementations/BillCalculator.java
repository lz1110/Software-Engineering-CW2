package com.acmetelecom.Implementations;

import java.util.Date;

import com.acmetelecom.Interfaces.IBillCalculator;
import com.acmetelecom.Utils.DaytimePeakPeriod;
import org.joda.time.DateTime;
import java.math.BigDecimal;

import com.acmetelecom.call.Call;

public class BillCalculator implements IBillCalculator {
    
    public BigDecimal calculate(Call call, BigDecimal peakRate, BigDecimal offPeakRate) {

        DaytimePeakPeriod peakPeriod = new DaytimePeakPeriod();
        boolean startOff = peakPeriod.offPeak(call.startTime());
        boolean endOff = peakPeriod.offPeak(call.endTime());
        boolean shortCall = call.durationSeconds()<12*60*60;

        long offPeak = 0;
        long peak = 0;
        BigDecimal cost;

        if (startOff && endOff && shortCall) {
                offPeak = call.durationSeconds();
        }
        else if (startOff && endOff && !shortCall) {
                peak = 12*60*60;
                offPeak = call.durationSeconds() - peak;
        }
        else if (!startOff && !endOff && shortCall) {
                peak = call.durationSeconds();
                offPeak = 0;
        }
        else if (!startOff && !endOff && !shortCall) {
                offPeak = 12*60*60;
                peak = call.durationSeconds()-offPeak;
        }
        else if (startOff && !endOff) {
                peak = ( call.endTime().getMillis() - peakStartOn(call.endTime()) )/1000;
                offPeak = call.durationSeconds() - peak;
        }
        else if (!startOff && endOff) {
                peak = ( peakEndOn(call.startTime()) - call.startTime().getMillis()) / 1000;
                offPeak = call.durationSeconds() - peak;
        }

            BigDecimal peakCharge = BigDecimal.valueOf(peak).multiply(peakRate);
            BigDecimal offPeakCharge = BigDecimal.valueOf(offPeak).multiply(offPeakRate);
            cost = peakCharge.add(offPeakCharge);
    
            return cost;
        }
	
	private long peakStartOn(DateTime date) {
		return date.getMillis()-date.getMillisOfDay()+7*60*60*1000;
	}
	
	private long peakEndOn(DateTime date) {
		return date.getMillis()-date.getMillisOfDay()+19*60*60*1000;
	}
	
}



	





