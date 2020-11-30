/* Licensed under Apache-2.0 */
package ro.common.elasticsearch;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.ElasticsearchEntityMapper;
import org.springframework.data.elasticsearch.core.EntityMapper;
import org.springframework.http.HttpHeaders;

/**
 * Default configuration class for ElasticSearch
 *
 * @author r.krishnakumar
 */
@Configuration
@DependsOn("log")
public class ElasticSearchConfig extends AbstractElasticsearchConfiguration {

  @Value("${ro.search.hosts}")
  private String elasticServers;

  @Value("${ro.search.username}")
  private String elasticUser;

  @Value("${ro.search.password}")
  private String elasticPassword;

  /*
   * Custom entity based mapper
   */
  @Bean
  @Override
  public EntityMapper entityMapper() {
    ElasticsearchEntityMapper entityMapper =
        new ElasticsearchEntityMapper(
            elasticsearchMappingContext(), new DefaultConversionService());
    entityMapper.setConversions(elasticsearchCustomConversions());
    return entityMapper;
  }

  /** Creating elastic search client */
  @Override
  public RestHighLevelClient elasticsearchClient() {
    HttpHeaders defaultHeaders = new HttpHeaders();
    defaultHeaders.setBasicAuth(elasticUser, elasticPassword);
    ClientConfiguration clientConfiguration =
        ClientConfiguration.builder()
            .connectedTo(elasticServers)
            .withConnectTimeout(Duration.ofSeconds(5))
            .withSocketTimeout(Duration.ofSeconds(3))
            .withDefaultHeaders(defaultHeaders)
            .withBasicAuth(elasticUser, elasticPassword)
            .build();
    return RestClients.create(clientConfiguration).rest(); // NOSONAR
  }

  /**
   * Generating current date for indexing property
   *
   * @return
   */
  @Bean
  public String currentDate() {
    return LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
  }
}
