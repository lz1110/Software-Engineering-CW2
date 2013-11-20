package test.acmetelecom.call;

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
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;


import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

@RunWith(ZohhakRunner.class)
public class CallTest {    
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

    @TestWith({
            "'440011223344', '4400000000'",
            "'444433221100', '4400000000'",
            "'caller', 'callee'"
    })
    public void testCallee(String caller, String callee){
        // should return the callee of start time
        Call call = new Call(new CallStart(callee, callee), new CallEnd("caller", "callee"));
        assertEquals(call.callee(), callee);
    }

	@TestWith({
		   "'11/11/2013 19:00:00', '12/11/2013 06:59:59', 43199",
		   "'11/11/2013 19:00:00', '11/11/2013 19:00:00', 0",
		})
	public void testDurationSeconds (String startTime, String endTime, int expectedResult) {
		DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss");
        DateTime formattedStartTime = formatter.parseDateTime(startTime);
        DateTime formattedEndTime = formatter.parseDateTime(endTime);

        freeze(formattedStartTime);
        CallStart startEvent = new CallStart("Caller", "Callee");
        freeze(formattedEndTime);
		CallEnd endEvent = new CallEnd("Caller", "Callee");
		unfreeze();
		Call tester = new Call(startEvent, endEvent);
		int result = tester.durationSeconds();

        assertEquals(tester.startTime(), formattedStartTime);
        assertEquals(tester.endTime(), formattedEndTime);
	    assertEquals(result,expectedResult);

	}
	
	@Coercion
	public BigDecimal whateverName (String input) {
		return new BigDecimal(input);
	}
	
    private void freeze(DateTime frozenDateTime) {DateTimeUtils.setCurrentMillisFixed(frozenDateTime.getMillis());}
    private void unfreeze() {DateTimeUtils.setCurrentMillisSystem();}

}

