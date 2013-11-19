package com.acmetelecom.Implementations;

import java.util.Date;

import com.acmetelecom.Interfaces.IBillCalculator;
import com.acmetelecom.Utils.DaytimePeakPeriod;
import org.joda.time.DateTime;
import java.math.BigDecimal;
import java.math.MathContext;

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
        	peak = ( call.endTime().getTime() - peakStartOn(call.endTime()) )/1000;
        	offPeak = call.durationSeconds() - peak;
        } 
        else if (!startOff && endOff) {
        	peak = ( peakEndOn(call.startTime()) - call.startTime().getTime()) / 1000;
        	offPeak = call.durationSeconds() - peak;
        }
        MathContext mc = new MathContext(4);
		BigDecimal peakCharge = BigDecimal.valueOf(peak).multiply(peakRate,mc);
		BigDecimal offPeakCharge = BigDecimal.valueOf(offPeak).multiply(offPeakRate,mc);
		cost = peakCharge.add(offPeakCharge);
        
		return cost;
	}
	
	
	private long peakStartOn(Date date) {
		DateTime joda = new DateTime(date);
		return joda.getMillis()-joda.getMillisOfDay()+7*60*60*1000;
	}
	
	private long peakEndOn(Date date) {
		DateTime joda = new DateTime(date);
		return joda.getMillis()-joda.getMillisOfDay()+19*60*60*1000;
	}
	
}



	




