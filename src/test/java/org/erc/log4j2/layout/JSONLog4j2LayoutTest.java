package org.erc.log4j2.layout;

import java.nio.charset.Charset;

import org.apache.logging.log4j.core.LogEvent;
import org.junit.Test;

/**
 * The Class JSONLog4j2LayoutTest.
 */
public class JSONLog4j2LayoutTest {

	/**
	 * Test empty event.
	 */
	@Test
	public void testEmptyEvent(){
		JSONLog4j2Layout layout = new JSONLog4j2Layout(false,false,false,Charset.forName("UTF-8"));
		LogEvent event = new DummyEmptyLogEvent();
		System.out.println(layout.toSerializable(event));
	}
	
	/**
	 * Test filled event.
	 */
	@Test
	public void testFilledEvent(){
		JSONLog4j2Layout layout = new JSONLog4j2Layout(false,false,false,Charset.forName("UTF-8"));
		LogEvent event = new DummyFilledLogEvent("Dummy Message Test with tab T\tT before here.");
		System.out.println(layout.toSerializable(event));
	}
	
	/**
	 * Test filled multi line event.
	 */
	@Test
	public void testFilledMultiLineEvent(){
		JSONLog4j2Layout layout = new JSONLog4j2Layout(false,false,false,Charset.forName("UTF-8"));
		LogEvent event = new DummyFilledLogEvent("Dummy Message Test with tab T\tT before here.\r\nOther Line.\r\nAnother Line.");
		System.out.println(layout.toSerializable(event));
	}		
	
	/**
	 * Test filled multi line removed event.
	 */
	@Test
	public void testFilledMultiLineRemovedEvent(){
		JSONLog4j2Layout layout = new JSONLog4j2Layout(false,true,false,Charset.forName("UTF-8"));
		LogEvent event = new DummyFilledLogEvent("Dummy Message Test with tab T\tT before here.\r\nOther Line.\r\nAnother Line.");
		System.out.println(layout.toSerializable(event));
	}
}
