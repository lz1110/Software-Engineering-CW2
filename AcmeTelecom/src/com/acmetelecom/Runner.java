package com.acmetelecom;

import java.util.List;

import com.acmetelecom.customer.CentralCustomerDatabase;
import com.acmetelecom.customer.Customer;

public class Runner {
	public static void main(String[] args) throws Exception {
		System.out.println("Running...");
		BillingSystem billingSystem = new BillingSystem();
		billingSystem.callInitiated("447722113434", "447766511332");
		sleepSeconds(5);
		billingSystem.callCompleted("447722113434", "447766511332");
		billingSystem.createCustomerBills();	
	}
	private static void sleepSeconds(int n) throws InterruptedException {
		Thread.sleep(n*1000);
	}

}


