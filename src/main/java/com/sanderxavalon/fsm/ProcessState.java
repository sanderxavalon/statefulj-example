package com.sanderxavalon.fsm;

import org.statefulj.persistence.annotations.State;

public class ProcessState {

    @State
    private String state;   // Memory Persister requires a String

    private boolean withdraw;

    // Note: there is no setter for the state field
    //       as the value is set by StatefulJ
    public void setWithdraw(boolean withdraw) {
        this.withdraw = withdraw;
    }

    public String getState() {
        return state;
    }

    public boolean isWithdraw() {
        return withdraw;
    }
}
