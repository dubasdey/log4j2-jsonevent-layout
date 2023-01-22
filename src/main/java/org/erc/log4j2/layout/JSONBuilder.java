package org.erc.log4j2.layout;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext.ContextStack;
import org.apache.logging.log4j.status.StatusLogger;
/**
 * Simple JSON Builder
 * @author erc
 *
 */
public class JSONBuilder {
    
    /** The Constant REPLACEMENT_CHARS. */
    private static final String[] REPLACEMENT_CHARS;

    /** The Constant HTML_SAFE_REPLACEMENT_CHARS. */
    private static final String[] HTML_SAFE_REPLACEMENT_CHARS;

    static {
        REPLACEMENT_CHARS = new String[128];
        for (int i = 0; i <= 0x1f; i++) {
            REPLACEMENT_CHARS[i] = String.format("\\u%04x", (int) i);
        }
        REPLACEMENT_CHARS['"'] = "\\\"";
        REPLACEMENT_CHARS['\\'] = "\\\\";
        REPLACEMENT_CHARS['\t'] = "\\t";
        REPLACEMENT_CHARS['\b'] = "\\b";
        REPLACEMENT_CHARS['\n'] = "\\n";
        REPLACEMENT_CHARS['\r'] = "\\r";
        REPLACEMENT_CHARS['\f'] = "\\f";

        HTML_SAFE_REPLACEMENT_CHARS = REPLACEMENT_CHARS.clone();
        HTML_SAFE_REPLACEMENT_CHARS['<'] = "\\u003c";
        HTML_SAFE_REPLACEMENT_CHARS['>'] = "\\u003e";
        HTML_SAFE_REPLACEMENT_CHARS['&'] = "\\u0026";
        HTML_SAFE_REPLACEMENT_CHARS['='] = "\\u003d";
        HTML_SAFE_REPLACEMENT_CHARS['\''] = "\\u0027";
    }
    
    /** The Constant LOGGER. */
    private static final Logger LOGGER = StatusLogger.getLogger();
    
    /** The Constant ENTITY_SEP. */
    private static final char ENTITY_SEP = '\"';

    /** The Constant OBJ_S. */
    private static final char OBJ_S = '{';

    /** The Constant OBJ_E. */
    private static final char OBJ_E = '}';

    /** The Constant LST_S. */
    private static final char LST_S = '[';

    /** The Constant LST_E. */
    private static final char LST_E = ']';

    /** The Constant COMMA. */
    private static final char COMMA = ',';

    /** The Constant DOTS. */
    private static final String DOTS = ":";
    
    /** The builder. */
    private StringBuilder builder = new StringBuilder();
    
    /** The single line. */
    private boolean singleLine = false;
    
    /** The html safe. */
    private boolean htmlSafe = false;
    
    /** The new line format. */
    private String newLineFormat = null;
    
    /**
     * Start.
     *
     * @param htmlSafe the html safe
     * @param singleLine the single line
     * @param newLineFormat the new line format
     */
    public void start(boolean htmlSafe,boolean singleLine,String newLineFormat) {
        this.singleLine = singleLine;
        this.htmlSafe = htmlSafe;
        if(newLineFormat == null) {
            this.newLineFormat = "\n";
        } else {
            this.newLineFormat = newLineFormat;
        }     
        builder.append(OBJ_S);
        if(!singleLine) {
            builder.append(this.newLineFormat );
        }
    }
    
    /**
     * End.
     *
     * @return the string
     */
    public String end() {
        addField("@version", Constants.VERSION,false,0);
        builder.append(OBJ_E);
        builder.append(newLineFormat);
        return builder.toString();
    }
    /**
     * Clean JSON.
     *
     * @param value the value
     * @return the string
     */
    private String cleanJSON(String value) {
        LOGGER.debug("cleanJSON {}", value);
        if (value == null) {
            return "";
        } else {
            StringBuilder builder = new StringBuilder();
            if (singleLine) {
                value = value.replaceAll("\r", "");
                value = value.replaceAll("\n", "");
            }

            int length = value.length();
            for (int i = 0; i < length; i++) {
                char c = value.charAt(i);
                String replacement = null;

                if (c < 128) {
                    replacement = htmlSafe ? HTML_SAFE_REPLACEMENT_CHARS[c] : REPLACEMENT_CHARS[c];
                }

                if (replacement != null) {
                    builder.append(replacement);
                } else {
                    if (c == '\u2028') {
                        builder.append("\\u2028");
                    } else if (c == '\u2029') {
                        builder.append("\\u2029");
                    } else {
                        builder.append(c);
                    }
                }
            }
            return builder.toString();
        }
    }

