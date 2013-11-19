package com.acmetelecom;

import java.math.BigDecimal;

import com.acmetelecom.customer.CentralTariffDatabase;
import com.acmetelecom.customer.Customer;
import com.acmetelecom.customer.Tariff;

public class TariffDatabase implements ITariffDatabase {
	public BigDecimal offPeakRate(Customer customer) {
		return CentralTariffDatabase.getInstance().tarriffFor(customer).offPeakRate();
	}
	
	public BigDecimal peakRate(Customer customer) {
		return CentralTariffDatabase.getInstance().tarriffFor(customer).peakRate();
	}
}
