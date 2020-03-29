/* Licensed under Apache-2.0 */
package com.rico.kafka.services;

import com.rico.kafka.utils.QueueTopics;
import org.springframework.stereotype.Service;
import ro.common.exception.DataParsingException;
import ro.common.kafka.CommonQueuePublisher;
import ro.common.kafka.MessageWrapper;

/**
 * Sample service for testing Kafka publisher
 *
 * @author r.krishnakumar
 */
@Service
public class QueueServicePublisher extends CommonQueuePublisher {

  /**
   * Sample method for kafka data publish
   *
   * @param topic
   * @param id
   * @param obj
   * @throws DataParsingException
   */
  public void publisher(QueueTopics topic, Long id, Object obj) throws DataParsingException {
    System.out.println("Data is going to publish for the topic " + topic.name());
    MessageWrapper wrapper = getMessageWrapper();
    wrapper.setData(obj);
    wrapper.setId(id);
    publisher(
        topic.toString(),
        wrapper,
        (to, wra) -> {
          System.out.println(
              "Ack on successful publish: Published values are - " + to + " " + wra.getId());
        },
        (to, wra, ex) -> {
          // To-do handle exceptions on failures
        });
  }
}
