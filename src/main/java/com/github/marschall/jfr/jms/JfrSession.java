package com.github.marschall.jfr.jms;

import java.io.Serializable;
import java.util.Objects;

import jakarta.jms.BytesMessage;
import jakarta.jms.Destination;
import jakarta.jms.JMSException;
import jakarta.jms.MapMessage;
import jakarta.jms.Message;
import jakarta.jms.MessageConsumer;
import jakarta.jms.MessageListener;
import jakarta.jms.MessageProducer;
import jakarta.jms.ObjectMessage;
import jakarta.jms.Queue;
import jakarta.jms.QueueBrowser;
import jakarta.jms.Session;
import jakarta.jms.StreamMessage;
import jakarta.jms.TemporaryQueue;
import jakarta.jms.TemporaryTopic;
import jakarta.jms.TextMessage;
import jakarta.jms.Topic;
import jakarta.jms.TopicSubscriber;

final class JfrSession implements Session {

  private final Session delegate;

  JfrSession(Session delegate) {
    Objects.requireNonNull(delegate, "delegate");
    this.delegate = delegate;
  }

  @Override
  public BytesMessage createBytesMessage() throws JMSException {
    return this.delegate.createBytesMessage();
  }

  @Override
  public MapMessage createMapMessage() throws JMSException {
    return this.delegate.createMapMessage();
  }

  @Override
  public Message createMessage() throws JMSException {
    return this.delegate.createMessage();
  }

  @Override
  public ObjectMessage createObjectMessage() throws JMSException {
    return this.delegate.createObjectMessage();
  }

  @Override
  public ObjectMessage createObjectMessage(Serializable object) throws JMSException {
    return this.delegate.createObjectMessage(object);
  }

  @Override
  public StreamMessage createStreamMessage() throws JMSException {
    return this.delegate.createStreamMessage();
  }

  @Override
  public TextMessage createTextMessage() throws JMSException {
    return this.delegate.createTextMessage();
  }

  @Override
  public TextMessage createTextMessage(String text) throws JMSException {
    return this.delegate.createTextMessage(text);
  }

  @Override
  public boolean getTransacted() throws JMSException {
    return this.delegate.getTransacted();
  }

  @Override
  public int getAcknowledgeMode() throws JMSException {
    return this.delegate.getAcknowledgeMode();
  }

  @Override
  public void commit() throws JMSException {
    JmsOperationEvent event = newSessionEvent("commit");
    event.begin();
    try {
      this.delegate.commit();
    } finally {
      event.end();
      event.commit();
    }
  }

  @Override
  public void rollback() throws JMSException {
    JmsOperationEvent event = newSessionEvent("rollback");
    event.begin();
    try {
      this.delegate.rollback();
    } finally {
      event.end();
      event.commit();
    }
  }

  @Override
  public void close() throws JMSException {
    JmsOperationEvent event = newSessionEvent("close");
    event.begin();
    try {
      this.delegate.close();
    } finally {
      event.end();
      event.commit();
    }
  }

  @Override
  public void recover() throws JMSException {
    JmsOperationEvent event = newSessionEvent("recover");
    event.begin();
    try {
      this.delegate.recover();
    } finally {
      event.end();
      event.commit();
    }
  }

  @Override
  public MessageListener getMessageListener() throws JMSException {
    return this.delegate.getMessageListener();
  }

  @Override
  public void setMessageListener(MessageListener listener) throws JMSException {
    this.delegate.setMessageListener(listener);
  }

  @Override
  public void run() {
    this.delegate.run();
  }

  @Override
  public MessageProducer createProducer(Destination destination) throws JMSException {
    JmsOperationEvent event = newSessionEvent("createProducer");
    event.begin();
    MessageProducer messageProducer;
    try {
      messageProducer = this.delegate.createProducer(destination);
    } finally {
      event.end();
      event.commit();
    }
    return new JfrMessageProducer(messageProducer, DestinationUtil.getDestinationName(destination));
  }

  @Override
  public MessageConsumer createConsumer(Destination destination) throws JMSException {
    return this.delegate.createConsumer(destination);
  }

  @Override
  public MessageConsumer createConsumer(Destination destination, String messageSelector) throws JMSException {
    return this.delegate.createConsumer(destination, messageSelector);
  }

  @Override
  public MessageConsumer createConsumer(Destination destination, String messageSelector, boolean noLocal)
      throws JMSException {
    return this.delegate.createConsumer(destination, messageSelector, noLocal);
  }

  @Override
  public MessageConsumer createSharedConsumer(Topic topic, String sharedSubscriptionName) throws JMSException {
    return this.delegate.createSharedConsumer(topic, sharedSubscriptionName);
  }

  @Override
  public MessageConsumer createSharedConsumer(Topic topic, String sharedSubscriptionName, String messageSelector)
      throws JMSException {
    return this.delegate.createSharedConsumer(topic, sharedSubscriptionName, messageSelector);
  }

  @Override
  public Queue createQueue(String queueName) throws JMSException {
    return this.delegate.createQueue(queueName);
  }

  @Override
  public Topic createTopic(String topicName) throws JMSException {
    return this.delegate.createTopic(topicName);
  }

  @Override
  public TopicSubscriber createDurableSubscriber(Topic topic, String name) throws JMSException {
    return this.delegate.createDurableSubscriber(topic, name);
  }

  @Override
  public TopicSubscriber createDurableSubscriber(Topic topic, String name, String messageSelector, boolean noLocal)
      throws JMSException {
    return this.delegate.createDurableSubscriber(topic, name, messageSelector, noLocal);
  }

  @Override
  public MessageConsumer createDurableConsumer(Topic topic, String name) throws JMSException {
    return this.delegate.createDurableConsumer(topic, name);
  }

  @Override
  public MessageConsumer createDurableConsumer(Topic topic, String name, String messageSelector, boolean noLocal)
      throws JMSException {
    return this.delegate.createDurableConsumer(topic, name, messageSelector, noLocal);
  }

  @Override
  public MessageConsumer createSharedDurableConsumer(Topic topic, String name) throws JMSException {
    return this.delegate.createSharedDurableConsumer(topic, name);
  }

  @Override
  public MessageConsumer createSharedDurableConsumer(Topic topic, String name, String messageSelector)
      throws JMSException {
    return this.delegate.createSharedDurableConsumer(topic, name, messageSelector);
  }

  @Override
  public QueueBrowser createBrowser(Queue queue) throws JMSException {
    QueueBrowser browser = this.delegate.createBrowser(queue);
    String queueName = DestinationUtil.getQueueNameSafe(queue);
    return new JfrQueueBrowser(browser, queueName, null);
  }

  @Override
  public QueueBrowser createBrowser(Queue queue, String messageSelector) throws JMSException {
    QueueBrowser browser = this.delegate.createBrowser(queue);
    String queueName = DestinationUtil.getQueueNameSafe(queue);
    return new JfrQueueBrowser(browser, queueName, messageSelector);
  }

  @Override
  public TemporaryQueue createTemporaryQueue() throws JMSException {
    return this.delegate.createTemporaryQueue();
  }

  @Override
  public TemporaryTopic createTemporaryTopic() throws JMSException {
    return this.delegate.createTemporaryTopic();
  }

  @Override
  public void unsubscribe(String name) throws JMSException {
    this.delegate.unsubscribe(name);
  }

  private static JmsOperationEvent newSessionEvent(String operationName) {
    JmsOperationEvent event = new JmsOperationEvent();
    event.operationObject = "Session";
    event.operationName = operationName;
    return event;
  }

}
