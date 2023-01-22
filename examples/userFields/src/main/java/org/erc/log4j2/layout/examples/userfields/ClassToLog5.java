package org.erc.log4j2.layout.examples.userfields;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

public class ClassToLog5 implements Runnable {
    
    private static final Logger logger = LogManager.getLogger(ClassToLog5.class);
    
    public void run() {
        ThreadContext.push(UUID.randomUUID().toString());
        ThreadContext.put("MyCustomThreadKey1", "MyCustomThreadValue1");
        ThreadContext.put("MyCustomThreadKey2", "MyCustomThreadValue2");
        logger.debug("Debug line on ClassToLog5"); 
    }   
}
