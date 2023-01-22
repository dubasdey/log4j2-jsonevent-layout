package org.erc.log4j2.layout;


import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.charset.Charset;

import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.spi.DefaultThreadContextStack;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * The Class JSONLog4j2LayoutTest.
 */
@DisplayName("Layout Plugin test")
class JSONLog4j2LayoutTest {

    /** The Constant SHORT_STRING. */
    private static final String SHORT_STRING = "Dummy Message Test with tab T\tT before here.";

    /** The Constant LONG_STRING. */
    private static final String LONG_STRING = "Dummy Message Test with tab T\tT before here.\r\nOther Line.\r\nAnother Line.";

    /**
     * Test empty event.
     */
    @Test
    void testEmptyEvent() {
        JSONLog4j2Layout layout = new JSONLog4j2Layout(false, false, false, "\n", false, null,
                Charset.forName("UTF-8"));
        LogEvent event = new DummyEmptyLogEvent();
        String serializedData = layout.toSerializable(event);
        assertNotNull(serializedData,"Serialized data");
    }

    /**
     * Test filled event.
     */
    @Test
    void testFilledEvent() {
        JSONLog4j2Layout layout = new JSONLog4j2Layout(false, false, false, "\n", false, null,
                Charset.forName("UTF-8"));
        LogEvent event = new DummyFilledLogEvent(SHORT_STRING);
        String serializedData = layout.toSerializable(event);
        assertNotNull(serializedData,"Serialized data");
    }

    /**
     * Test filled event.
     */
    @Test
    void testFilledEventNoContextMap() {
        JSONLog4j2Layout layout = new JSONLog4j2Layout(false, false, false, "\n", false, null,
                Charset.forName("UTF-8"));
        LogEvent event = new DummyFilledLogEvent(SHORT_STRING);
        ((DummyFilledLogEvent) event).setContextData(null);
        String serializedData = layout.toSerializable(event);
        assertNotNull(serializedData,"Serialized data");
    }
    

    /**
     * Test filled event.
     */
    @Test
    void testFilledEventContextMapEmpty() {
        JSONLog4j2Layout layout = new JSONLog4j2Layout(false, false, false, "\n", false, null,
                Charset.forName("UTF-8"));
        LogEvent event = new DummyFilledLogEvent(SHORT_STRING);
        ((DummyFilledLogEvent) event).getContextDataDummy().clear();
        String serializedData = layout.toSerializable(event);
        assertNotNull(serializedData,"Serialized data");
    }
    
    /**
     * Test filled event.
     */
    @Test
    void testFilledEventContextMapData() {
        JSONLog4j2Layout layout = new JSONLog4j2Layout(false, false, false, "\n", false, null,
                Charset.forName("UTF-8"));
        LogEvent event = new DummyFilledLogEvent(SHORT_STRING);
        ((DummyFilledLogEvent) event).getContextDataDummy().put("A", "B");
        String serializedData = layout.toSerializable(event);
        assertNotNull(serializedData,"Serialized data");
    }
    
    /**
     * Test filled event.
     */
    @Test
    void testFilledEventContextStack() {
        JSONLog4j2Layout layout = new JSONLog4j2Layout(false, false, false, "\n", false, null,
                Charset.forName("UTF-8"));
        LogEvent event = new DummyFilledLogEvent(SHORT_STRING);
        ((DummyFilledLogEvent) event).setContextStack(new DefaultThreadContextStack(true));
        ((DummyFilledLogEvent) event).getContextStack().add("UPS");
        String serializedData = layout.toSerializable(event);
        assertNotNull(serializedData,"Serialized data");
    }
    
    /**
     * Test filled event.
     */
    @Test
    void testFilledEventContextStackEmpty() {
        JSONLog4j2Layout layout = new JSONLog4j2Layout(false, false, false, "\n", false, null,
                Charset.forName("UTF-8"));
        LogEvent event = new DummyFilledLogEvent(SHORT_STRING);
        ((DummyFilledLogEvent) event).setContextStack(ThreadContext.EMPTY_STACK);
        String serializedData = layout.toSerializable(event);
        assertNotNull(serializedData,"Serialized data");
    }
    
    /**
     * Test filled event.
     */
    @Test
    void testFilledEventContextStackNull() {
        JSONLog4j2Layout layout = new JSONLog4j2Layout(false, false, false, "\n", false, null,
                Charset.forName("UTF-8"));
        LogEvent event = new DummyFilledLogEvent(SHORT_STRING);
        ((DummyFilledLogEvent) event).setContextStack(null);
        String serializedData = layout.toSerializable(event);
        assertNotNull(serializedData,"Serialized data");
    }
    
