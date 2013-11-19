package com.acmetelecom;

import com.acmetelecom.Implementations.*;
import com.acmetelecom.Interfaces.IBillCalculator;
import com.acmetelecom.Interfaces.IBillGenerator;
import com.acmetelecom.Interfaces.ICustomerDatabase;
import com.acmetelecom.Interfaces.ITariffDatabase;
import com.acmetelecom.Utils.LineItem;
import com.acmetelecom.Utils.MoneyFormatter;
import com.acmetelecom.call.*;
import com.acmetelecom.customer.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class BillingSystem {
    private ICallEventFactory callFactory;
    private ICustomerDatabase customerDatabase;
    private ITariffDatabase tariffDatabase;
	private IBillGenerator billGenerator;
	private IBillCalculator billCalculator;

    // Getter (private auxiliary) and Setters
    private ICustomerDatabase getCustomerDatabase() {
        if (customerDatabase == null)
            customerDatabase = new com.acmetelecom.Implementations.CustomerDatabase();
        return customerDatabase;
    }
    public void setCustomerDatabase(ICustomerDatabase customerDatabase) {
        this.customerDatabase = customerDatabase;
    }

    private IBillGenerator getBillGenerator() {
        if (billGenerator == null)
            billGenerator = new BillGenerator();
        return billGenerator;
    }
    public void setBillGenerator(IBillGenerator billGenerator) {
        this.billGenerator = billGenerator;
    }
    
    private ITariffDatabase getTariffDatabase() {
		if (tariffDatabase == null)
			tariffDatabase = new TariffDatabase(); 
	    return tariffDatabase;
	}
	public void setTariffDatabase(ITariffDatabase tariffDatabase) {
		this.tariffDatabase = tariffDatabase;
	}
    
	
    // The BillingSystem starts here
    private Map<String,List<CallEvent>> callLog = new HashMap<String, List<CallEvent>>();
    //private List<CallEvent> callLog = new ArrayList<CallEvent>();
    private List<Customer> customers;
    
    public BillingSystem(){
        callFactory = new CallEventFactory();
        billCalculator = new BillCalculator();
        // TODO either make it static class, or move the construction to somewhere else
    }
    
    public void callInitiated(String caller, String callee) {
        // store the event in a caller-specific list
        List<CallEvent> eventList = callLog.get(caller); 
        if (eventList == null)
            eventList = new ArrayList<CallEvent>();
        eventList.add(callFactory.createCallEvent(caller, callee, CallEventType.START));
        callLog.put(caller, eventList);
    }

    public void callCompleted(String caller, String callee) {
        // store the event in a caller-specific list
    	List<CallEvent> eventList = callLog.get(caller); 
        if (eventList == null)
            eventList = new ArrayList<CallEvent>();
        eventList.add(callFactory.createCallEvent(caller, callee, CallEventType.END));
        callLog.put(caller, eventList);
    }

    
    public void createCustomerBills() {
    	List<Customer> customers = getCustomers();
        for (Customer customer : customers) {
            createBillFor(customer);
        }
        callLog.clear();
    }

    private void createBillFor(Customer customer) {
    	// Format the call log for the customer.
        List<CallEvent> customerEvents = callLog.get(customer.getPhoneNumber());
        List<Call> calls = callFormat(customerEvents);
        
        // Calculate the cost.
        BigDecimal totalBill = new BigDecimal(0);
        List<LineItem> items = new ArrayList<LineItem>();

        for (Call call : calls) {
        	// Calculation is done inside the billCalculator
            BigDecimal cost = billCalculator.calculate(call,getTariffDatabase().peakRate(customer),getTariffDatabase().offPeakRate(customer));
            cost = cost.setScale(0, RoundingMode.HALF_UP);
            BigDecimal callCost = cost;
            totalBill = totalBill.add(callCost);
            items.add(new LineItem(call, callCost));
        }

        getBillGenerator().send(customer, items, MoneyFormatter.penceToPounds(totalBill));
    }


    public void setCustomers(List<Customer> customers){
        this.customers = customers;
    }

    public List<Customer> getCustomers() {
        if(customers == null)
            customers = getCustomerDatabase().getCustomers();
        return customers;
    }
    
    private List<Call> callFormat(List<CallEvent> eventLog) {
    	List<Call> callLog = new ArrayList<Call>();
    	
    	if (eventLog != null) {
    		CallEvent start = null;
        	for (CallEvent event : eventLog) {
        		if (event.getType() == CallEventType.START) {
        			start = event;
        		} else if (event.getType() == CallEventType.END && start != null) {
        			callLog.add(new Call(start, event));
        			start = null;
        		}
        	}
    	}
        return callLog;
    }
}
