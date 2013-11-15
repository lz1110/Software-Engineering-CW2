package com.acmetelecom.call;

import org.joda.time.DateTime;

public class CallEnd extends CallEvent {
    public CallEnd(String caller, String callee) {
        super(caller, callee, new DateTime().getMillis());
    }

    @Override
    public CallEventType getType() {
        return CallEventType.END;
    }
}
