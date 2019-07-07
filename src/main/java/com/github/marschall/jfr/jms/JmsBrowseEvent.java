package com.github.marschall.jfr.jms;

import jdk.jfr.Category;
import jdk.jfr.Description;
import jdk.jfr.Event;
import jdk.jfr.Label;

@Label("JMS Browse")
@Description("A JMS browse operation")
@Category("JMS")
class JmsBrowseEvent extends Event {

  JmsBrowseEvent() {
    super();
  }

  @Label("Object")
  @Description("The object type executing the operation")
  String operationObject;

  @Label("Operation Name")
  @Description("The name of the JMS operation")
  String operationName;
  
  @Label("Queue Name")
  @Description("The name of the queue being browsed")
  String queueName;
  
  @Label("Message Selector")
  @Description("The selector used to browse the queue")
  String messageSelector;

}
