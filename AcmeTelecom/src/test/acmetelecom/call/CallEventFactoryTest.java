package test.acmetelecom.call;

import com.acmetelecom.call.CallEvent;
import com.acmetelecom.call.CallEventFactory;
import com.acmetelecom.call.CallEventType;
import com.googlecode.zohhak.api.TestWith;
import com.googlecode.zohhak.api.runners.ZohhakRunner;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;


@RunWith(ZohhakRunner.class)
public class CallEventFactoryTest {
    @TestWith({
            "'caller', 'callee'",
            "'441111222233', '440000000000'"
    })
    public void testCreateCallEvent(String caller, String callee){
        CallEventFactory factory = new CallEventFactory();

        CallEvent start = factory.createCallEvent(caller, callee, CallEventType.START);
        CallEvent end = factory.createCallEvent(caller, callee, CallEventType.END);

        assertEquals(start.getType(), CallEventType.START);
        assertEquals(end.getType(), CallEventType.END);
    }
}
