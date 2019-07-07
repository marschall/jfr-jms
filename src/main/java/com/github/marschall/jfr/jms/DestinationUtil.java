package com.github.marschall.jfr.jms;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.Topic;

final class DestinationUtil {

  private DestinationUtil() {
    throw new AssertionError("not instantiable");
  }

  static String getQueueNameSafe(Queue queue) {
    try {
      return queue.getQueueName();
    } catch (JMSException e) {
      return "<JMSException>";
    }
  }

  static String getDestinationName(Destination destination) {
    try {
      if (destination instanceof Queue) {
        return ((Queue) destination).getQueueName();
      } else if (destination instanceof Topic) {
        return ((Topic) destination).getTopicName();
      }
    } catch (JMSException e) {
      return "<JMSException>";
    }
    return null;
  }

}
