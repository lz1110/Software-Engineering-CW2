package com.acmetelecom;

import java.util.List;

import com.acmetelecom.customer.CentralCustomerDatabase;
import com.acmetelecom.customer.Customer;

public class CustomerDatabase implements ICustomerDatabase {
	@Override
	public List<Customer> getCustomers() {
		return CentralCustomerDatabase.getInstance().getCustomers();
	}
}
