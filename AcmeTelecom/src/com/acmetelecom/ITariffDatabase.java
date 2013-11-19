package com.acmetelecom;

import java.math.BigDecimal;

import com.acmetelecom.customer.Customer;

public interface ITariffDatabase {
	public abstract BigDecimal peakRate(Customer customer);
	public abstract BigDecimal offPeakRate(Customer customer);
}