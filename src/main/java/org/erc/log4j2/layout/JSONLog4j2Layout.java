package org.erc.log4j2.layout;

import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import org.apache.logging.log4j.Logger;
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

    /** The Constant ISO_DATETIME_TIME_ZONE_FORMAT_WITH_MILLIS. */
    private static final DateFormat ISO_DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    /** The location info. */
    private boolean locationInfo = false;

    /** The single line. */
    private boolean singleLine = false;

    /** The html safe. */
    private boolean htmlSafe = false;

    /** The plain context map. */
    private boolean plainContextMap = false;

    /** The new line format. */
    private String newLineFormat;
    
    /** The user fields. */
    private UserField[] userFields;

    /**
     * Instantiates a new JSON log 4 j 2 layout.
     *
     * @param locationInfo    the location info
     * @param singleLine      the single line
     * @param htmlSafe        the html safe
     * @param newLineFormat the new line format
     * @param plainContextMap the plain context map
     * @param userFields      the user fields
     * @param charset         the charset
     */
    protected JSONLog4j2Layout(
            boolean locationInfo, 
            boolean singleLine, 
            boolean htmlSafe,
            String newLineFormat,
            boolean plainContextMap,
            UserField[] userFields, 
            Charset charset) {
        
        super(charset);
        this.newLineFormat = newLineFormat;
        this.locationInfo = locationInfo;
        this.singleLine = singleLine;
        this.plainContextMap = plainContextMap;
        this.userFields = userFields;
        this.htmlSafe = htmlSafe;
        ISO_DATETIME_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    /**
     * Creates the layout.
     *
     * @param config          Log4j Configuration
     * @param locationInfo    the location info
     * @param singleLine      the single line
     * @param htmlSafe        the html safe
     * @param newLineFormat the new line format
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
            @PluginAttribute("newLineFormat") String newLineFormat,
            @PluginAttribute("plainContextMap") boolean plainContextMap, 
            @PluginAttribute("charset") Charset charset,
            @PluginElement("UserFields") final UserField[] userFields
// @formatter:on
    ) {
        Charset finalCharset = null;
        if (charset == null) {
            finalCharset = Charset.forName("UTF-8");
        }else {
            finalCharset = charset;
        }
        
        LOGGER.debug("Creating JSONLog4j2Layout {}", finalCharset);
        return new JSONLog4j2Layout(
                locationInfo, singleLine, htmlSafe, newLineFormat, 
                plainContextMap, userFields, finalCharset
        );
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

    /**
     * To serializable.
     *
     * @param event the event
     * @return the string
     */
    /*
     * (non-Javadoc)
     * 
     * @see
     * org.apache.logging.log4j.core.Layout#toSerializable(org.apache.logging.log4j.
     * core.LogEvent)
     */
    public String toSerializable(LogEvent event) {
        JSONBuilder builder = new JSONBuilder();
        builder.start(htmlSafe,singleLine,newLineFormat);
        if (event != null) {
            toSerializableEvent(builder, event);    
        }
        toSerializableEventUserFields(builder);
        builder.addField("@timestamp", ISO_DATETIME_FORMAT.format(event.getTimeMillis()));
        
        return builder.end();
    }

    /**
     * Internal operation toSerializable of the event part.
     *
     * @param builder builder
     * @param event   event
     */
    private void toSerializableEvent(JSONBuilder builder, LogEvent event) {
        builder.addField("logger_name", event.getLoggerName());

        if (event.getLevel() != null) {
            builder.addField( "level", event.getLevel().name());
            builder.addField( "level_int", event.getLevel().intLevel());
        }

        builder.addField( "thread_name", event.getThreadName());
        builder.addField( "source_host", getHostName());
        builder.addField( "source_ip", getHostIP());

        if (event.getMessage() != null) {
            builder.addField( "message", event.getMessage().getFormattedMessage());
        }

        if (event.getThrown() != null) {
            builder.addField( "exception", event.getThrown());
        }

        if (locationInfo && event.getSource()!=null) {
            builder.addField( "file", event.getSource().getFileName());
            builder.addField( "line_number", event.getSource().getLineNumber());
            builder.addField( "class", event.getSource().getClassName());
            builder.addField( "method", event.getSource().getMethodName());
        }

        if (event.getContextStack() != null && !event.getContextStack().isEmpty()) {
            builder.addField( "contextStack", event.getContextStack());
        }

        if (event.getContextData() != null && !event.getContextData().isEmpty()) {
            if (plainContextMap) {
                event.getContextData().forEach((k, v) -> builder.addField(k, v));
            } else {
                builder.addField( "contextMap", event.getContextData().toMap());
            }
        }
    }
    
    /**
     * to Seriazliable event, user custom data.
     *
     * @param builder   builder
     * @param event     event
     */
    private void toSerializableEventUserFields(JSONBuilder builder) {
        if (userFields != null) {
            for (UserField userField : userFields) {
                builder.addField( userField.getKey(), userField.getValue());
            }
        }
    }
}
