package com.github.marschall.jfr.jms;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Objects;

@SuppressWarnings("rawtypes") // JMS uses raw types
final class JfrEnumeration implements Enumeration {

  private final Enumeration delegate;
  private final String queueName;
  private final String messageSelector;
  private long messageCount;

  JfrEnumeration(Enumeration delegate, String queueName, String messageSelector) {
    Objects.requireNonNull(delegate, "delegate");
    this.queueName = queueName;
    this.messageSelector = messageSelector;
    this.delegate = delegate;
    this.messageCount = 0L;
  }

  @Override
  public boolean hasMoreElements() {
    JmsBrowseEvent event = newBrowseEvent("hasMoreElements");
    event.begin();
    boolean hasMoreElements;
    try {
      hasMoreElements = delegate.hasMoreElements();
    } finally {
      event.end();
      event.commit();
    }
    return hasMoreElements;
  }

  @Override
  public Object nextElement() {
    JmsBrowseEvent event = newBrowseEvent("nextElement");
    event.begin();
    Object nextElement;
    try {
      nextElement = delegate.nextElement();
    } finally {
      event.end();
      event.commit();
    }
    this.messageCount += 1L;
    return nextElement;
  }

  @Override
  public Iterator asIterator() {
    return new JfrIterator(delegate.asIterator(), queueName, messageSelector);
  }

  private JmsBrowseEvent newBrowseEvent(String operationName) {
    JmsBrowseEvent event = new JmsBrowseEvent();
    event.queueName = this.queueName;
    event.messageSelector = this.messageSelector;
    event.operationObject = "Enumeration";
    event.operationName = operationName;
    return event;
  }

}
