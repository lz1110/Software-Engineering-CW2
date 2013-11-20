package com.acmetelecom.Implementations;

import java.math.BigDecimal;

import com.acmetelecom.Interfaces.IBillCalculator;
import com.acmetelecom.Utils.DaytimePeakPeriod;
import com.acmetelecom.call.Call;

public class OldBillCalculator implements IBillCalculator{

	public BigDecimal calculate(Call call, BigDecimal peakRate, BigDecimal offPeakRate) {
		BigDecimal cost;
		DaytimePeakPeriod peakPeriod = new DaytimePeakPeriod();
		if (peakPeriod.offPeak(call.startTime()) && peakPeriod.offPeak(call.endTime()) && call.durationSeconds() < 12*60*60){
			cost = new BigDecimal(call.durationSeconds()).multiply(offPeakRate);
		} else {
			cost = new BigDecimal(call.durationSeconds()).multiply(peakRate);
		}
		
		return cost;
	}

}
