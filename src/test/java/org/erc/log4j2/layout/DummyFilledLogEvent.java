package org.erc.log4j2.layout;

import java.util.Map;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.ThreadContext.ContextStack;
import org.apache.logging.log4j.core.AbstractLogEvent;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.SimpleMessage;
import org.apache.logging.log4j.spi.DefaultThreadContextMap;
import org.apache.logging.log4j.util.ReadOnlyStringMap;

/**
 * The Class DummyFilledLogEvent.
 */
public class DummyFilledLogEvent extends AbstractLogEvent {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 2743107617386765655L;

    /** The message. */
    private String message = null;

    /** Thread Context as Dummy contextData. */
    private DefaultThreadContextMap contextMap = new DefaultThreadContextMap();

    /** exception. */
    private Throwable thrown;

    /** The source. */
    private StackTraceElement source;

    /** The context stack. */
    private ContextStack contextStack;
    
    
    /**
     * Instantiates a new dummy filled log event.
     *
     * @param message the message
     */
    public DummyFilledLogEvent(String message) {
        this.message = message;
    }
    
    /**
     * Gets the context stack.
     *
     * @return the context stack
     */
    @Override
    public ContextStack getContextStack() {
        return this.contextStack;
    }
    
    /**
     * Sets the context steack.
     *
     * @param stack the new context steack
     */
    public void setContextStack(ContextStack stack) {
        this.contextStack = stack;
    }

    /**
     * Gets the level.
     *
     * @return the level
     */
    @Override
    public Level getLevel() {
        return Level.DEBUG;
    }

    /**
     * Gets the logger fqcn.
     *
     * @return the logger fqcn
     */
    @Override
    public String getLoggerFqcn() {
        return "org.dummy.package";
    }

    /**
     * Gets the message.
     *
     * @return the message
     */
    @Override
    public Message getMessage() {
        return new SimpleMessage(message);
    }

    /**
     * Gets the logger name.
     *
     * @return the logger name
     */
    @Override
    public String getLoggerName() {
        return "org.dummy.logger";
    }

    /**
     * Gets the thread name.
     *
     * @return the thread name
     */
    @Override
    public String getThreadName() {
        return "DummyThread";
    }

    /**
     * Gets the thrown.
     *
     * @return the thrown
     */
    @Override
    public Throwable getThrown() {
        return thrown;
    }

    /**
     * Set dummy throwable.
     *
     * @param thrown the new thrown dummy
     */
    public void setThrownDummy(Throwable thrown) {
        this.thrown = thrown;
    }

    /**
     * Gets the time millis.
     *
     * @return the time millis
     */
    @Override
    public long getTimeMillis() {
        return System.currentTimeMillis();
    }

    /**
     * Gets the context data.
     *
     * @return the context data
     */
    @Override
    public ReadOnlyStringMap getContextData() {
        return contextMap;
    }

    /**
     * Gets the context data dummy.
     *
     * @return the context data dummy
     */
    public DefaultThreadContextMap getContextDataDummy() {
        return contextMap;
    }

    /**
     * Sets the context data.
     *
     * @param map the map
     * @return the read only string map
     */
    public ReadOnlyStringMap setContextData(ReadOnlyStringMap map) {
        return contextMap;
    }

    /**
     * Gets the context map.
     *
     * @return the context map
     */
    /*
     * (non-Javadoc)
     * 
     * @see org.apache.logging.log4j.core.AbstractLogEvent#getContextMap()
     */
    @Override
    public Map<String, String> getContextMap() {
        // Deprecated skip usage
        return null;
    }

    /**
     * Gets the source.
     *
     * @return the source
     */
    /*
     * (non-Javadoc)
     * 
     * @see org.apache.logging.log4j.core.AbstractLogEvent#getSource()
     */
    @Override
    public StackTraceElement getSource() {
        return source;
    }

    /**
     * Sets the source.
     *
     * @param source the new source
     */
    public void setSource(StackTraceElement source) {
        this.source = source;
    }

    /**
     * Checks if is include location.
     *
     * @return true, if is include location
     */
    @Override
    public boolean isIncludeLocation() {
        return source != null;
    }
}
