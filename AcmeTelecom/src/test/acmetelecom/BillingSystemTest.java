package test.acmetelecom;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import java.math.BigDecimal;

import com.acmetelecom.BillingSystem;
import com.googlecode.zohhak.api.Coercion;
import com.googlecode.zohhak.api.TestWith;
import com.googlecode.zohhak.api.runners.ZohhakRunner;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;

@RunWith(ZohhakRunner.class)
public class BillingSystemTest {
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@TestWith({
		   "caller, callee, 1"
		})
	public void testCallInitiated (String caller, String callee, int expectedResult) {
		BillingSystem tester = new BillingSystem();
		int sizeBefore = tester.getCallLog().size();
		tester.callInitiated(caller, callee);		
		int sizeAfter = tester.getCallLog().size();
	    assertEquals(sizeAfter-sizeBefore,expectedResult);
	}
	
	@TestWith({
		   "caller, callee, 1"
		})
	public void testCallCompleted (String caller, String callee, int expectedResult) {
		BillingSystem tester = new BillingSystem();
		int sizeBefore = tester.getCallLog().size();
		tester.callInitiated(caller, callee);		
		int sizeAfter = tester.getCallLog().size();
	    assertEquals(sizeAfter-sizeBefore,expectedResult);
	}
	
	@Coercion
	public BigDecimal whateverName (String input) {
		return new BigDecimal(input);
	}
	

}
