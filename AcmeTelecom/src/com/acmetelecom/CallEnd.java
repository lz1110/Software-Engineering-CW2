package com.acmetelecom;

import org.joda.time.DateTime;

public class CallEnd extends CallEvent {
    public CallEnd(String caller, String callee) {
        super(caller, callee, new DateTime().getMillis());
    }
}
