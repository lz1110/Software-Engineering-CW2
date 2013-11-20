package com.acmetelecom;

import com.acmetelecom.Implementations.BillCalculator;
import com.acmetelecom.Implementations.BillGenerator;
import com.acmetelecom.Implementations.CustomerDatabase;
import com.acmetelecom.Implementations.OldBillCalculator;
import com.acmetelecom.Implementations.TariffDatabase;
import com.acmetelecom.Interfaces.IBillCalculator;
import com.acmetelecom.Interfaces.IBillGenerator;
import com.acmetelecom.Interfaces.ICustomerDatabase;
import com.acmetelecom.Interfaces.ITariffDatabase;
import com.acmetelecom.call.CallEventFactory;
import com.acmetelecom.call.ICallEventFactory;

/**
 *  BillingSystem class manages instantiation of concrete classes for BillingEngine.
 *  Provides a window from outside to use the BillingEngine functionality.
 */
public class BillingSystem {
    private BillingEngine billingEngine;

    // public setters
    public void setCustomerDatabase(ICustomerDatabase customerDatabase) {
        billingEngine.setCustomerDatabase(customerDatabase);
    }

    public void setBillGenerator(IBillGenerator billGenerator) {
        billingEngine.setBillGenerator(billGenerator);
    }

	public void setTariffDatabase(ITariffDatabase tariffDatabase) {
        billingEngine.setTariffDatabase(tariffDatabase);
    }

    // Constructor instantiates class objects
    public BillingSystem(){

        // Instantiates concrete classes and injects them into BillingEngine
        ICallEventFactory callEventFactory = new CallEventFactory();
        IBillGenerator billGenerator = new BillGenerator();
        IBillCalculator billCalculator = new BillCalculator();
        ICustomerDatabase customerDatabase = new CustomerDatabase();
        ITariffDatabase tariffDatabase = new TariffDatabase();

        billingEngine = new BillingEngine(callEventFactory, billGenerator,
                billCalculator, customerDatabase, tariffDatabase);
    }

    // Access methods
    public void callInitiated(String caller, String callee) {
        billingEngine.callInitiated(caller, callee);
    }

    public void callCompleted(String caller, String callee) {
        billingEngine.callCompleted(caller, callee);
    }

    public void createCustomerBills() {
        billingEngine.createCustomerBills();
    }
}
