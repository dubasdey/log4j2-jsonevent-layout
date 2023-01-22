package org.erc.log4j2.layout.examples.userfields;

import java.io.IOException;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

public class ClassToLog6 implements Runnable {
    
    private static final Logger logger = LogManager.getLogger(ClassToLog6.class);
    
    public void run() {
        Exception ex = new IOException("Dummy IO Exception", new Exception("Dummy base Exception"));
        
        ThreadContext.push(UUID.randomUUID().toString());
        ThreadContext.push(UUID.randomUUID().toString());
        ThreadContext.push(UUID.randomUUID().toString());
        ThreadContext.put("MyCustomThreadKey1", "MyCustomThreadValue1");
        ThreadContext.put("MyCustomThreadKey2", "MyCustomThreadValue2");
        ThreadContext.put("MyCustomThreadKey3", "MyCustomThreadValue3");
        logger.error("Error line on ClassToLog6",ex); 
        
        
        ThreadContext.clearStack();
        logger.error("Error line on ClassToLog6",ex);
        logger.debug("Debug line on ClassToLog6");
        
        ThreadContext.clearAll();
        ThreadContext.push(UUID.randomUUID().toString());
        ThreadContext.push(UUID.randomUUID().toString());
        logger.debug("Debug line on ClassToLog6");
        
        ThreadContext.clearAll();
        logger.debug("Debug line on ClassToLog6");
        
    }   
}
