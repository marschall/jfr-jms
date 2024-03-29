package com.github.marschall.jfr.jms;

import java.util.Objects;

import jakarta.jms.Connection;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSContext;
import jakarta.jms.JMSException;

/**
 * A connection factory that wraps an other one and generates Flight Recorder events.
 */
public class JfrConnectionFactory implements ConnectionFactory {

  private final ConnectionFactory delegate;

  /**
   * Constructs a new JFR connection factory.
   * 
   * @param delegate the actual connection factory to delegate to, not {@code null}
   */
  public JfrConnectionFactory(ConnectionFactory delegate) {
    Objects.requireNonNull(delegate, "delegate");
    this.delegate = delegate;
  }

  @Override
  public Connection createConnection() throws JMSException {
    JmsOperationEvent event = newConnectionFactoryEvent("createConnection");
    event.begin();
    Connection connection;
    try {
      connection = this.delegate.createConnection();
    } finally {
      event.end();
      event.commit();
    }
    return new JfrConnection(connection);
  }

  @Override
  public Connection createConnection(String userName, String password) throws JMSException {
    JmsOperationEvent event = newConnectionFactoryEvent("createConnection");
    event.begin();
    Connection connection;
    try {
      connection = this.delegate.createConnection(userName, password);
    } finally {
      event.end();
      event.commit();
    }
    return new JfrConnection(connection);
  }

  @Override
  public JMSContext createContext() {
    JmsOperationEvent event = newConnectionFactoryEvent("createContext");
    event.begin();
    JMSContext context;
    try {
      context = this.delegate.createContext();
    } finally {
      event.end();
      event.commit();
    }
    return new JfrJMSContext(context);
  }

  @Override
  public JMSContext createContext(String userName, String password) {
    JmsOperationEvent event = newConnectionFactoryEvent("createContext");
    event.begin();
    JMSContext context;
    try {
      context = this.delegate.createContext(userName, password);
    } finally {
      event.end();
      event.commit();
    }
    return new JfrJMSContext(context);
  }

  @Override
  public JMSContext createContext(String userName, String password, int sessionMode) {
    JmsOperationEvent event = newConnectionFactoryEvent("createContext");
    event.begin();
    JMSContext context;
    try {
      context = this.delegate.createContext(userName, password, sessionMode);
    } finally {
      event.end();
      event.commit();
    }
    return new JfrJMSContext(context);
  }

  @Override
  public JMSContext createContext(int sessionMode) {
    JmsOperationEvent event = newConnectionFactoryEvent("createContext");
    event.begin();
    JMSContext context;
    try {
      context = this.delegate.createContext(sessionMode);
    } finally {
      event.end();
      event.commit();
    }
    return new JfrJMSContext(context);
  }

  private static JmsOperationEvent newConnectionFactoryEvent(String operationName) {
    JmsOperationEvent event = new JmsOperationEvent();
    event.operationObject = "ConnectionFactory";
    event.operationName = operationName;
    return event;
  }

}
