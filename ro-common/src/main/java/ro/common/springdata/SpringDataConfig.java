/* Licensed under Apache-2.0 */
package ro.common.springdata;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

/**
 * JPA configuration params
 *
 * @author r.krishnakumar
 */
@Configuration
@EnableTransactionManagement
@DependsOn("log")
public class SpringDataConfig {

  @Value("${ro.db.host}")
  private String host;

  @Value("${ro.db.database}")
  private String databaseName;

  @Value("${ro.db.username}")
  private String userName;

  @Value("${ro.db.password}")
  private String password;
  
  @Value("${ro.db.entity.package}")
  private String entityPackageName;

  @Autowired private String dataBaseName;

  /**
   * Datasource with connection pooling
   *
   * @return
   */
  @Bean
  public HikariDataSource dataSource() {

    if (host == null || host.isEmpty()) {
      throw new RuntimeException("No host configured for the database");
    }
    try {
      HikariDataSource ds = new HikariDataSource();
      if (dataBaseName.equals("mysql")) {
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.setJdbcUrl("jdbc:mysql://" + host + ":3306/" + databaseName);
      } else if (dataBaseName.equals("postgres")) {
        ds.setDriverClassName("org.postgresql.Driver");
        ds.setJdbcUrl("jdbc:postgresql://" + host + ":5432/" + databaseName);
      }
      ds.addDataSourceProperty("useSSL", false);
      ds.setMaximumPoolSize(12);
      ds.setUsername(userName);
      ds.setPassword(password);
      ds.addDataSourceProperty("cachePrepStmts", true);
      ds.addDataSourceProperty("prepStmtCacheSize", 250);
      ds.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
      ds.addDataSourceProperty("useServerPrepStmts", true);
      return ds;
    } catch (Exception e) {
      throw new RuntimeException("Failed to initialise datasource");
    }
  }

  /** Entity manager factory bean creation */
  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
    HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    vendorAdapter.setGenerateDdl(true);
    LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
    factory.setJpaVendorAdapter(vendorAdapter);
    factory.setDataSource(dataSource());
    factory.setPackagesToScan(entityPackageName);
    factory.setPersistenceUnitName("ricoPersistence");
    return factory;
  }

  /** JPA transaction manager bean creation */
  @Bean
  public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
    JpaTransactionManager txManager = new JpaTransactionManager();
    txManager.setEntityManagerFactory(entityManagerFactory);
    return txManager;
  }
}
