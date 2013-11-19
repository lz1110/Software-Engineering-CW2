package com.acmetelecom.Implementations;

import java.math.BigDecimal;

import com.acmetelecom.Interfaces.ITariffDatabase;
import com.acmetelecom.customer.CentralTariffDatabase;
import com.acmetelecom.customer.Customer;

public class TariffDatabase implements ITariffDatabase {
	public BigDecimal offPeakRate(Customer customer) {
		return CentralTariffDatabase.getInstance().tarriffFor(customer).offPeakRate();
	}
	
	public BigDecimal peakRate(Customer customer) {
		return CentralTariffDatabase.getInstance().tarriffFor(customer).peakRate();
	}
}
