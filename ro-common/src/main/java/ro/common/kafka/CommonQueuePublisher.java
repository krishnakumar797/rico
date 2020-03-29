/* Licensed under Apache-2.0 */
package ro.common.kafka;

import java.util.function.BiConsumer;
import org.nustaq.serialization.FSTConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import ro.common.generated.MessageProtoOuterClass;
import ro.common.generated.MessageProtoOuterClass.MessageProto;

/**
 * Abstract Common publisher class for kafka topics
 *
 * @author r.krishnakumar
 */
@DependsOn("KafkaConfig")
public abstract class CommonQueuePublisher {

  @Autowired private KafkaTemplate<String, byte[]> kafkaTemplate;

  @Autowired private FSTConfiguration conf;

  @FunctionalInterface
  protected interface TriConsumer<T1, T2, T3> {
    public void accept(T1 t1, T2 t2, T3 t3);
  }

  @FunctionalInterface
  protected interface FourConsumer<T1, T2, T3, T4> {
    public void accept(T1 t1, T2 t2, T3 t3, T4 t4);
  }

  /**
   * Publisher method to send data to the queue
   *
   * @param topicName
   * @param value
   */
  protected final void publisher(String topicName, MessageWrapper value) {
    MessageProto messageProto =
        MessageProtoOuterClass.MessageProto.newBuilder()
            .setId((value.getId() != null ? value.getId() : 0l))
            .setStatus((value.getStatus() != null ? value.getStatus() : ""))
            .setData(value.getByteString())
            .build();
    kafkaTemplate.send(topicName, messageProto.toByteArray());
  }

  /**
   * Publisher method to send data to the queue with ACK on success and failure
   *
   * @param topicName
   * @param value
   */
  protected final void publisher(
      String topicName,
      MessageWrapper value,
      BiConsumer<String, MessageWrapper> ackOnSuccess,
      TriConsumer<String, MessageWrapper, Throwable> ackOnFailure) {
    MessageProto messageProto =
        MessageProtoOuterClass.MessageProto.newBuilder()
            .setId((value.getId() != null ? value.getId() : 0l))
            .setStatus((value.getStatus() != null ? value.getStatus() : ""))
            .setData(value.getByteString())
            .build();
    ListenableFuture<SendResult<String, byte[]>> future =
        kafkaTemplate.send(topicName, messageProto.toByteArray());

    future.addCallback(
        new ListenableFutureCallback<SendResult<String, byte[]>>() {
          @Override
          public void onSuccess(SendResult<String, byte[]> result) {
            ackOnSuccess.accept(topicName, value);
          }

          @Override
          public void onFailure(Throwable ex) {
            ackOnFailure.accept(topicName, value, ex);
          }
        });
  }

  /**
   * Overloaded publisher method to send with key's for partitioning
   *
   * @param topicName
   * @param key
   * @param value
   */
  protected final void publisher(String topicName, String key, MessageWrapper value) {
    MessageProto messageProto =
        MessageProtoOuterClass.MessageProto.newBuilder()
            .setId((value.getId() != null ? value.getId() : 0l))
            .setStatus((value.getStatus() != null ? value.getStatus() : ""))
            .setData(value.getByteString())
            .build();
    kafkaTemplate.send(topicName, key, messageProto.toByteArray());
  }

  /**
   * Overloaded publisher method to send with key's for partitioning and ACK on success and failure
   *
   * @param topicName
   * @param key
   * @param value
   */
  protected final void publisher(
      String topicName,
      String key,
      MessageWrapper value,
      TriConsumer<String, String, MessageWrapper> ackOnSuccess,
      FourConsumer<String, String, MessageWrapper, Throwable> ackOnFailure) {
    MessageProto messageProto =
        MessageProtoOuterClass.MessageProto.newBuilder()
            .setId((value.getId() != null ? value.getId() : 0l))
            .setStatus((value.getStatus() != null ? value.getStatus() : ""))
            .setData(value.getByteString())
            .build();
    ListenableFuture<SendResult<String, byte[]>> future =
        kafkaTemplate.send(topicName, key, messageProto.toByteArray());
    future.addCallback(
        new ListenableFutureCallback<SendResult<String, byte[]>>() {
          @Override
          public void onSuccess(SendResult<String, byte[]> result) {
            ackOnSuccess.accept(topicName, key, value);
          }

          @Override
          public void onFailure(Throwable ex) {
            ackOnFailure.accept(topicName, key, value, ex);
          }
        });
  }

  /**
   * Util method to create new message wrapper object with fst
   *
   * @return this
   */
  protected final MessageWrapper getMessageWrapper() {
    return MessageWrapper.newInstance().with(local -> local.setFSTConfiguration(conf)).build();
  }
}
