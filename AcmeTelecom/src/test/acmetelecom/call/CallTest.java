package test.acmetelecom.call;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import java.math.BigDecimal;
import com.acmetelecom.Implementations.BillCalculator;
import com.acmetelecom.call.Call;
import com.acmetelecom.call.CallEnd;
import com.acmetelecom.call.CallStart;
import com.googlecode.zohhak.api.Coercion;
import com.googlecode.zohhak.api.TestWith;
import com.googlecode.zohhak.api.runners.ZohhakRunner;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

@RunWith(ZohhakRunner.class)
public class CallTest {    
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@TestWith({
		   "'11/11/2013 19:00:00', '12/11/2013 06:59:59', 43199",
		   "'11/11/2013 19:00:00', '11/11/2013 19:00:00', 0",
		})
	public void testDurationSeconds (String startTime, String endTime, int expectedResult) {
		DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss");
		freeze(formatter.parseDateTime(startTime));
		CallStart startEvent = new CallStart("Caller", "Callee");
		freeze(formatter.parseDateTime(endTime));
		CallEnd endEvent = new CallEnd("Caller", "Callee");
		unfreeze();
		Call tester = new Call(startEvent, endEvent);
		int result = tester.durationSeconds();
	    assertEquals(result,expectedResult);
	}
	
	@Coercion
	public BigDecimal whateverName (String input) {
		return new BigDecimal(input);
	}
	
    private void freeze(DateTime frozenDateTime) {DateTimeUtils.setCurrentMillisFixed(frozenDateTime.getMillis());}
    private void unfreeze() {DateTimeUtils.setCurrentMillisSystem();}

}

