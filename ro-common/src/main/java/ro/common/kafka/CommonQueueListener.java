/* Licensed under Apache-2.0 */
package ro.common.kafka;

import com.google.protobuf.InvalidProtocolBufferException;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.nustaq.serialization.FSTConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.ConsumerSeekAware;
import ro.common.exception.QueueException;
import ro.common.exception.QueueInitException;
import ro.common.generated.MessageProtoOuterClass.MessageProto;

/**
 * Abstract Common listener class for kafka topics
 *
 * @author r.krishnakumar
 */
@DependsOn("KafkaConfig")
public abstract class CommonQueueListener implements ConsumerSeekAware {

  @Autowired private FSTConfiguration conf;

  @Value("#{'${ro.queue.topics.read.start: ''}'.split(',')}")
  private String[] topicsToStartFromBeginning;

  private static int counter = 0;

  /** Restricting the usage of CommonQueueListener more than once */
  protected CommonQueueListener(Class<? extends CommonQueueListener> clas) { // NOSONAR
    counter++;
    if (counter != 1) {
      throw new QueueInitException("CommonQueueListener is inherited more than once");
    }
  }

  /**
   * Main listener for kafka configured topics
   *
   * @param consumerRecord
   * @throws QueueException
   */
  @KafkaListener(topics = "#{'${ro.queue.topics}'.split(',')}")
  private void listener(ConsumerRecord<?, byte[]> consumerRecord) throws QueueException {
    try {
      MessageProto messageProto = MessageProto.parseFrom(consumerRecord.value());
      MessageWrapper wrapper =
          MessageWrapper.newInstance()
              .with(
                  $ -> { // NOSONAR
                    $.setId(messageProto.getId());
                    $.setStatus(messageProto.getStatus());
                    $.setByteData(messageProto.getData().toByteArray());
                    $.setFSTConfiguration(conf);
                  })
              .build();
      listener(consumerRecord.topic(), consumerRecord.partition(), wrapper);
    } catch (InvalidProtocolBufferException ip) {
      throw new QueueException("Invalid Protocol buffer parsing", ip);
    }
  }

  /** Method which determines the consumer seek position for a particular topic to the beginning. */
  @Override
  public void onPartitionsAssigned(
      Map<TopicPartition, Long> assignments, ConsumerSeekCallback callback) {
    for (String topic : topicsToStartFromBeginning) {
      assignments.keySet().stream()
          .filter(partition -> topic.equals(partition.topic()))
          .forEach(partition -> callback.seekToBeginning(topic, partition.partition()));
    }
  }

  /**
   * Abstract method for data processing. Override this method inside the inherited class to handle
   * the data received at kafka topics
   *
   * @param topicName
   * @param value
   */
  protected abstract void listener(String topicName, Integer partition, MessageWrapper value);
}
