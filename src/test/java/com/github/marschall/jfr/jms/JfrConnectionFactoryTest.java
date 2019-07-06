package com.github.marschall.jfr.jms;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.junit.EmbeddedActiveMQBroker;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.jms.core.JmsOperations;
import org.springframework.jms.core.JmsTemplate;

public class JfrConnectionFactoryTest {
  
  @Rule
  public EmbeddedActiveMQBroker broker = new EmbeddedActiveMQBroker();
  
  private JmsOperations template;

  @Before
  public void setUp() {
    ConnectionFactory connectionFactory = broker.createConnectionFactory();
    this.template = new JmsTemplate(new JfrConnectionFactory(connectionFactory));
  }

  @Test
  public void test() throws JMSException {
    Queue queue = this.template.execute((Session session) ->  session.createQueue("queue_name"));
    this.template.send(queue, (Session session) -> session.createTextMessage("message_text"));
    Message received = this.template.receive(queue);
    assertTrue(received instanceof TextMessage);
    assertEquals("message_text", ((TextMessage) received).getText());
  }

}
