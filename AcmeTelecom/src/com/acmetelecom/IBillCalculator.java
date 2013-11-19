package com.acmetelecom;

import java.math.BigDecimal;
import com.acmetelecom.call.Call;

public interface IBillCalculator {

	public BigDecimal calculate(Call call, BigDecimal peakRate, BigDecimal offPeakRate);
	
}