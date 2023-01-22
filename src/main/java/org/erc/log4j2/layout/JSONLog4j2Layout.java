package org.erc.log4j2.layout;

import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext.ContextStack;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.AbstractStringLayout;
import org.apache.logging.log4j.status.StatusLogger;

/**
 * The Class JSONLog4j2Layout.
 */
@Plugin(name = "JSONLog4j2Layout", category = "Core", elementType = "layout", printObject = true)
public class JSONLog4j2Layout extends AbstractStringLayout {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = StatusLogger.getLogger();

    /** The Constant VERSION. */
    private static final String VERSION = "1";

    /** The Constant ENTITY_SEP. */
    private static final String ENTITY_SEP = "\"";

    /** The Constant OBJ_S. */
    private static final String OBJ_S = "{";

    /** The Constant OBJ_E. */
    private static final String OBJ_E = "}";

    /** The Constant LST_S. */
    private static final String LST_S = "[";

    /** The Constant LST_E. */
    private static final String LST_E = "]";

    /** The Constant COMMA. */
    private static final String COMMA = ",";

    /** The Constant DOTS. */
    private static final String DOTS = ":";

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

    /** The Constant ISO_DATETIME_TIME_ZONE_FORMAT_WITH_MILLIS. */
    private static final DateFormat ISO_DATETIME_TIME_ZONE_FORMAT_WITH_MILLIS = new SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    /** The location info. */
    private boolean locationInfo = false;

    /** The single line. */
    private boolean singleLine = false;

    /** The html safe. */
    private boolean htmlSafe = false;

    /** The plain context map. */
    private boolean plainContextMap = false;

    /** The user fields. */
    private UserField[] userFields;

