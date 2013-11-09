package specs.acmetelecom;

import java.util.ArrayList;
import java.util.List;
import org.concordion.integration.junit4.ConcordionRunner;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.api.Invocation;
import org.jmock.lib.action.CustomAction;
import org.junit.runner.RunWith;



import com.acmetelecom.BillGenerator;
import com.acmetelecom.BillingSystem;
import com.acmetelecom.CallEvent;
import com.acmetelecom.IBillGenerator;
import com.acmetelecom.ICustomerDatabase;
import com.acmetelecom.Printer;
import com.acmetelecom.customer.CentralCustomerDatabase;
import com.acmetelecom.customer.CentralTariffDatabase;
import com.acmetelecom.customer.Customer;

@RunWith(ConcordionRunner.class)
public class BillingSystemFixture {
	
	private Mockery mockingContext = new Mockery();
	
	public String testMock() throws InterruptedException {
		final List<Customer> mockCustomers = new ArrayList<Customer>();
    	mockCustomers.add(new Customer("customer1", "1","Standard"));
    	mockCustomers.add(new Customer("customer2", "2","Business"));
    	mockCustomers.add(new Customer("customer3", "3","Leisure"));
    	
		BillingSystem billingSystem = new BillingSystem();
		final IBillGenerator mockBillGenerator = mockingContext.mock(IBillGenerator.class);
		final ICustomerDatabase mockCustomerDatabase = mockingContext.mock(ICustomerDatabase.class);
		billingSystem.setBillGenerator(mockBillGenerator);
		billingSystem.setCustomerDatabase(mockCustomerDatabase);
		
		mockingContext.checking(new Expectations() {
			{
				oneOf(mockCustomerDatabase).getCustomers(); will(returnValue(mockCustomers));
			}
		});
		
		
		mockingContext.checking(new Expectations() {
			{
				oneOf(mockBillGenerator).send(with(same(mockCustomers.get(1))), with(any(List.class)),with(equal("0.01")));
				allowing(mockBillGenerator).send(with(any(Customer.class)), with(any(List.class)), with(any(String.class)));	
			}
		});
		
		System.out.println("Running...");
		
		billingSystem.callInitiated("2", "2");
		sleepSeconds(5);
		billingSystem.callCompleted("2", "2");
		billingSystem.createCustomerBills();
		
       mockingContext.assertIsSatisfied();
       return "requirements were met";
       
	}
	
	private static void sleepSeconds(int n) throws InterruptedException {
		Thread.sleep(n*1000);
	}
	
	/*
    public static void freeze(DateTime frozenDateTime) {
        DateTimeUtils.setCurrentMillisFixed(frozenDateTime.getMillis());
    }
    
    public static void unfreeze() {
        DateTimeUtils.setCurrentMillisSystem();
    }
    */
}
