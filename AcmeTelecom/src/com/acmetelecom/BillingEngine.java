package com.acmetelecom;

import com.acmetelecom.Interfaces.IBillCalculator;
import com.acmetelecom.Interfaces.IBillGenerator;
import com.acmetelecom.Interfaces.ICustomerDatabase;
import com.acmetelecom.Interfaces.ITariffDatabase;
import com.acmetelecom.Utils.LineItem;
import com.acmetelecom.Utils.MoneyFormatter;
import com.acmetelecom.call.Call;
import com.acmetelecom.call.CallEvent;
import com.acmetelecom.call.CallEventType;
import com.acmetelecom.call.ICallEventFactory;
import com.acmetelecom.customer.Customer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BillingEngine {
    private ICustomerDatabase customerDatabase;
    private ITariffDatabase tariffDatabase;
    private IBillGenerator billGenerator;
    private IBillCalculator billCalculator;
    private ICallEventFactory callEventFactory;

    // Setters
    public void setCustomerDatabase(ICustomerDatabase customerDatabase) {
        this.customerDatabase = customerDatabase;
    }

    public void setBillGenerator(IBillGenerator billGenerator) {
        this.billGenerator = billGenerator;
    }

    public void setTariffDatabase(ITariffDatabase tariffDatabase) {
        this.tariffDatabase = tariffDatabase;
    }

    public void setBillCalculator(IBillCalculator billCalculator){
        this.billCalculator = billCalculator;
    }

    public void setCallEventFactory(ICallEventFactory callEventFactory) {
        this.callEventFactory = callEventFactory;
    }


    //
    private Map<String,List<CallEvent>> callLog = new HashMap<String, List<CallEvent>>();
    private List<Customer> customers;

    public BillingEngine(ICallEventFactory callEventFactory, IBillGenerator billGenerator,
                         IBillCalculator billCalculator, ICustomerDatabase customerDatabase,
                         ITariffDatabase tariffDatabase){
        this.callEventFactory = callEventFactory;
        this.billGenerator = billGenerator;
        this.billCalculator = billCalculator;
        this.customerDatabase = customerDatabase;
        this.tariffDatabase = tariffDatabase;
    }

    public void callInitiated(String caller, String callee) {
        // store the event in a caller-specific list
        List<CallEvent> eventList = callLog.get(caller);
        if (eventList == null)
            eventList = new ArrayList<CallEvent>();
        eventList.add(callEventFactory.createCallEvent(caller, callee, CallEventType.START));
        callLog.put(caller, eventList);
    }

    public void callCompleted(String caller, String callee) {
        // store the event in a caller-specific list
        List<CallEvent> eventList = callLog.get(caller);
        if (eventList == null)
            eventList = new ArrayList<CallEvent>();
        eventList.add(callEventFactory.createCallEvent(caller, callee, CallEventType.END));
        callLog.put(caller, eventList);
    }


    public void createCustomerBills() {
        List<Customer> customers = customerDatabase.getCustomers();
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
            BigDecimal cost = billCalculator.calculate(call,tariffDatabase.peakRate(customer),tariffDatabase.offPeakRate(customer));
            cost = cost.setScale(0, RoundingMode.HALF_UP);
            BigDecimal callCost = cost;
            totalBill = totalBill.add(callCost);
            items.add(new LineItem(call, callCost));
        }

        billGenerator.send(customer, items, MoneyFormatter.penceToPounds(totalBill));
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
