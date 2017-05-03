package org.erc.log4j2.layout;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.AbstractLogEvent;
import org.apache.logging.log4j.message.FormattedMessageFactory;
import org.apache.logging.log4j.message.Message;

public class DummyFilledLogEvent extends AbstractLogEvent{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2743107617386765655L;

	@Override
	public Level getLevel() {
		return Level.DEBUG;
	}
	
	@Override
	public String getLoggerFqcn() {
		return "org.dummy.package";
	}
	
	@Override
	public Message getMessage() {
		FormattedMessageFactory factory = new FormattedMessageFactory();
		return factory.newMessage("Dummy Message Test with tab \t before here");
	}
	
	@Override
	public String getLoggerName() {
		return "org.dummy.logger";
	}
	
	@Override
	public String getThreadName() {
		return "DummyThread";
	}
	@Override
	public long getTimeMillis() {
		return System.currentTimeMillis();
	}
}
