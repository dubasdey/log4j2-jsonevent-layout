package org.erc.log4j2.layout;

import java.util.Map;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.AbstractLogEvent;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.SimpleMessage;
import org.apache.logging.log4j.spi.DefaultThreadContextMap;
import org.apache.logging.log4j.util.ReadOnlyStringMap;

/**
 * The Class DummyFilledLogEvent.
 */
public class DummyFilledLogEvent extends AbstractLogEvent{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2743107617386765655L;

	/** The message. */
	private String message = null;
	
	/** Thread Context as Dummy contextData */
	private DefaultThreadContextMap contextMap = new DefaultThreadContextMap();
	
	/** exception */
	private Throwable thrown;
	
	
	/** The source. */
	private StackTraceElement source;
	
	
	/**
	 * Instantiates a new dummy filled log event.
	 *
	 * @param message the message
	 */
	public DummyFilledLogEvent(String message){
		this.message = message;
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
	
	@Override
	public Throwable getThrown() {
		return thrown;
	}
	
	/** Set dummy throwable*/
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
	
	@Override
	public ReadOnlyStringMap getContextData() {
		return contextMap;
	}
	
	public DefaultThreadContextMap getContextDataDummy() {
		return contextMap;
	}
	
	/* (non-Javadoc)
	 * @see org.apache.logging.log4j.core.AbstractLogEvent#getContextMap()
	 */
	@Override
	public Map<String, String> getContextMap() {
		// Deprecated skip usage
		return null;
	}

	/* (non-Javadoc)
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
	
	@Override
	public boolean isIncludeLocation() {
		return source!=null;
	}
}
