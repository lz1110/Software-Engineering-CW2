package specs.acmetelecom;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import com.acmetelecom.IBillGenerator;
import com.acmetelecom.ICustomerDatabase;
import org.concordion.integration.junit4.ConcordionRunner;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
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


  /*      BillingSystem billingSystem = new BillingSystem();
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
        });*/


		BillingSystem billingSystem = new BillingSystem();
        billingSystem.setCustomers(mockCustomers);

        final int YEAR = 2013;
        final int MONTH = 11;
        final int DAY = 11;

        freeze(new DateTime(YEAR, MONTH, DAY, 19, 00, 00));
		billingSystem.callInitiated("447722113434", "447070372938");

        freeze(new DateTime(YEAR, MONTH, DAY+1, 6, 59, 59));
		billingSystem.callCompleted("447722113434", "447070372938");

        unfreeze();
		billingSystem.createCustomerBills();

        return byteOutput.toString();
	}

    public static void freeze(DateTime frozenDateTime) {
        DateTimeUtils.setCurrentMillisFixed(frozenDateTime.getMillis());
    }

    public static void unfreeze() {
        DateTimeUtils.setCurrentMillisSystem();
    }

    // creates mock customer list
    private List<Customer> getMockCustomers() {
        final List<Customer> mockCustomers = new ArrayList<Customer>();
        mockCustomers.add(new Customer("customer1", "447722113434","Standard"));
        mockCustomers.add(new Customer("customer2", "447712312312","Business"));
        mockCustomers.add(new Customer("customer3", "447010102020","Leisure"));
        return mockCustomers;
    }
}
