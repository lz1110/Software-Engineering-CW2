package com.acmetelecom.Implementations;

import java.util.Date;

import com.acmetelecom.Interfaces.IBillCalculator;
import com.acmetelecom.Utils.DaytimePeakPeriod;
import org.joda.time.DateTime;
import java.math.BigDecimal;

import com.acmetelecom.call.Call;

public class BillCalculator implements IBillCalculator {
	
	public BigDecimal calculate(Call call, BigDecimal peakRate, BigDecimal offPeakRate) {
		BigDecimal peakDuration = getPeakDuration(call);
        BigDecimal offPeakDuration = getOffPeakDuration(call);
		return peakDuration.multiply(peakRate).add(offPeakDuration.multiply(offPeakRate));
	}
	
	public BigDecimal getPeakDuration (Call call) {
        DaytimePeakPeriod peakPeriod = new DaytimePeakPeriod();
        boolean startInOffPeak = peakPeriod.offPeak(call.startTime());
        boolean endInOffPeak = peakPeriod.offPeak(call.endTime());
        boolean callLessTwelveHr = call.durationSeconds()<12*60*60;
		long peakDuration = 0;
		
        if (startInOffPeak && endInOffPeak && callLessTwelveHr) {
        	peakDuration = 0;
        }
        else if (startInOffPeak && endInOffPeak && !callLessTwelveHr) {
        	peakDuration = 12*60*60;
        }
        else if (!startInOffPeak && !endInOffPeak && callLessTwelveHr) {
        	peakDuration = call.durationSeconds();
        }
        else if (!startInOffPeak && !endInOffPeak && !callLessTwelveHr) {
        	peakDuration = call.durationSeconds()-12*60*60;
        }
        else if (startInOffPeak && !endInOffPeak) {       
        	peakDuration = ( call.endTime().getTime() - peakStartOn(call.endTime()) )/1000;
        } 
        else if (!startInOffPeak && endInOffPeak) {
        	peakDuration = ( peakEndOn(call.startTime()) - call.startTime().getTime()) / 1000;
        }
		return new BigDecimal(peakDuration);
	}
	
	public BigDecimal getOffPeakDuration (Call call) {
		return new BigDecimal(call.durationSeconds()).min(getPeakDuration(call));
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



	





