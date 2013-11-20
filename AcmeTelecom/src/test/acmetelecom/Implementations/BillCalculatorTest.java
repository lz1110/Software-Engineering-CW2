package test.acmetelecom.Implementations;

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
public class BillCalculatorTest {    
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@TestWith({
		   "'11/11/2013 19:00:00', '12/11/2013 06:59:59', 20, 10, 431990",
		   "'11/11/2013 06:59:59', '11/11/2013 19:00:00', 20, 10, 864010",
		   "'11/11/2013 06:59:59', '12/11/2013 06:59:59', 20, 10, 1296000",
		   
		   "'11/11/2013 07:00:00', '11/11/2013 18:59:59', 20, 10, 863980",
		   "'11/11/2013 18:59:59', '12/11/2013 07:00:00', 20, 10, 432020",
		   
		   "'11/11/2013 06:59:59', '11/11/2013 07:00:00', 20, 10, 10",
		   "'11/11/2013 06:59:59', '11/11/2013 08:00:00', 20, 10, 72010",
		   "'11/11/2013 06:59:59', '11/11/2013 18:59:59', 20, 10, 863990",
		   "'11/11/2013 19:00:00', '12/11/2013 07:00:00', 20, 10, 432000",
		   "'11/11/2013 19:00:00', '12/11/2013 18:59:59', 20, 10, 1295980",
		   
		   "'11/11/2013 18:59:59', '11/11/2013 19:00:00', 20, 10, 20",
		   "'11/11/2013 18:59:59', '11/11/2013 20:00:00', 20, 10, 36020",
		   "'11/11/2013 18:59:59', '12/11/2013 06:59:59', 20, 10, 432010",
		   "'11/11/2013 07:00:00', '11/11/2013 19:00:00', 20, 10, 864000",
		   "'11/11/2013 07:00:00', '12/11/2013 06:59:59', 20, 10, 1295990",
		})
	public void testCalculate (String startTime, String endTime, BigDecimal peakRate, BigDecimal offPeakRate, BigDecimal expectedResult) {
		DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss");
		freeze(formatter.parseDateTime(startTime));
		CallStart startEvent = new CallStart("Caller", "Callee");
		freeze(formatter.parseDateTime(endTime));
		CallEnd endEvent = new CallEnd("Caller", "Callee");
		unfreeze();
		Call call = new Call(startEvent, endEvent);		
		BillCalculator tester = new BillCalculator();
	    BigDecimal result = tester.calculate(call, peakRate, offPeakRate);
	    assertTrue(result.equals(expectedResult));
	}
	

	@Coercion
	public BigDecimal whateverName (String input) {
		return new BigDecimal(input);
	}
	
    private void freeze(DateTime frozenDateTime) {DateTimeUtils.setCurrentMillisFixed(frozenDateTime.getMillis());}
    private void unfreeze() {DateTimeUtils.setCurrentMillisSystem();}

}
