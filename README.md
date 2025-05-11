# log4j2-jsonevent-layout

[![GitHub license](https://img.shields.io/badge/license-LGPL-blue.svg)](https://raw.githubusercontent.com/dubasdey/log4j2-jsonevent-layout/master/LICENSE)
[![GitHub issues](https://img.shields.io/github/issues/dubasdey/log4j2-jsonevent-layout.svg)](https://github.com/dubasdey/log4j2-jsonevent-layout/issues)
[![CI Build Status](https://github.com/dubasdey/log4j2-jsonevent-layout/actions/workflows/maven.yml/badge.svg)](https://github.com/dubasdey/log4j2-jsonevent-layout/actions/workflows/maven.yml)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.dubasdey/log4j2-jsonevent-layout.svg)](https://search.maven.org/artifact/com.github.dubasdey/log4j2-jsonevent-layout)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/d964db30361149b797ec3f9b80e5f731)](https://www.codacy.com/gh/dubasdey/log4j2-jsonevent-layout/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=dubasdey/log4j2-jsonevent-layout&amp;utm_campaign=Badge_Grade)
[![Codacy Badge](https://app.codacy.com/project/badge/Coverage/d964db30361149b797ec3f9b80e5f731)](https://www.codacy.com/gh/dubasdey/log4j2-jsonevent-layout/dashboard?utm_source=github.com&utm_medium=referral&utm_content=dubasdey/log4j2-jsonevent-layout&utm_campaign=Badge_Coverage)



Log4j2 JSON Event Layout without requirement of thirdparty libraries

## Usage

Add the `<JSONLog4j2Layout>` tag to any appender to use it.



**Note:** It could be required to add to the configuration node in the packages attribute the package of the plugin.

Example:

    <Configuration packages="org.erc.log4j2.layout">

Check the Log4j2 configuration java doc for more references 
https://logging.apache.org/log4j/2.x/manual/configuration.html


### Optional Attributes

* locationInfo - Adds location info to the Trace
* singleLine - Removes \r and \n in JSON String
* htmlSafe - Escapes additional characters to print the JSON on HTML pages.
* newLineFormat - Custom new line for each line (if singleLine is off) or the final line (if singleLine is on)
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




## Maven dependency

To include this library in your project just add the dependency to your maven project

Example:
```
<dependency>
  <groupId>com.github.dubasdey</groupId>
  <artifactId>log4j2-jsonevent-layout</artifactId>
  <version>0.1.0</version>
</dependency>
```


## Donate
Buy me a coffe to help me continue supporting this project. 
<a href="https://www.paypal.com/donate/?hosted_button_id=K6DQ5GLE8KHGY">Buy me a coffe</a>



# Security

You could check any important security information at the [security document](SECURITY.md)



# How-Tos


### Use the appender formatter to send data to logstash 


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