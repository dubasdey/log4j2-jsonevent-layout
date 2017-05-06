# log4j2-jsonevent-layout

[![GitHub license](https://img.shields.io/badge/license-LGPL-blue.svg)](https://raw.githubusercontent.com/dubasdey/log4j2-jsonevent-layout/master/LICENSE)
[![Build Status](https://travis-ci.org/dubasdey/log4j2-jsonevent-layout.svg?branch=master)](https://travis-ci.org/dubasdey/log4j2-jsonevent-layout)


Log4j2 JSON Event Layout without requirement of thirdparty libraries

## Usage

Add the `<JSONLog4j2Layout>` tag to any appender.

### Optional Attributes

* locationInfo - Adds location info to the Trace
* singleLine - Removes \r and \n in JSON String
* htmlSafe - Escapes additional characters to print the JSON on HTML pages.
* plainContextMap - Prints the content of the ContextMap in the root as __key:value__ instead of a contextMap object with the values
* charset - Charset to use (Default UTF-8)
* UserField - Collection of user fields with __key__ and __value__ that will be printed in the LogEntry


## Result Example

Example expanded to multiple lines

```json
{
"@timestamp":"2017-05-03T15:46:34.393Z",
"logger_name":"org.dummy.logger",
"level":"DEBUG",
"level_int":500,
"thread_name":"DummyThread",
"source_host":"MYLOCALMACHINE",
"source_ip":"192.168.1.10",
"message":"Dummy Message Test with tab T\tT before here.",
"file":"File.java",
"line_number":42,
"class":"ClassName.class",
"method":"MethodElement",
"contextStack":["Entry 1","Entry 2"],
"contextMap":
    [
       {"X-Generator":"JSONLog2j2Layout"},
       {"ThreadContextKey":"ThreadContextValue"}
    ],
"@version":"1"
}
```

## How-To

### Send to logstash 

1.- To send to logstash, add the jar to your application classpath and configure a log4j2 socket appender with the host and port used by logstash. Then add to the desired loggers.

```xml
<Socket name="socketAppender" host="myhost" port="9201">
      <JSONLog4j2Layout singleLine="true" />
</Socket>
```



2.- Configure logstash using tcp input in server mode with the same port.

```
input {
    tcp {
        port => 9201
        mode => "server"
        codec => json_lines
    }
}
```