    /**
     * Gets the stack trace.
     *
     * @param traces the traces
     * @return the stack trace
     */
    private void buildStackTrace(StackTraceElement[] traces,int deep) {
        if (traces != null) {
            deep++;
            addTab(deep);
            addKey("stacktrace");
            //addTab(deep);
            builder.append("[");
            addNewLine();
            deep++;
            for(int i=0;i<traces.length;i++) {
                addTab(deep);
                builder.append(ENTITY_SEP);
                builder.append(cleanJSON(traces[i].toString()));
                builder.append(ENTITY_SEP);
                if (i < traces.length - 1) {
                    builder.append(COMMA);
                }
                addNewLine();
            }
            deep--;
            addTab(deep);
            builder.append("]");
            addNewLine();
        }
 
    }

    /**
     * Adds the new line.
     */
    private void addNewLine() {
        if(!singleLine) {
            builder.append(newLineFormat);
        }
    }
    
    /**
     * Adds the field.
     *
     * @param builder the builder
     * @param key     the key
     * @param value   the value
     */
    public void addField(String key, Object value) {
        addField(key, value, true,0);
    }

    /**
     * Gets the map.
     *
     * @param map the map
     * @return the map
     */
    private void buildMap(Map<?, ?> map,int deep) {
        LOGGER.debug("buildMap {} {}", map,deep);
        builder.append(LST_S);
        addNewLine();
        
        deep++;
        if (map != null && !map.isEmpty()) {
             for (Object key : map.keySet()) {
                addTab(deep);
                builder.append(OBJ_S);
                builder.append(ENTITY_SEP).append(key).append(ENTITY_SEP).append(DOTS);
                Object value = map.get(key);
                if (value != null) {
                    builder.append(ENTITY_SEP).append(cleanJSON(value.toString())).append(ENTITY_SEP);
                } else {
                    builder.append(ENTITY_SEP).append(ENTITY_SEP);
                }
                builder.append(OBJ_E);
                builder.append(COMMA);
                addNewLine();
            }
        }
        addTab(deep);
        builder.append(OBJ_S);
        addKey("X-Generator");
        builder.append(ENTITY_SEP).append("JSONLog2j2Layout").append(ENTITY_SEP);
        builder.append(OBJ_E);
        addNewLine();
        deep--;
        addTab(deep);
        builder.append(LST_E);
    }

    /**
     * Adds the key.
     *
     * @param key the key
     */
    private void addKey(String key) {
        builder.append(ENTITY_SEP).append(key).append(ENTITY_SEP).append(DOTS);
    }
    
    /**
     * Adds the tab.
     *
     * @param deep the deep
     */
    private void addTab(int deep) {
        if(!singleLine) {
            for(int i =0;i<deep;i++) {
                builder.append('\t');    
            }
        }   
    }
    

    /**
     * Adds the field.
     *
     * @param builder the builder
     * @param key     the key
     * @param value   the value
     * @param comma   the comma
     */
    private void addField(String key, Object value, boolean comma,int deep) {
        LOGGER.debug("addField {}={} ({},{})", key, value, comma,deep);
        deep++;
        addTab(deep);
        
        if (value == null) {
            LOGGER.debug("addField value is null");
            addKey(key);
            builder.append(ENTITY_SEP).append(ENTITY_SEP);

        } else if (value instanceof String) {
            LOGGER.debug("addField value is String");
            addKey(key);
            builder.append(ENTITY_SEP).append(cleanJSON(value.toString())).append(ENTITY_SEP);

        } else if (value instanceof Number) {
            LOGGER.debug("addField value is Number");
            addKey(key);
            builder.append(value);

        } else if (value instanceof ContextStack) {
            LOGGER.debug("addField value is ContextStack");
            List<String> stack = ((ContextStack) value).asList();
            addKey(key);
            builder.append(LST_S);
            if (stack != null && !stack.isEmpty()) {
                for (int i = 0; i < stack.size(); i++) {
                    builder.append(ENTITY_SEP).append(cleanJSON(stack.get(i))).append(ENTITY_SEP);
                    if (i < stack.size() - 1) {
                        builder.append(COMMA);
                    }
                }
            }
            builder.append(LST_E);

        } else if (value instanceof Map) {
            LOGGER.debug("addField value is Map");
            addKey(key);
            buildMap((Map<?, ?>) value,deep);

        } else if (value instanceof Throwable) {
            LOGGER.debug("addField value is Throwable");
            Throwable t = (Throwable) value;
            addKey(key);
            addFieldException(t,deep);
        }
        
        if (comma) {
            builder.append(COMMA);
        }
        if(!singleLine) {
            addNewLine();
        }
    }

    /**
     * Adds the field exception.
     *
     * @param builder the builder
     * @param t the t
     */
    private void addFieldException(Throwable t,int deep) {
        builder.append(OBJ_S);
        if(!singleLine) {
            addNewLine();
        }
        addField("exception_class", t.getClass().getCanonicalName(),true,deep);
        addField("exception_message", cleanJSON(t.getMessage()),true,deep);
        buildStackTrace(t.getStackTrace(), deep);
        addTab(deep);
        builder.append(OBJ_E);  
    }
}
