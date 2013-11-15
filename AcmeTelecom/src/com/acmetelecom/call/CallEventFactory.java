package com.acmetelecom.call;

/**
 * Factory that creates different call events depending on given call event type
 * Returns null if the type is not found.
 */
public class CallEventFactory implements ICallEventFactory {
    @Override
    public CallEvent createCallEvent(String caller, String callee, CallEventType type) {
        switch(type){
            case START:
                return new CallStart(caller, callee);
            case END:
                return new CallEnd(caller, callee);
            default:
                return null;
        }
    }
}
