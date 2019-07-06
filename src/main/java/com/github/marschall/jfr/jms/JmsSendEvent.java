package com.github.marschall.jfr.jms;

import jdk.jfr.Category;
import jdk.jfr.Description;
import jdk.jfr.Event;
import jdk.jfr.Label;

@Label("JMS Send")
@Description("A JMS Send")
@Category("JMS")
class JmsSendEvent extends Event {

  JmsSendEvent() {
    super();
  }

  @Label("Destination Name")
  @Description("The name of the JMS destination")
  String destinationName;

}
