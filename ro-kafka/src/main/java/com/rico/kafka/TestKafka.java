/* Licensed under Apache-2.0 */
package com.rico.kafka;

import com.rico.kafka.documents.Test;
import com.rico.kafka.documents.User;
import com.rico.kafka.services.QueueServicePublisher;
import com.rico.kafka.utils.QueueTopics;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ro.common.exception.DataParsingException;

/**
 * Testing Kafka queue publisher and listener
 *
 * @author r.krishnakumar
 */
@Component
public class TestKafka {

  @Autowired private QueueServicePublisher queueServicePublisher;

  @PostConstruct
  public void publishData() {
    // Adding a thread and delay to let the consumer starts first before the
    // publisher publishes the data. This may not be necessary in real cases
    // as the producer and consumer for the same topic wont be with the same
    // application.
    new Thread(
            () -> {
              try {
                Thread.sleep(10000);
              } catch (InterruptedException e1) {
                e1.printStackTrace();
              }
              User u = new User("user1", "rico");
              Test t = new Test();
              t.setTestValue("myvalue");
              try {
                queueServicePublisher.publisher(QueueTopics.USER, 12l, u);
                queueServicePublisher.publisher(QueueTopics.TEST, 14l, t);
              } catch (DataParsingException e) {
                e.printStackTrace();
              }
            })
        .start();
  }
}
