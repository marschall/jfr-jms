package com.github.marschall.jfr.jms;

import jdk.jfr.Category;
import jdk.jfr.Description;
import jdk.jfr.Event;
import jdk.jfr.Label;

@Label("JMS Operation")
@Description("A JMS Operation")
@Category("JMS")
class JmsOperationEvent extends Event {

  JmsOperationEvent() {
    super();
  }

  @Label("Object")
  @Description("The object type executing the operation")
  String operationObject;

  @Label("Operation Name")
  @Description("The name of the JMS operation")
  String operationName;

}
