package com.github.marschall.jfr.jms;

import java.io.Serializable;
import java.util.Objects;

import javax.jms.BytesMessage;
import javax.jms.ConnectionMetaData;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.StreamMessage;
import javax.jms.TemporaryQueue;
import javax.jms.TemporaryTopic;
import javax.jms.TextMessage;
import javax.jms.Topic;

/**
 * A JMS context that wraps an other one and generates Flight Recorder events.
 */
public class JfrJMSContext implements JMSContext {

  private final JMSContext delegate;

  /**
   * Constructs a new JFR JMS context.
   * 
   * @param delegate the actual JMS context to delegate to, not {@code null}
   */
  public JfrJMSContext(JMSContext delegate) {
    Objects.requireNonNull(delegate, "delegate");
    this.delegate = delegate;
  }

  @Override
  public JMSContext createContext(int sessionMode) {
    return this.delegate.createContext(sessionMode);
  }

  @Override
  public JMSProducer createProducer() {
    JmsOperationEvent event = newJMSContextEvent("createProducer");
    event.begin();
    JMSProducer producer;
    try {
      producer = this.delegate.createProducer();
    } finally {
      event.end();
      event.commit();
    }
    return new JfrJMSProducer(producer);
  }

  @Override
  public String getClientID() {
    return this.delegate.getClientID();
  }

  @Override
  public void setClientID(String clientID) {
    this.delegate.setClientID(clientID);
  }

  @Override
  public ConnectionMetaData getMetaData() {
    return this.delegate.getMetaData();
  }

  @Override
  public ExceptionListener getExceptionListener() {
    return this.delegate.getExceptionListener();
  }

  @Override
  public void setExceptionListener(ExceptionListener listener) {
    this.delegate.setExceptionListener(listener);
  }

  @Override
  public void start() {
    this.delegate.start();
  }

  @Override
  public void stop() {
    this.delegate.stop();
  }

  @Override
  public void setAutoStart(boolean autoStart) {
    this.delegate.setAutoStart(autoStart);
  }

  @Override
  public boolean getAutoStart() {
    return this.delegate.getAutoStart();
  }

  @Override
  public void close() {
    JmsOperationEvent event = newJMSContextEvent("close");
    event.begin();
    try {
      this.delegate.close();
    } finally {
      event.end();
      event.commit();
    }
  }

  @Override
  public BytesMessage createBytesMessage() {
    return this.delegate.createBytesMessage();
  }

  @Override
  public MapMessage createMapMessage() {
    return this.delegate.createMapMessage();
  }

  @Override
  public Message createMessage() {
    return this.delegate.createMessage();
  }

  @Override
  public ObjectMessage createObjectMessage() {
    return this.delegate.createObjectMessage();
  }

  @Override
  public ObjectMessage createObjectMessage(Serializable object) {
    return this.delegate.createObjectMessage(object);
  }

  @Override
  public StreamMessage createStreamMessage() {
    return this.delegate.createStreamMessage();
  }

  @Override
  public TextMessage createTextMessage() {
    return this.delegate.createTextMessage();
  }

  @Override
  public TextMessage createTextMessage(String text) {
    return this.delegate.createTextMessage(text);
  }

  @Override
  public boolean getTransacted() {
    return this.delegate.getTransacted();
  }

  @Override
  public int getSessionMode() {
    return this.delegate.getSessionMode();
  }

  @Override
  public void commit() {
    JmsOperationEvent event = newJMSContextEvent("commit");
    event.begin();
    try {
      this.delegate.commit();
    } finally {
      event.end();
      event.commit();
    }
  }

  @Override
  public void rollback() {
    JmsOperationEvent event = newJMSContextEvent("rollback");
    event.begin();
    try {
      this.delegate.rollback();
    } finally {
      event.end();
      event.commit();
    }
  }

  @Override
  public void recover() {
    JmsOperationEvent event = newJMSContextEvent("recover");
    event.begin();
    try {
      this.delegate.recover();
    } finally {
      event.end();
      event.commit();
    }
  }

  @Override
  public JMSConsumer createConsumer(Destination destination) {
    return this.delegate.createConsumer(destination);
  }

  @Override
  public JMSConsumer createConsumer(Destination destination, String messageSelector) {
    return this.delegate.createConsumer(destination, messageSelector);
  }

  @Override
  public JMSConsumer createConsumer(Destination destination, String messageSelector, boolean noLocal) {
    return this.delegate.createConsumer(destination, messageSelector, noLocal);
  }

  @Override
  public Queue createQueue(String queueName) {
    return this.delegate.createQueue(queueName);
  }

  @Override
  public Topic createTopic(String topicName) {
    return this.delegate.createTopic(topicName);
  }

  @Override
  public JMSConsumer createDurableConsumer(Topic topic, String name) {
    return this.delegate.createDurableConsumer(topic, name);
  }

  @Override
  public JMSConsumer createDurableConsumer(Topic topic, String name, String messageSelector, boolean noLocal) {
    return this.delegate.createDurableConsumer(topic, name, messageSelector, noLocal);
  }

  @Override
  public JMSConsumer createSharedDurableConsumer(Topic topic, String name) {
    return this.delegate.createSharedDurableConsumer(topic, name);
  }

  @Override
  public JMSConsumer createSharedDurableConsumer(Topic topic, String name, String messageSelector) {
    return this.delegate.createSharedDurableConsumer(topic, name, messageSelector);
  }

  @Override
  public JMSConsumer createSharedConsumer(Topic topic, String sharedSubscriptionName) {
    return this.delegate.createSharedConsumer(topic, sharedSubscriptionName);
  }

  @Override
  public JMSConsumer createSharedConsumer(Topic topic, String sharedSubscriptionName, String messageSelector) {
    return this.delegate.createSharedConsumer(topic, sharedSubscriptionName, messageSelector);
  }

  @Override
  public QueueBrowser createBrowser(Queue queue) {
    QueueBrowser browser = this.delegate.createBrowser(queue);
    String queueName = DestinationUtil.getQueueNameSafe(queue);
    return new JfrQueueBrowser(browser, queueName, null);
  }

  @Override
  public QueueBrowser createBrowser(Queue queue, String messageSelector) {
    QueueBrowser browser = this.delegate.createBrowser(queue);
    String queueName = DestinationUtil.getQueueNameSafe(queue);
    return new JfrQueueBrowser(browser, queueName, messageSelector);
  }

  @Override
  public TemporaryQueue createTemporaryQueue() {
    return this.delegate.createTemporaryQueue();
  }

  @Override
  public TemporaryTopic createTemporaryTopic() {
    return this.delegate.createTemporaryTopic();
  }

  @Override
  public void unsubscribe(String name) {
    this.delegate.unsubscribe(name);
  }

  @Override
  public void acknowledge() {
    JmsOperationEvent event = newJMSContextEvent("acknowledge");
    event.begin();
    try {
      this.delegate.acknowledge();
    } finally {
      event.end();
      event.commit();
    }
  }

  private static JmsOperationEvent newJMSContextEvent(String operationName) {
    JmsOperationEvent event = new JmsOperationEvent();
    event.operationObject = "JMSContext";
    event.operationName = operationName;
    return event;
  }

}
