package com.sanderxavalon;

import com.sanderxavalon.fsm.FSMConstructor;
import com.sanderxavalon.fsm.ProcessState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.statefulj.fsm.FSM;
import org.statefulj.fsm.TooBusyException;

import static com.sanderxavalon.fsm.FSMConstructor.*;

public class Application {

    private final static Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws TooBusyException {

        // Instantiate the Stateful Entity
        ProcessState processState = new ProcessState();

        FSM fsm = FSMConstructor.getInstance();

        fsm.onEvent(processState, event_activate);  // state_pending(event_active) -> state_active/action_when_activate

        fsm.onEvent(processState, event_release);  // state_active(event_release) -> state_release/action_when_release

        processState.setWithdraw(true);

        fsm.onEvent(processState, event_recall);  // event_release(event_recall) -> state_pending/action_when_withdraw

        // same flow but set withdraw false

        fsm.onEvent(processState, event_activate);

        fsm.onEvent(processState, event_release);

        processState.setWithdraw(false);

        fsm.onEvent(processState, event_recall);  // event_release(event_recall) -> state_active/No operation

        logger.info(processState.getState());

    }

}
