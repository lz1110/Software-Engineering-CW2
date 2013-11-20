package com.acmetelecom.call;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Call {
    private CallEvent start;
    private CallEvent end;

    public Call(CallEvent start, CallEvent end) {
        this.start = start;
        this.end = end;
    }

    public String callee() {
        return start.getCallee();
    }

    public int durationSeconds() {
        return (int) (((end.time() - start.time()) / 1000));
    }

    public String date() {
        return SimpleDateFormat.getInstance().format(new Date(start.time()));
    }

    public DateTime startTime() {
        return new DateTime(start.time());
    }

    public DateTime endTime() {
        return new DateTime(end.time());
    }
}
