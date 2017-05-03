package org.erc.log4j2.layout;

import java.nio.charset.Charset;

import org.apache.logging.log4j.core.LogEvent;
import org.junit.Test;

public class JSONLog4j2LayoutTest {

	@Test
	public void testEvent(){
		JSONLog4j2Layout layout = new JSONLog4j2Layout(false,Charset.forName("UTF-8"));
		
		LogEvent event = new DummyLogEvent();
		
		String str = layout.toSerializable(event);
		System.out.println(str);
		
	}
}
