package org.erc.log4j2.layout;


import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.ThreadContext.ContextStack;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.AbstractStringLayout;

/**
 * The Class JSONLog4j2Layout.
 */
@Plugin(name = "JSONLog4j2Layout", category = "Core", elementType = "layout", printObject = true)
public class JSONLog4j2Layout extends AbstractStringLayout {

	/** The Constant VERSION. */
	private static final String VERSION ="1";
	
	/** The Constant ENTITY_SEP. */
	private static final String ENTITY_SEP ="'";
	
	/** The Constant OBJ_S. */
	private static final String OBJ_S ="{";
	
	/** The Constant OBJ_E. */
	private static final String OBJ_E ="}";
	
	/** The Constant LST_S. */
	private static final String LST_S ="[";
	
	/** The Constant LST_E. */
	private static final String LST_E ="]";	
	
	/** The Constant COMMA. */
	private static final String COMMA = ",";
	
	/** The Constant DOTS. */
	private static final String DOTS  = ":";
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -9204326215721954008L;

    /** The Constant ISO_DATETIME_TIME_ZONE_FORMAT_WITH_MILLIS. */
    private static final DateFormat ISO_DATETIME_TIME_ZONE_FORMAT_WITH_MILLIS = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    /** The location info. */
    private boolean locationInfo = false;
    
	/**
	 * Instantiates a new JSON log 4 j 2 layout.
	 *
	 * @param locationInfo the location info
	 * @param charset the charset
	 */
	protected JSONLog4j2Layout(boolean locationInfo,Charset charset) {
		super(charset);
		this.locationInfo = locationInfo;
	}
	
    /**
     * Creates the layout.
     *
     * @param locationInfo the location info
     * @param charset the charset
     * @return the JSON log 4 j 2 layout
     */
    @PluginFactory
    public static JSONLog4j2Layout createLayout(
    		@PluginAttribute("locationInfo") boolean locationInfo,
    		@PluginAttribute("charset") Charset charset ) {
        return new JSONLog4j2Layout(locationInfo,charset);
    }
    
    /**
     * Clean JSON.
     *
     * @param jsonStr the json str
     * @return the string
     */
    private String cleanJSON(String jsonStr){
    	return jsonStr;
    }
    
    /**
     * Gets the stack trace.
     *
     * @param traces the traces
     * @return the stack trace
     */
    private String getStackTrace(StackTraceElement[] traces){
    	StringBuilder builder = new StringBuilder();
    	if(traces!=null){
    		for(StackTraceElement trace: traces){
    			builder.append(cleanJSON(trace.toString())).append("\n");
    		}
    	}
    	return builder.toString();
    }
    
    /**
     * Adds the field.
     *
     * @param builder the builder
     * @param key the key
     * @param value the value
     */
    private void addField(StringBuilder builder,String key,Object value){
    	addField(builder,key,value,true);
    }
    
    /**
     * Gets the map.
     *
     * @param map the map
     * @return the map
     */
    private String getMap(Map<String, String> map){
    	StringBuilder builder = new StringBuilder();
    	builder.append(LST_S);
    	if(map!=null && !map.isEmpty()){
    		for(String key:map.keySet()){
    			builder.append(ENTITY_SEP).append(key).append(ENTITY_SEP).append(DOTS).append(ENTITY_SEP);
    			builder.append( cleanJSON(map.get(key)) ).append(ENTITY_SEP).append(COMMA);
    		}
    	}
    	builder.append(ENTITY_SEP).append("X-Generator").append(ENTITY_SEP).append(DOTS);
    	builder.append(ENTITY_SEP).append("JSONLog2j2Layout").append(ENTITY_SEP);
    	builder.append(LST_E);
    	return builder.toString();
    }
    
    /**
     * Adds the field.
     *
     * @param builder the builder
     * @param key the key
     * @param value the value
     * @param comma the comma
     */
    private void addField(StringBuilder builder,String key,Object value,boolean comma){
    	if(value instanceof String){
    		builder.append(ENTITY_SEP).append(key).append(ENTITY_SEP).append(DOTS);
    		builder.append(ENTITY_SEP).append(cleanJSON(value.toString())).append(ENTITY_SEP);
    		
    	} else if (value instanceof Number){
    		builder.append(ENTITY_SEP).append(key).append(ENTITY_SEP).append(DOTS).append(value);
    		
    	} else if (value instanceof ContextStack){
    		List<String> stack = ((ContextStack) value).asList();
    		builder.append(ENTITY_SEP).append(key).append(ENTITY_SEP).append(DOTS);
    		builder.append(LST_S);
    		if(stack!=null && !stack.isEmpty()){
	    		for(int i=0;i<stack.size();i++){
	    			builder.append(ENTITY_SEP).append(cleanJSON(stack.get(i))).append(ENTITY_SEP);
	    			if(i<stack.size()-1){
	    				builder.append(COMMA);
	    			}
	    		}
    		}
    		builder.append(LST_E);
    	
    	} else if (value instanceof Throwable){
    		Throwable t = (Throwable) value;
    		builder.append(ENTITY_SEP).append(key).append(ENTITY_SEP).append(DOTS);
    		
    		builder.append(OBJ_S);
    			addField(builder,"exception_class",t.getClass().getCanonicalName());
    			addField(builder,"exception_message",cleanJSON(t.getMessage()));
    			addField(builder,"stacktrace",getStackTrace(t.getStackTrace()),false);
    		builder.append(OBJ_E);
    	}
		if(comma){
			builder.append(COMMA);
		}    	
    }
    
    /**
     * Gets the host name.
     *
     * @return the host name
     */
    private String getHostName(){
    	String hostName = "unknown-host"; 
        try {
            hostName = java.net.InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            /* Ignored */
        }
        return hostName;
    }
    
	/* (non-Javadoc)
	 * @see org.apache.logging.log4j.core.Layout#toSerializable(org.apache.logging.log4j.core.LogEvent)
	 */
	public String toSerializable(LogEvent event) {
		StringBuilder builder = new StringBuilder();
		
        builder.append(OBJ_S);
        
        addField(builder,"@timestamp",ISO_DATETIME_TIME_ZONE_FORMAT_WITH_MILLIS.format(event.getTimeMillis()));
        addField(builder,"logger_name", event.getLoggerName());
        addField(builder,"level", event.getLevel().name());
        addField(builder,"thread_name", event.getThreadName());
        addField(builder,"source_host", getHostName());
        addField(builder,"message", event.getMessage().getFormattedMessage());
        
        if (event.getThrown() !=null){
        	addField(builder,"exception",event.getThrown());
        }
        
        if (locationInfo) {
        	 addField(builder,"file",event.getSource().getFileName());
        	 addField(builder,"line_number",event.getSource().getLineNumber());
        	 addField(builder,"class",event.getSource().getClassName());
        	 addField(builder,"method",event.getSource().getMethodName());
        }
        
        if(event.getContextStack()!=null) {
        	addField(builder,"contextStack",event.getContextStack());
        }
        
        if(event.getContextStack()!=null) {
        	addField(builder,"contextMap",getMap(event.getContextMap()));
        }

        addField(builder,"@version",VERSION,false);
        builder.append(OBJ_E);
		return builder.toString();
	}

}