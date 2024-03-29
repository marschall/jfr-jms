package com.github.marschall.jfr.jms;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.Queue;
import jakarta.jms.QueueBrowser;
import jakarta.jms.Session;
import jakarta.jms.TextMessage;

import org.apache.activemq.junit.EmbeddedActiveMQBroker;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.jms.core.JmsOperations;
import org.springframework.jms.core.JmsTemplate;

import com.github.marschall.jakartajmsadapter.JakartaQueueConnectionFactory;

public class JfrConnectionFactoryTest {

  @Rule
  public EmbeddedActiveMQBroker broker = new EmbeddedActiveMQBroker();

  private JmsOperations template;

  @Before
  public void setUp() {
    ConnectionFactory connectionFactory = new JakartaQueueConnectionFactory(broker.createConnectionFactory());
    this.template = new JmsTemplate(new JfrConnectionFactory(connectionFactory));
  }

  @Test
  public void createQueue() throws JMSException {
    String queueName = "queue_name";
    Queue queue = this.template.execute((Session session) ->  session.createQueue(queueName));
    assertNotNull(queue);
  }

  @Test
  public void sendAndReceive() throws JMSException {
    String queueName = "queue_name";
    this.template.send(queueName, (Session session) -> session.createTextMessage("message_text"));

    Message received = this.template.receive(queueName);

    assertTrue(received instanceof TextMessage);
    assertEquals("message_text", ((TextMessage) received).getText());
  }

  @Test
  public void browse() throws JMSException {
    String queueName = "queue_name";
    this.template.send(queueName, (Session session) -> session.createTextMessage("message_text"));

    List<String> messageIds = this.template.browse(queueName, (Session session, QueueBrowser browser) -> {
      Enumeration en = browser.getEnumeration();
      List<String> ids = new ArrayList<>();
      while (en.hasMoreElements()) {
        Message message = (Message) en.nextElement();
        ids.add(message.getJMSMessageID());
      }
      return ids;
    });
    assertEquals(1, messageIds.size());
  }

}