    /**
     * Test filled event.
     */
    @Test
    void testFilledWithLocationEvent() {
        JSONLog4j2Layout layout = new JSONLog4j2Layout(true, false, false, "\n", false, null, Charset.forName("UTF-8"));
        DummyFilledLogEvent event = new DummyFilledLogEvent(SHORT_STRING);
        event.setSource(new StackTraceElement("ClassName.class", "MethodElement", "File.java", 42));
        String serializedData = layout.toSerializable(event);
        assertNotNull(serializedData,"Serialized data");
    }

    /**
     * Test filled multi line event.
     */
    @Test
    void testFilledMultiLineEvent() {
        JSONLog4j2Layout layout = new JSONLog4j2Layout(false, false, false, null, false, null,
                Charset.forName("UTF-8"));
        LogEvent event = new DummyFilledLogEvent(LONG_STRING);
        String serializedData = layout.toSerializable(event);
        assertNotNull(serializedData,"Serialized data");
    }

    /**
     * Test filled multi line event.
     */
    @Test
    void testFilledMultiLineWithUserFieldsEvent() {
        UserField[] fields = new UserField[2];
        fields[0] = new UserField("A1", "B1");
        fields[1] = new UserField("A2", "B2");
        JSONLog4j2Layout layout = new JSONLog4j2Layout(false, false, false, "\n", false, fields,Charset.forName("UTF-8"));
        LogEvent event = new DummyFilledLogEvent(LONG_STRING);
        String serializedData = layout.toSerializable(event);
        assertNotNull(serializedData,"Serialized data");
    }

    /**
     * Test filled multi line removed event.
     */
    @Test
    void testNullEvent() {
        Throwable exception = assertThrows(NullPointerException.class, () -> {
            JSONLog4j2Layout layout = JSONLog4j2Layout.createLayout(null, false, false, false, "\n", false,
                    Charset.forName("UTF-8"), null);
            layout.toSerializable(null);
        });
        assertNotNull(exception,"Exception");
    }

    /**
     * Test filled multi line removed event.
     */
    @Test
    void testThrow() {
        JSONLog4j2Layout layout = JSONLog4j2Layout.createLayout(null, false, false,  false,"\n", false,
                Charset.forName("UTF-8"), null);
        DummyFilledLogEvent event = new DummyFilledLogEvent(LONG_STRING);
        event.setThrownDummy(new Throwable("TESTEX"));
        String serializedData = layout.toSerializable(event);
        assertNotNull(serializedData,"Serialized data");
    }
    
    /**
     * Test filled multi line removed event.
     */
    @Test
    void testNoChartSet() {
        JSONLog4j2Layout layout = JSONLog4j2Layout.createLayout(null, false, false,  false,"\n", false,
                null, null);
        DummyFilledLogEvent event = new DummyFilledLogEvent(LONG_STRING);
        event.setThrownDummy(new Throwable("TESTEX"));
        String serializedData = layout.toSerializable(event);
        assertNotNull(serializedData,"Serialized data");
    }
    
    /**
     * Test filled multi line removed event.
     */
    @Test
    void testFilledMultiLineRemovedEvent() {
        JSONLog4j2Layout layout = new JSONLog4j2Layout(false, true, false, "\n", false, null, Charset.forName("UTF-8"));
        LogEvent event = new DummyFilledLogEvent(LONG_STRING);
        String serializedData = layout.toSerializable(event);
        assertNotNull(serializedData,"Serialized data");
    }

    /**
     * Test filled multi line event.
     */
    @Test
    void testFilledMultiLineWithUserFieldsAndContextMapEvent() {
        UserField[] fields = new UserField[2];
        fields[0] = new UserField("A1", "B1");
        fields[1] = new UserField("A2", "B2");
        JSONLog4j2Layout layout = new JSONLog4j2Layout(false, false, false, "\n", false, fields,
                Charset.forName("UTF-8"));
        DummyFilledLogEvent event = new DummyFilledLogEvent(LONG_STRING);
        event.getContextDataDummy().put("CT1", "VALUE");
        event.getContextDataDummy().put("CT2", "VALUE");
        String serializedData = layout.toSerializable(event);
        assertNotNull(serializedData,"Serialized data");
    }

    /**
     * Test filled multi line event.
     */
    @Test
    void testFilledMultiLineWithUserFieldsAndContextMapAsRootEvent() {
        UserField[] fields = new UserField[2];
        fields[0] = new UserField("A1", "B1");
        fields[1] = new UserField("A2", "B2");
        JSONLog4j2Layout layout = new JSONLog4j2Layout(false, false, false, "\n", true, fields,
                Charset.forName("UTF-8"));
        DummyFilledLogEvent event = new DummyFilledLogEvent(LONG_STRING);
        event.getContextDataDummy().put("CT1", "VALUE");
        event.getContextDataDummy().put("CT2", "VALUE");
        String serializedData = layout.toSerializable(event);
        assertNotNull(serializedData,"Serialized data");
    }
}
