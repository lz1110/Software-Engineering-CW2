package specs.acmetelecom;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import org.concordion.integration.junit4.ConcordionRunner;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.runner.RunWith;
import com.acmetelecom.BillingSystem;
import com.acmetelecom.customer.Customer;

@RunWith(ConcordionRunner.class)
public class BillingSystemFixture {
	
	private Mockery mockingContext = new Mockery();
	
	public String testSimpleOffPeakRate() throws InterruptedException {

        final List<Customer> mockCustomers = getMockCustomers();

        // set custom output stream
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(byteOutput));

		BillingSystem billingSystem = new BillingSystem();
        billingSystem.setCustomers(mockCustomers);
		
		billingSystem.callInitiated("447722113434", "447070372938");
		sleepSeconds(5);
		billingSystem.callCompleted("447722113434", "447070372938");
		billingSystem.createCustomerBills();


        return byteOutput.toString();
	}


    // creates mock customer list
    private List<Customer> getMockCustomers() {
        final List<Customer> mockCustomers = new ArrayList<Customer>();
        mockCustomers.add(new Customer("customer1", "447722113434","Standard"));
        mockCustomers.add(new Customer("customer2", "447712312312","Business"));
        mockCustomers.add(new Customer("customer3", "447010102020","Leisure"));
        return mockCustomers;
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
