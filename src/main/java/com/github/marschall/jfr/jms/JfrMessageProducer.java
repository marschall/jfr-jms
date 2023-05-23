package com.github.marschall.jfr.jms;

import java.util.Objects;

import jakarta.jms.CompletionListener;
import jakarta.jms.Destination;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageProducer;

final class JfrMessageProducer implements MessageProducer {
  
  private final MessageProducer delegate;
  private String destinationName;

  JfrMessageProducer(MessageProducer delegate, String destinationName) {
    Objects.requireNonNull(delegate, "delegate");
    this.delegate = delegate;
    this.destinationName = destinationName;
  }

  @Override
  public void setDisableMessageID(boolean value) throws JMSException {
    delegate.setDisableMessageID(value);
  }

  @Override
  public boolean getDisableMessageID() throws JMSException {
    return delegate.getDisableMessageID();
  }

  @Override
  public void setDisableMessageTimestamp(boolean value) throws JMSException {
    delegate.setDisableMessageTimestamp(value);
  }

  @Override
  public boolean getDisableMessageTimestamp() throws JMSException {
    return delegate.getDisableMessageTimestamp();
  }

  @Override
  public void setDeliveryMode(int deliveryMode) throws JMSException {
    delegate.setDeliveryMode(deliveryMode);
  }

  @Override
  public int getDeliveryMode() throws JMSException {
    return delegate.getDeliveryMode();
  }

  @Override
  public void setPriority(int defaultPriority) throws JMSException {
    delegate.setPriority(defaultPriority);
  }

  @Override
  public int getPriority() throws JMSException {
    return delegate.getPriority();
  }

  @Override
  public void setTimeToLive(long timeToLive) throws JMSException {
    delegate.setTimeToLive(timeToLive);
  }

  @Override
  public long getTimeToLive() throws JMSException {
    return delegate.getTimeToLive();
  }

  @Override
  public void setDeliveryDelay(long deliveryDelay) throws JMSException {
    delegate.setDeliveryDelay(deliveryDelay);
  }

  @Override
  public long getDeliveryDelay() throws JMSException {
    return delegate.getDeliveryDelay();
  }

  @Override
  public Destination getDestination() throws JMSException {
    return delegate.getDestination();
  }

  @Override
  public void close() throws JMSException {
    delegate.close();
  }

  @Override
  public void send(Message message) throws JMSException {
    JmsSendEvent event = newSendEvent(this.destinationName);
    event.begin();
    try {
      delegate.send(message);
    } finally {
      event.end();
      event.commit();
    }
  }

  @Override
  public void send(Message message, int deliveryMode, int priority, long timeToLive) throws JMSException {
    JmsSendEvent event = newSendEvent(this.destinationName);
    event.begin();
    try {
      delegate.send(message, deliveryMode, priority, timeToLive);
    } finally {
      event.end();
      event.commit();
    }
  }

  @Override
  public void send(Destination destination, Message message) throws JMSException {
    JmsSendEvent event = newSendEvent(destination);
    event.begin();
    try {
      delegate.send(destination, message);
    } finally {
      event.end();
      event.commit();
    }
  }

  @Override
  public void send(Destination destination, Message message, int deliveryMode, int priority, long timeToLive)
      throws JMSException {
    JmsSendEvent event = newSendEvent(destination);
    event.begin();
    try {
      delegate.send(destination, message, deliveryMode, priority, timeToLive);
    } finally {
      event.end();
      event.commit();
    }
  }

  @Override
  public void send(Message message, CompletionListener completionListener) throws JMSException {
    JmsSendEvent event = newSendEvent(this.destinationName);
    event.begin();
    delegate.send(message, completionListener);
  }

  @Override
  public void send(Message message, int deliveryMode, int priority, long timeToLive,
      CompletionListener completionListener) throws JMSException {
    JmsSendEvent event = newSendEvent(this.destinationName);
    event.begin();
    try {
      delegate.send(message, deliveryMode, priority, timeToLive, completionListener);
    } finally {
      event.end();
      event.commit();
    }
  }

  @Override
  public void send(Destination destination, Message message, CompletionListener completionListener)
      throws JMSException {
    JmsSendEvent event = newSendEvent(destination);
    event.begin();
    try {
      delegate.send(destination, message, completionListener);
    } finally {
      event.end();
      event.commit();
    }
  }

  @Override
  public void send(Destination destination, Message message, int deliveryMode, int priority, long timeToLive,
      CompletionListener completionListener) throws JMSException {
    JmsSendEvent event = newSendEvent(destination);
    event.begin();
    try {
      delegate.send(destination, message, deliveryMode, priority, timeToLive, completionListener);
    } finally {
      event.end();
      event.commit();
    }
  }

  private static JmsSendEvent newSendEvent(Destination destination) {
    return newSendEvent(destination);
  }

  private static JmsSendEvent newSendEvent(String destinationName) {
    JmsSendEvent event = new JmsSendEvent();
    event.destinationName = destinationName;
    return event;
  }

}
