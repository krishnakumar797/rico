/* Licensed under Apache-2.0 */
package ro.common.kafka;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.CreateTopicsOptions;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.nustaq.serialization.FSTConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import ro.common.exception.QueueInitException;

/**
 * Default configuration class for Kafka
 *
 * @author r.krishnakumar
 */
@Configuration
@DependsOn("log")
public class KafkaConfig {

  @Value("${ro.queue.hosts}")
  private String bootStrapServers;

  @Value("${ro.queue.topics.auto.create}")
  private boolean createTopics;

  @Value("${ro.queue.consumer.name:false}")
  private String consumerName;

  @Value("${ro.queue.consumer.group.name:false}")
  private String consumerGroupName;

  @Value("${ro.queue.consumer.concurrency:2}")
  private Integer concurrencyCount;

  @Value("#{'${ro.queue.topics}'.split(',')}")
  private String[] topicArray;

  @Value("#{${ro.queue.topic.partition: {test: 1}}}")
  private Map<String, Integer> partition;

  @Value("#{${ro.queue.topic.replica: {test: 1}}}")
  private Map<String, Integer> replica;

  @PostConstruct
  private void initialize() {
    if (!createTopics) {
      return;
    }
    Map<String, Object> conf = new HashMap<>();
    conf.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServers);
    conf.put(AdminClientConfig.REQUEST_TIMEOUT_MS_CONFIG, "5000");
    try (AdminClient client = AdminClient.create(conf)) {
      List<NewTopic> newTopicList = new ArrayList<>();
      for (String topic : topicArray) {
        Integer partitionCount = partition.get(topic);
        if (partitionCount == null) {
          throw new QueueInitException("No partition count is configured for the topic " + topic);
        }
        Integer replicaCount = replica.get(topic);
        if (replicaCount == null) {
          throw new QueueInitException("No replica count is configured for the topic " + topic);
        }
        newTopicList.add(new NewTopic(topic, partitionCount, replicaCount.shortValue()));
      }
      KafkaFuture<?> result =
          client.createTopics(newTopicList, new CreateTopicsOptions().timeoutMs(10000)).all();
      result.get();
      if (result.isCompletedExceptionally()) {
        throw new QueueInitException("Topic creation has failed");
      }
    } catch (Exception e) {
      throw new QueueInitException("Kafka config initialization has failed");
    }
  }

  @Bean
  ConcurrentKafkaListenerContainerFactory<String, byte[]> kafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, byte[]> factory =
        new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(consumerFactory());
    factory.setConcurrency(concurrencyCount);
    return factory;
  }

  @Bean
  KafkaTemplate<String, byte[]> kafkaTemplate() {
    return new KafkaTemplate<>(producerFactory());
  }

  @Bean
  ConsumerFactory<String, byte[]> consumerFactory() {
    return new DefaultKafkaConsumerFactory<>(consumerProps());
  }

  @Bean
  ProducerFactory<String, byte[]> producerFactory() {
    return new DefaultKafkaProducerFactory<>(senderProps());
  }

  @Bean
  FSTConfiguration fstConfiguration() {
    return FSTConfiguration.createDefaultConfiguration();
  }

  /**
   * Kafka consumer property configs
   *
   * @return
   */
  private Map<String, Object> consumerProps() {
    Map<String, Object> props = new HashMap<>();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServers);
    props.put(ConsumerConfig.CLIENT_ID_CONFIG, consumerName);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroupName);
    props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
    props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "100");
    props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "15000");
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class);
    return props;
  }

  /**
   * Kafka producer property configs
   *
   * @return
   */
  private Map<String, Object> senderProps() {
    Map<String, Object> props = new HashMap<>();
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServers);
    props.put(ProducerConfig.RETRIES_CONFIG, 0);
    props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
    props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
    props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class);
    return props;
  }
}
