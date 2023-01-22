package org.erc.log4j2.layout.examples.userfields;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ClassToLog3 implements Runnable {
    
    private static final Logger logger = LogManager.getLogger(ClassToLog3.class);
    
    public void run() {
        logger.debug("Debug line on ClassToLog3"); 
    }
}
