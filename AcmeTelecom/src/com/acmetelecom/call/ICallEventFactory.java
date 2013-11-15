package com.acmetelecom.call;

/**
 * Interface for factory creating call events
 */
public interface ICallEventFactory {
    public CallEvent createCallEvent(String caller, String callee, CallEventType type);
}
