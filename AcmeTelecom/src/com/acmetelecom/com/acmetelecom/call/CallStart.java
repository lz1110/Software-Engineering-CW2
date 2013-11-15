package com.acmetelecom.com.acmetelecom.call;

import org.joda.time.DateTime;

public class CallStart extends CallEvent {
    public CallStart(String caller, String callee) {
        super(caller, callee, new DateTime().getMillis());
    }

    @Override
    public CallEventType getType() {
        return CallEventType.START;
    }
}
