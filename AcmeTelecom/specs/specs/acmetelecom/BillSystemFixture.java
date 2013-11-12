package specs.acmetelecom;

import java.util.ArrayList;
import java.util.List;

import org.concordion.integration.junit4.ConcordionRunner;
import org.jmock.Mockery;
import org.junit.runner.RunWith;

import com.acmetelecom.BillingSystem;
import com.acmetelecom.IBillGenerator;
import com.acmetelecom.ICustomerDatabase;
import com.acmetelecom.customer.Customer;

import org.jmock.Expectations;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

@RunWith(ConcordionRunner.class)
public class BillSystemFixture {
	private Mockery mockingContext;
	private IBillGenerator mockBillGenerator;
    private ICustomerDatabase mockCustomerDatabase;
	private String bill;
	private Customer caller;
    private DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss");
    private DateTime startTime;
    private DateTime endTime;

	
	private final Customer testCallee = new Customer("Test Callee", "Test Callee's Phone Number", "Test Callee");
	private final List<Customer> mockCustomers = new ArrayList<Customer> () {{
		add(new Customer("Standard Caller 1", "Standard Caller 1's Phone Number","Standard"));
		add(new Customer("Business Caller 1", "Business Caller 1's Phone Number","Business"));
		add(new Customer("Business Caller 2", "Business Caller 2's Phone Number","Business"));
		add(new Customer("Leisure Caller 1", "Leisure Caller 1's Phone Number","Leisure"));
		add(new Customer("Standard", "Standard Caller's Phone Number","Standard"));
		add(new Customer("Business", "Business Caller's Phone Number","Business"));
		add(new Customer("Leisure", "Leisure Caller's Phone Number","Leisure"));
		add(testCallee);
	}};
	
	private Customer lookUpCustomer (List<Customer> customers, String customerType) {
		for (Customer c: customers) {
			if (c.getFullName().equals(customerType)) {
				return c;
			}
		}
		return null;
	}
	
    private void freeze(DateTime frozenDateTime) {DateTimeUtils.setCurrentMillisFixed(frozenDateTime.getMillis());}
    private void unfreeze() {DateTimeUtils.setCurrentMillisSystem();}	
    
	public Boolean testCallRate(String customerName, String callStartTime, String callEndTime, String callersToTest, String expectedBill) {
		
		// Before the test, set up all mocks
		mockingContext = new Mockery();
		mockBillGenerator = mockingContext.mock(IBillGenerator.class);
		mockCustomerDatabase = mockingContext.mock(ICustomerDatabase.class);
		BillingSystem billingSystem = new BillingSystem();
		billingSystem.setBillGenerator(mockBillGenerator);
        billingSystem.setCustomerDatabase(mockCustomerDatabase);
		
		// Split parameters to support multiple calls
		String [] customerNames = customerName.split("AND");
		String [] callStartTimes = callStartTime.split("AND");
		String [] callEndTimes = callEndTime.split("AND");
		String [] expectedBills = expectedBill.split("AND");
		String [] allCallers = callersToTest.split("AND");

		mockingContext.checking(new Expectations() {
			{
				allowing(mockCustomerDatabase).getCustomers(); will(returnValue(mockCustomers));
			}
		});
		
		
		for (int i = 0; i<customerNames.length; i++){
			startTime = formatter.parseDateTime(callStartTimes[i]);
	        endTime = formatter.parseDateTime(callEndTimes[i]);
			caller = lookUpCustomer(mockCustomers, customerNames[i]);
	        // Simulate the call
	        freeze(startTime);
			billingSystem.callInitiated(caller.getPhoneNumber(), testCallee.getPhoneNumber());
	        freeze(endTime);
			billingSystem.callCompleted(caller.getPhoneNumber(), testCallee.getPhoneNumber());
	        unfreeze();
		}
		
		for (int i = 0; i<allCallers.length; i++){
			caller = lookUpCustomer(mockCustomers, allCallers[i]);
			bill = expectedBills[i];
			// Add mock expectations
	        mockingContext.checking(new Expectations() {
	            {
	                oneOf(mockBillGenerator).send(with(same(caller)), with(any(List.class)),with(equal(bill)));
	            }
	        });
		}
		
		
		mockingContext.checking(new Expectations() {
            {
            	allowing(mockBillGenerator).send(with(any(Customer.class)), with(any(List.class)), with(any(String.class)));
            }
		});
        
		billingSystem.createCustomerBills();
		mockingContext.assertIsSatisfied();
		return true;
	}
}
