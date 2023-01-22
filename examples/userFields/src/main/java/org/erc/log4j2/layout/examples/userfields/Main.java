package org.erc.log4j2.layout.examples.userfields;

/**
 * The Class Main.
 */
public class Main {

    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
        runThread(new ClassToLog1());
        runThread(new ClassToLog2());
        runThread(new ClassToLog3());
        runThread(new ClassToLog4());
        runThread(new ClassToLog5());
        runThread(new ClassToLog6());
    }
    
    /**
     * Run thread.
     *
     * @param r the r
     */
    private static void runThread(Runnable r) {
        Thread thread = new Thread(r);
        thread.start();    
    }
}
