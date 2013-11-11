package com.acmetelecom;

import org.joda.time.DateTime;

public class CallStart extends CallEvent {
    public CallStart(String caller, String callee) {
        super(caller, callee, new DateTime().getMillis());
    }
}
