package com.sanderxavalon.fsm;

import com.sanderxavalon.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.statefulj.fsm.FSM;
import org.statefulj.fsm.RetryException;
import org.statefulj.fsm.model.Action;
import org.statefulj.fsm.model.State;
import org.statefulj.fsm.model.StateActionPair;
import org.statefulj.fsm.model.Transition;
import org.statefulj.fsm.model.impl.StateActionPairImpl;
import org.statefulj.fsm.model.impl.StateImpl;
import org.statefulj.persistence.memory.MemoryPersisterImpl;

import java.util.LinkedList;
import java.util.List;

public class FSMConstructor {

    private final static Logger logger = LoggerFactory.getLogger(FSMConstructor.class);

    private static FSM<ProcessState> instance = null;

    public static String event_activate  = "Event Activate";
    public static String event_release = "Event Release";
    public static String event_recall = "Event Recall";

    public static State<ProcessState> state_pending = new StateImpl<ProcessState>("Pending");
    public static State<ProcessState> state_active = new StateImpl<ProcessState>("Active");
    public static State<ProcessState> state_release = new StateImpl<ProcessState>("Release", true); // End state

    public static Action<ProcessState> action_when_activate = new ProcessAction<>("The state now is active.");
    public static Action<ProcessState> action_when_release = new ProcessAction<>("The state now is released.");
    public static Action<ProcessState> action_when_withdraw = new ProcessAction<>("The state now is pending.");



    public static FSM<ProcessState> getInstance() {

        if (instance == null) {
            synchronized (FSM.class) {

                /*
                    Deterministic Transitions
                */

                // state "state_pending", "event_activate" trigger
                // state move to "state_active", do action "action_when_activate"
                state_pending.addTransition(event_activate, state_active, action_when_activate);

                // state "state_active", "event_release" trigger
                // state move to "state_release", do action "action_when_release"
                state_active.addTransition(event_release, state_release, action_when_release);

                /*
                    Non-Deterministic Transitions
                */

                //                                +--> state_pending/action_when_withdraw
                //  state_release(event_recall) --|
                //                                +--> state_active/NOOP

                state_release.addTransition(event_recall, new Transition<ProcessState>() {
                    @Override
                    public StateActionPair<ProcessState> getStateActionPair(ProcessState processState, String s, Object... objects) throws RetryException {
                        if (processState.isWithdraw()) {
                            return new StateActionPairImpl<ProcessState>(state_pending, action_when_withdraw);
                        } else {
                            return new StateActionPairImpl<ProcessState>(state_active, null);
                        }
                    }
                });

                // In-Memory Persister
                List<State<ProcessState>> states = new LinkedList<State<ProcessState>>();
                states.add(state_pending);
                states.add(state_active);
                states.add(state_release);

                MemoryPersisterImpl<ProcessState> persister =
                        new MemoryPersisterImpl<ProcessState>(
                                states,          // Set of States
                                state_pending);  // Start State

                instance = new FSM<ProcessState>("Process FSM", persister);

            }
        }

        return instance;
    }


}

