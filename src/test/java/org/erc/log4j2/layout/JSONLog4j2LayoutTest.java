package org.erc.log4j2.layout;

import java.nio.charset.Charset;

import org.apache.logging.log4j.core.LogEvent;
import org.junit.Test;

/**
 * The Class JSONLog4j2LayoutTest.
 */
public class JSONLog4j2LayoutTest {

	/** The Constant SHORT_STRING. */
	private static final String SHORT_STRING="Dummy Message Test with tab T\tT before here.";
	
	/** The Constant LONG_STRING. */
	private static final String LONG_STRING="Dummy Message Test with tab T\tT before here.\r\nOther Line.\r\nAnother Line.";
	
	/**
	 * Test empty event.
	 */
	@Test
	public void testEmptyEvent(){
		JSONLog4j2Layout layout = new JSONLog4j2Layout(false,false,false,false,null,Charset.forName("UTF-8"));
		LogEvent event = new DummyEmptyLogEvent();
		System.out.println(layout.toSerializable(event));
	}
	
	/**
	 * Test filled event.
	 */
	@Test
	public void testFilledEvent(){
		JSONLog4j2Layout layout = new JSONLog4j2Layout(false,false,false,false,null,Charset.forName("UTF-8"));
		LogEvent event = new DummyFilledLogEvent(SHORT_STRING);
		System.out.println(layout.toSerializable(event));
	}
	
	/**
	 * Test filled event.
	 */
	@Test
	public void testFilledWithLocationEvent(){
		JSONLog4j2Layout layout = new JSONLog4j2Layout(true,false,false,false,null,Charset.forName("UTF-8"));
		DummyFilledLogEvent event = new DummyFilledLogEvent(SHORT_STRING);
		event.setSource(new StackTraceElement("ClassName.class","MethodElement","File.java",42));
		System.out.println(layout.toSerializable(event));
	}
	
	
	/**
	 * Test filled multi line event.
	 */
	@Test
	public void testFilledMultiLineEvent(){
		JSONLog4j2Layout layout = new JSONLog4j2Layout(false,false,false,false,null,Charset.forName("UTF-8"));
		LogEvent event = new DummyFilledLogEvent(LONG_STRING);
		System.out.println(layout.toSerializable(event));
	}		
	
	/**
	 * Test filled multi line event.
	 */
	@Test
	public void testFilledMultiLineWithUserFieldsEvent(){
		UserField[] fields = new UserField[2];
		fields[0] = new UserField("A1","B1");
		fields[1] = new UserField("A2","B2");
		JSONLog4j2Layout layout = new JSONLog4j2Layout(false,false,false,false,fields,Charset.forName("UTF-8"));
		LogEvent event = new DummyFilledLogEvent(LONG_STRING);
		System.out.println(layout.toSerializable(event));
	}	
	
	/**
	 * Test filled multi line removed event.
	 */
	@Test
	public void testFilledMultiLineRemovedEvent(){
		JSONLog4j2Layout layout = new JSONLog4j2Layout(false,true,false,false,null,Charset.forName("UTF-8"));
		LogEvent event = new DummyFilledLogEvent(LONG_STRING);
		System.out.println(layout.toSerializable(event));
	}
	
	/**
	 * Test filled multi line event.
	 */
	@Test
	public void testFilledMultiLineWithUserFieldsAndContextMapEvent(){
		UserField[] fields = new UserField[2];
		fields[0] = new UserField("A1","B1");
		fields[1] = new UserField("A2","B2");
		JSONLog4j2Layout layout = new JSONLog4j2Layout(false,false,false,false,fields,Charset.forName("UTF-8"));
		LogEvent event = new DummyFilledLogEvent(LONG_STRING);
		event.getContextMap().put("CT1", "VALUE");
		event.getContextMap().put("CT2", "VALUE");
		System.out.println(layout.toSerializable(event));
	}	
	
	/**
	 * Test filled multi line event.
	 */
	@Test
	public void testFilledMultiLineWithUserFieldsAndContextMapAsRootEvent(){
		UserField[] fields = new UserField[2];
		fields[0] = new UserField("A1","B1");
		fields[1] = new UserField("A2","B2");
		JSONLog4j2Layout layout = new JSONLog4j2Layout(false,false,false,true,fields,Charset.forName("UTF-8"));
		DummyFilledLogEvent event = new DummyFilledLogEvent(LONG_STRING);
		event.getContextMap().put("CT1", "VALUE");
		event.getContextMap().put("CT2", "VALUE");
		System.out.println(layout.toSerializable(event));
	}		
}
