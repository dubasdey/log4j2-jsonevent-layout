package org.erc.log4j2.layout;

import static org.junit.Assert.assertNotNull;

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
		String serializedData = layout.toSerializable(event);
		assertNotNull("Serialized data",serializedData);
	}
	
	/**
	 * Test filled event.
	 */
	@Test
	public void testFilledEvent(){
		JSONLog4j2Layout layout = new JSONLog4j2Layout(false,false,false,false,null,Charset.forName("UTF-8"));
		LogEvent event = new DummyFilledLogEvent(SHORT_STRING);
		String serializedData = layout.toSerializable(event);
		assertNotNull("Serialized data",serializedData);
	}
	
	/**
	 * Test filled event.
	 */
	@Test
	public void testFilledWithLocationEvent(){
		JSONLog4j2Layout layout = new JSONLog4j2Layout(true,false,false,false,null,Charset.forName("UTF-8"));
		DummyFilledLogEvent event = new DummyFilledLogEvent(SHORT_STRING);
		event.setSource(new StackTraceElement("ClassName.class","MethodElement","File.java",42));
		String serializedData = layout.toSerializable(event);
		assertNotNull("Serialized data",serializedData);
	}
	
	
	/**
	 * Test filled multi line event.
	 */
	@Test
	public void testFilledMultiLineEvent(){
		JSONLog4j2Layout layout = new JSONLog4j2Layout(false,false,false,false,null,Charset.forName("UTF-8"));
		LogEvent event = new DummyFilledLogEvent(LONG_STRING);
		String serializedData = layout.toSerializable(event);
		assertNotNull("Serialized data",serializedData);
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
		String serializedData = layout.toSerializable(event);
		assertNotNull("Serialized data",serializedData);
	}	
	
	/**
	 * Test filled multi line removed event.
	 */
	@Test(expected = NullPointerException.class)
	public void testNullEvent(){
		JSONLog4j2Layout layout = JSONLog4j2Layout.createLayout(null, false, false, false, false, Charset.forName("UTF-8"), null);
		String serializedData = layout.toSerializable(null);
		assertNotNull("Serialized data",serializedData);
	}
	
	/**
	 * Test filled multi line removed event.
	 */
	@Test
	public void testThrow(){
		JSONLog4j2Layout layout = JSONLog4j2Layout.createLayout(null, false, false, false, false, Charset.forName("UTF-8"), null);
		DummyFilledLogEvent event = new DummyFilledLogEvent(LONG_STRING);
		event.setThrownDummy(new Throwable("TESTEX"));
		String serializedData = layout.toSerializable(event);
		assertNotNull("Serialized data",serializedData);
	}
	
	
	/**
	 * Test filled multi line removed event.
	 */
	@Test
	public void testFilledMultiLineRemovedEvent(){
		JSONLog4j2Layout layout = new JSONLog4j2Layout(false,true,false,false,null,Charset.forName("UTF-8"));
		LogEvent event = new DummyFilledLogEvent(LONG_STRING);
		String serializedData = layout.toSerializable(event);
		assertNotNull("Serialized data",serializedData);
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
		DummyFilledLogEvent event = new DummyFilledLogEvent(LONG_STRING);
		event.getContextDataDummy().put("CT1", "VALUE");
		event.getContextDataDummy().put("CT2", "VALUE");
		String serializedData = layout.toSerializable(event);
		assertNotNull("Serialized data",serializedData);
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
		event.getContextDataDummy().put("CT1", "VALUE");
		event.getContextDataDummy().put("CT2", "VALUE");
		String serializedData = layout.toSerializable(event);
		assertNotNull("Serialized data",serializedData);
	}		
}
