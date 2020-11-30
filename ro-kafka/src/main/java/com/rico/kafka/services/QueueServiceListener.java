/* Licensed under Apache-2.0 */
package com.rico.kafka.services;

import com.rico.kafka.documents.Test;
import com.rico.kafka.documents.User;
import com.rico.kafka.utils.QueueTopics;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ro.common.exception.DataParsingException;
import ro.common.kafka.CommonQueueListener;
import ro.common.kafka.MessageWrapper;

/**
 * Sample service for testing Kafka listener
 *
 * @author r.krishnakumar
 */
@Service
@Log4j2
public class QueueServiceListener extends CommonQueueListener {

  protected QueueServiceListener() {
    super(QueueServiceListener.class);
  }

  /** Listener method to receive the data on the published topics */
  @Override
  public void listener(String topic, Integer partition, MessageWrapper wrapper) {
    log.info("Listening the topic " + topic);
    try {
      switch (QueueTopics.valueOf(topic.toUpperCase())) {
        case TEST:
          log.info("ID " + wrapper.getId());
          Test t = wrapper.getData(Test.class);
          log.info("Test " + t.getTestValue());
          break;
        case USER:
          log.info("ID " + wrapper.getId());
          User u = wrapper.getData(User.class);
          log.info("User " + u.getFirstname());
          break;
        default:
          break;
      }

    } catch (DataParsingException e) {
      e.printStackTrace();
    }
  }
}
