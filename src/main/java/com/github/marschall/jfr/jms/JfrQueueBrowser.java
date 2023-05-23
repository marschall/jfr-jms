package com.github.marschall.jfr.jms;

import java.util.Enumeration;
import java.util.Objects;

import jakarta.jms.JMSException;
import jakarta.jms.Queue;
import jakarta.jms.QueueBrowser;

final class JfrQueueBrowser implements QueueBrowser {
  
  private final QueueBrowser delegate;
  private final String queueName;
  private final String messageSelector;

  JfrQueueBrowser(QueueBrowser delegate, String queueName, String messageSelector) {
    Objects.requireNonNull(delegate, "delegate");
    this.queueName = queueName;
    this.messageSelector = messageSelector;
    this.delegate = delegate;
  }

  @Override
  public Queue getQueue() throws JMSException {
    return delegate.getQueue();
  }

  @Override
  public String getMessageSelector() throws JMSException {
    return delegate.getMessageSelector();
  }

  @Override
  @SuppressWarnings("rawtypes") // JMS uses raw types
  public Enumeration getEnumeration() throws JMSException {
    return new JfrEnumeration(delegate.getEnumeration(), queueName, messageSelector);
  }

  @Override
  public void close() throws JMSException {
    delegate.close();
  }

}
