package com.sanderxavalon.fsm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.statefulj.fsm.RetryException;
import org.statefulj.fsm.model.Action;

public class ProcessAction<t> implements Action<t> {

    private final static Logger logger = LoggerFactory.getLogger(ProcessAction.class);

    String mailText;

    public ProcessAction(String mailText) {
        this.mailText = mailText;
    }

    @Override
    public void execute(Object o, String s, Object... objects) throws RetryException {
        logger.info(mailText);
    }
}
