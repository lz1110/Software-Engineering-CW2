package com.acmetelecom;

import com.acmetelecom.call.*;
import com.acmetelecom.customer.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class BillingSystem {
    private ICallEventFactory callFactory;
    private List<CallEvent> callLog = new ArrayList<CallEvent>();
    private List<Customer> customers;

    public BillingSystem(){
        callFactory = new CallEventFactory();
        // TODO either make it static class, or move the construction to somewhere else
    }

    public void callInitiated(String caller, String callee) {
        callLog.add(callFactory.createCallEvent(caller, callee, CallEventType.START));
    }

    public void callCompleted(String caller, String callee) {
        callLog.add(callFactory.createCallEvent(caller, callee, CallEventType.END));
    }

    
    public void createCustomerBills() {
    	List<Customer> customers = getCustomers();
        for (Customer customer : customers) {
            createBillFor(customer);
        }
        callLog.clear();
    }

    private void createBillFor(Customer customer) {
        List<CallEvent> customerEvents = new ArrayList<CallEvent>();
        for (CallEvent callEvent : callLog) {
            if (callEvent.getCaller().equals(customer.getPhoneNumber())) {
                customerEvents.add(callEvent);
            }
        }

        List<Call> calls = new ArrayList<Call>();

        CallEvent start = null;
        for (CallEvent event : customerEvents) {
            if (event.getType() == CallEventType.START) {
                start = event;
            } else if (event.getType() == CallEventType.END && start != null) {
                calls.add(new Call(start, event));
                start = null;
            }
        }

        BigDecimal totalBill = new BigDecimal(0);
        List<LineItem> items = new ArrayList<LineItem>();

        for (Call call : calls) {

            Tariff tariff = CentralTariffDatabase.getInstance().tarriffFor(customer); //TODO make into interface

            BigDecimal cost;

            DaytimePeakPeriod peakPeriod = new DaytimePeakPeriod();
            if (peakPeriod.offPeak(call.startTime()) && peakPeriod.offPeak(call.endTime()) && call.durationSeconds() < 12 * 60 * 60) {
                cost = new BigDecimal(call.durationSeconds()).multiply(tariff.offPeakRate());
            } else {
                cost = new BigDecimal(call.durationSeconds()).multiply(tariff.peakRate());
            }

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

    ICustomerDatabase customerDatabase;
    public ICustomerDatabase getCustomerDatabase() {
        if (customerDatabase == null)
            customerDatabase = new CustomerDatabase();
        return customerDatabase;
    }
    public void setCustomerDatabase(ICustomerDatabase customerDatabase) {
        this.customerDatabase = customerDatabase;
    }

    private IBillGenerator billGenerator;
    public IBillGenerator getBillGenerator() {
        if (billGenerator == null)
            billGenerator = new BillGenerator();
        return billGenerator;
    }
    public void setBillGenerator(IBillGenerator billGenerator) {
        this.billGenerator = billGenerator;
    }

    static class LineItem {
        private Call call;
        private BigDecimal callCost;

        public LineItem(Call call, BigDecimal callCost) {
            this.call = call;
            this.callCost = callCost;
        }

        public String date() {
            return call.date();
        }

        public String callee() {
            return call.callee();
        }

        public String durationMinutes() {
            return "" + call.durationSeconds() / 60 + ":" + String.format("%02d", call.durationSeconds() % 60);
        }

        public BigDecimal cost() {
            return callCost;
        }
    }
}
