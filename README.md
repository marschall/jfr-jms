JFR JMS [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.marschall/jfr-jms/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.marschall/jfr-jms) [![Javadocs](https://www.javadoc.io/badge/com.github.marschall/jfr-jms.svg)](https://www.javadoc.io/doc/com.github.marschall/jfr-jms)
=======

A JMS driver that wraps an other JDBC driver and generates JFR events.

```xml
<dependency>
  <groupId>com.github.marschall</groupId>
  <artifactId>jfr-jms</artifactId>
  <version>0.1.0-SNAPSHOT</version>
</dependency>
```

Usage
-----

Simply wrap your `ConnectionFactory` with a `JfrConnectionFactory`

```java
new JfrConnectionFactory(connectionFactory)
```

or your `JMSContext` with a `JfrJMSContext`


```java
new JfrJMSContext(jmsContext)
```

Implementation/Overhead
-----------------------

The implementation is based around wrapper objects.

* no reflection
* no string concatenation

We assume `javax.jms.Queue#getQueueName()` and `javax.jms.Topic#getTopicName()` are simple getters.

