package com.github.marschall.jfr.jms;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Consumer;

@SuppressWarnings("rawtypes") // JMS uses raw types
final class JfrIterator implements Iterator {
  
  private final Iterator delegate;
  private String queueName;
  private String messageSelector;
  private long messageCount;

  JfrIterator(Iterator delegate, String queueName, String messageSelector) {
    Objects.requireNonNull(delegate, "delegate");
    this.delegate = delegate;
    this.queueName = queueName;
    this.messageSelector = messageSelector;
    this.messageCount = 0L;
  }

  @Override
  public boolean hasNext() {
    JmsBrowseEvent event = newBrowseEvent("hasNext");
    event.begin();
    boolean hasNext;
    try {
      hasNext = delegate.hasNext();
    } finally {
      event.end();
      event.commit();
    }
    return hasNext;
  }

  @Override
  public Object next() {
    JmsBrowseEvent event = newBrowseEvent("next");
    event.begin();
    Object next;
    try {
      next = delegate.next();
    } finally {
      event.end();
      event.commit();
    }
    this.messageCount += 1L;
    return next;
  }

  @Override
  public void remove() {
    delegate.remove();
  }

  @Override
  @SuppressWarnings("unchecked") // JMS uses raw types
  public void forEachRemaining(Consumer action) {
    delegate.forEachRemaining(message -> {
      this.messageCount += 1L;
      action.accept(message);
    });
  }

  private JmsBrowseEvent newBrowseEvent(String operationName) {
    JmsBrowseEvent event = new JmsBrowseEvent();
    event.queueName = this.queueName;
    event.messageSelector = this.messageSelector;
    event.operationObject = "Iterator";
    event.operationName = operationName;
    return event;
  }

}
