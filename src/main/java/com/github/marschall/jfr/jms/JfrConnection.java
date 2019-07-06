package com.github.marschall.jfr.jms;

import java.util.Objects;

import javax.jms.Connection;
import javax.jms.ConnectionConsumer;
import javax.jms.ConnectionMetaData;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.ServerSessionPool;
import javax.jms.Session;
import javax.jms.Topic;

final class JfrConnection implements Connection {

  private final Connection delegate;

  JfrConnection(Connection delegate) {
    Objects.requireNonNull(delegate, "delegate");
    this.delegate = delegate;
  }

  @Override
  public Session createSession(boolean transacted, int acknowledgeMode) throws JMSException {
    JmsOperationEvent event = newConnectionEvent("createSession");
    event.begin();
    Session session;
    try {
      session = this.delegate.createSession(transacted, acknowledgeMode);
    } finally {
      event.end();
      event.commit();
    }
    return new JfrSession(session);
  }

  @Override
  public Session createSession(int sessionMode) throws JMSException {
    JmsOperationEvent event = newConnectionEvent("createSession");
    event.begin();
    Session session;
    try {
      session = this.delegate.createSession(sessionMode);
    } finally {
      event.end();
      event.commit();
    }
    return new JfrSession(session);
  }

  @Override
  public Session createSession() throws JMSException {
    JmsOperationEvent event = newConnectionEvent("createSession");
    event.begin();
    Session session;
    try {
      session = this.delegate.createSession();
    } finally {
      event.end();
      event.commit();
    }
    return new JfrSession(session);
  }

  @Override
  public String getClientID() throws JMSException {
    return this.delegate.getClientID();
  }

  @Override
  public void setClientID(String clientID) throws JMSException {
    this.delegate.setClientID(clientID);
  }

  @Override
  public ConnectionMetaData getMetaData() throws JMSException {
    return this.delegate.getMetaData();
  }

  @Override
  public ExceptionListener getExceptionListener() throws JMSException {
    return this.delegate.getExceptionListener();
  }

  @Override
  public void setExceptionListener(ExceptionListener listener) throws JMSException {
    this.delegate.setExceptionListener(listener);
  }

  @Override
  public void start() throws JMSException {
    this.delegate.start();
  }

  @Override
  public void stop() throws JMSException {
    this.delegate.stop();
  }

  @Override
  public void close() throws JMSException {
    this.delegate.close();
  }

  @Override
  public ConnectionConsumer createConnectionConsumer(Destination destination, String messageSelector,
      ServerSessionPool sessionPool, int maxMessages) throws JMSException {
    return this.delegate.createConnectionConsumer(destination, messageSelector, sessionPool, maxMessages);
  }

  @Override
  public ConnectionConsumer createSharedConnectionConsumer(Topic topic, String subscriptionName, String messageSelector,
      ServerSessionPool sessionPool, int maxMessages) throws JMSException {
    return this.delegate.createSharedConnectionConsumer(topic, subscriptionName, messageSelector, sessionPool, maxMessages);
  }

  @Override
  public ConnectionConsumer createDurableConnectionConsumer(Topic topic, String subscriptionName,
      String messageSelector, ServerSessionPool sessionPool, int maxMessages) throws JMSException {
    return this.delegate.createDurableConnectionConsumer(topic, subscriptionName, messageSelector, sessionPool, maxMessages);
  }

  @Override
  public ConnectionConsumer createSharedDurableConnectionConsumer(Topic topic, String subscriptionName,
      String messageSelector, ServerSessionPool sessionPool, int maxMessages) throws JMSException {
    return this.delegate.createSharedDurableConnectionConsumer(topic, subscriptionName, messageSelector, sessionPool,
        maxMessages);
  }

  private static JmsOperationEvent newConnectionEvent(String operationName) {
    JmsOperationEvent event = new JmsOperationEvent();
    event.operationObject = "Connection";
    event.operationName = operationName;
    return event;
  }

}