    /**
     * Instantiates a new JSON log 4 j 2 layout.
     *
     * @param locationInfo    the location info
     * @param singleLine      the single line
     * @param htmlSafe        the html safe
     * @param plainContextMap the plain context map
     * @param userFields      the user fields
     * @param charset         the charset
     */
    protected JSONLog4j2Layout(boolean locationInfo, boolean singleLine, boolean htmlSafe, boolean plainContextMap,
            UserField[] userFields, Charset charset) {
        super(charset);
        this.locationInfo = locationInfo;
        this.singleLine = singleLine;
        this.plainContextMap = plainContextMap;
        this.userFields = userFields;
        ISO_DATETIME_TIME_ZONE_FORMAT_WITH_MILLIS.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    /**
     * Creates the layout.
     *
     * @param config          Log4j Configuration
     * @param locationInfo    the location info
     * @param singleLine      the single line
     * @param htmlSafe        the html safe
     * @param plainContextMap the plain context map
     * @param charset         the charset
     * @param userFields      the user fields
     * @return the JSON log 4 j 2 layout
     */
    @PluginFactory
    public static JSONLog4j2Layout createLayout(
// @formatter:off
			@PluginConfiguration final Configuration config,
			@PluginAttribute("locationInfo") boolean locationInfo,
			@PluginAttribute("singleLine") boolean singleLine,
			@PluginAttribute("htmlSafe") boolean htmlSafe,
			@PluginAttribute("plainContextMap") boolean plainContextMap, 
			@PluginAttribute("charset") Charset charset,
			@PluginElement("UserFields") final UserField[] userFields
// @formatter:on
    ) {

        if (charset == null) {
            charset = Charset.forName("UTF-8");
        }

        LOGGER.debug("Creating JSONLog4j2Layout {}", charset);
        return new JSONLog4j2Layout(locationInfo, singleLine, htmlSafe, plainContextMap, userFields, charset);
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
    private String getStackTrace(StackTraceElement[] traces) {
        StringBuilder builder = new StringBuilder();
        if (traces != null) {
            for (StackTraceElement trace : traces) {
                builder.append(cleanJSON(trace.toString())).append("\n");
            }
        }
        return builder.toString();
    }

    /**
     * Adds the field.
     *
     * @param builder the builder
     * @param key     the key
     * @param value   the value
     */
    private void addField(StringBuilder builder, String key, Object value) {
        addField(builder, key, value, true);
    }

    /**
     * Gets the map.
     *
     * @param map the map
     * @return the map
     */
    private String getMap(Map<?, ?> map) {
        StringBuilder builder = new StringBuilder();
        builder.append(LST_S);
        if (map != null && !map.isEmpty()) {
            for (Object key : map.keySet()) {
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
            }
        }
        builder.append(OBJ_S);
        builder.append(ENTITY_SEP).append("X-Generator").append(ENTITY_SEP).append(DOTS);
        builder.append(ENTITY_SEP).append("JSONLog2j2Layout").append(ENTITY_SEP);
        builder.append(OBJ_E);
        builder.append(LST_E);
        return builder.toString();
    }

    /**
     * Adds the field.
     *
     * @param builder the builder
     * @param key     the key
     * @param value   the value
     * @param comma   the comma
     */
    private void addField(StringBuilder builder, String key, Object value, boolean comma) {
        LOGGER.debug("addField {}={} ({})", key, value, comma);
        if (value == null) {
            builder.append(ENTITY_SEP).append(key).append(ENTITY_SEP).append(DOTS).append(ENTITY_SEP)
                    .append(ENTITY_SEP);

        } else if (value instanceof String) {
            builder.append(ENTITY_SEP).append(key).append(ENTITY_SEP).append(DOTS);
            builder.append(ENTITY_SEP).append(cleanJSON(value.toString())).append(ENTITY_SEP);

        } else if (value instanceof Number) {
            builder.append(ENTITY_SEP).append(key).append(ENTITY_SEP).append(DOTS).append(value);

        } else if (value instanceof ContextStack) {
            List<String> stack = ((ContextStack) value).asList();
            builder.append(ENTITY_SEP).append(key).append(ENTITY_SEP).append(DOTS);
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
            builder.append(ENTITY_SEP).append(key).append(ENTITY_SEP).append(DOTS).append(getMap((Map<?, ?>) value));

        } else if (value instanceof Throwable) {
            Throwable t = (Throwable) value;
            builder.append(ENTITY_SEP).append(key).append(ENTITY_SEP).append(DOTS);

            builder.append(OBJ_S);
            addField(builder, "exception_class", t.getClass().getCanonicalName());
            addField(builder, "exception_message", cleanJSON(t.getMessage()));
            addField(builder, "stacktrace", getStackTrace(t.getStackTrace()), false);
            builder.append(OBJ_E);
        }
        if (comma) {
            builder.append(COMMA);
        }
    }

    /**
     * Gets the host name.
     *
     * @return the host name
     */
    private String getHostName() {
        String hostName = "unknown-host";
        try {
            hostName = java.net.InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            /* Ignored */
        }
        return hostName;
    }

    /**
     * Gets the host IP.
     *
     * @return the host IP
     */
    private String getHostIP() {
        String hostName = "unknown-address";
        try {
            hostName = java.net.InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            /* Ignored */
        }
        return hostName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.apache.logging.log4j.core.Layout#toSerializable(org.apache.logging.log4j.
     * core.LogEvent)
     */
    public String toSerializable(LogEvent event) {
        StringBuilder builder = new StringBuilder();

        builder.append(OBJ_S);
        addField(builder, "@timestamp", ISO_DATETIME_TIME_ZONE_FORMAT_WITH_MILLIS.format(event.getTimeMillis()));

        if (event != null) {
            toSerializableEvent(builder, event);
            toSerializableEventUserFields(builder,event);
        }
        addField(builder, "@version", VERSION, false);
        builder.append(OBJ_E);
        builder.append("\n");
        return builder.toString();
    }

    /**
     * Internal operation toSerializable of the event part
     * 
     * @param builder builder
     * @param event   event
     */
    private void toSerializableEvent(StringBuilder builder, LogEvent event) {
        addField(builder, "logger_name", event.getLoggerName());

        if (event.getLevel() != null) {
            addField(builder, "level", event.getLevel().name());
            addField(builder, "level_int", event.getLevel().intLevel());
        }

        addField(builder, "thread_name", event.getThreadName());
        addField(builder, "source_host", getHostName());
        addField(builder, "source_ip", getHostIP());

        if (event.getMessage() != null) {
            addField(builder, "message", event.getMessage().getFormattedMessage());
        }

        if (event.getThrown() != null) {
            addField(builder, "exception", event.getThrown());
        }

        if (locationInfo) {
            addField(builder, "file", event.getSource().getFileName());
            addField(builder, "line_number", event.getSource().getLineNumber());
            addField(builder, "class", event.getSource().getClassName());
            addField(builder, "method", event.getSource().getMethodName());
        }

        if (event.getContextStack() != null) {
            addField(builder, "contextStack", event.getContextStack());
        }

        if (event.getContextData() != null) {
            if (plainContextMap) {
                event.getContextData().forEach((k, v) -> addField(builder, k, v));
            } else {
                addField(builder, "contextMap", event.getContextData());
            }
        }
    }
    
    /**
     * to Seriazliable event, user custom data
     * @param builder   builder
     * @param event     event
     */
    private void toSerializableEventUserFields(StringBuilder builder, LogEvent event) {
        if (userFields != null) {
            for (UserField userField : userFields) {
                addField(builder, userField.getKey(), userField.getValue());
            }
        }
    }
}
