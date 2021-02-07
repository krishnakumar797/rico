/* Licensed under Apache-2.0 */
package ro.common.springdata;

import com.zaxxer.hikari.HikariDataSource;
import java.sql.SQLException;
import java.util.*;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.hibernate.MultiTenancyStrategy;
import org.hibernate.cfg.Environment;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import ro.common.exception.SpringDataInitException;
import ro.common.utils.AppContext;
import ro.common.utils.Utils;

/**
 * Spring data JPA configuration params
 *
 * @author r.krishnakumar
 */
@Configuration
@EnableTransactionManagement
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@DependsOn({"log", "cache"})
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

  @Value("${ro.db.showSQL:false}")
  private boolean showSQL;

  @Autowired
  @Qualifier("multitenancy")
  private boolean isMultenant;

  @Autowired
  @Qualifier("security")
  private boolean isSecurityEnabled;

  @Autowired private String dataBaseType;

  private String packageName;

  /** Spring data initialisation validations */
  @PostConstruct
  public void initialise() {
    if (dataBaseType.equals("mysql") && isMultenant) {
      throw new SpringDataInitException("Multitenancy is not supported with MySQL");
    }
    if (isSecurityEnabled) {
      // packageName = SecurityConfig.class.getPackageName();
      packageName = "ro.common.security";
    }
  }

  /**
   * Datasource with connection pooling
   *
   * @return
   */
  @Bean
  public HikariDataSource dataSource() {

    if (Utils.propertyHasTrailingSpaces(host)) {
      throw new RuntimeException("DB Host value has trailing white space");
    }
    if (Utils.propertyHasTrailingSpaces(userName)) {
      throw new RuntimeException("DB Username value has trailing white space");
    }
    if (Utils.propertyHasTrailingSpaces(password)) {
      throw new RuntimeException("DB password value has trailing white space");
    }
    if (Utils.propertyHasTrailingSpaces(entityPackageName)) {
      throw new RuntimeException("Entity Package Name value has trailing white space");
    }
    try {
      HikariDataSource ds = new HikariDataSource();
      if (dataBaseType.equals("mysql")) {
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.setJdbcUrl("jdbc:mysql://" + host + ":3306/" + databaseName);
      } else if (dataBaseType.equals("postgres")) {
        ds.setDriverClassName("org.postgresql.Driver");
        ds.setJdbcUrl("jdbc:postgresql://" + host + ":5432/" + databaseName);
      }
      ds.setMaximumPoolSize(12);
      ds.setUsername(userName);
      ds.setPassword(password);
      ds.addDataSourceProperty("cachePrepStmts", true);
      ds.addDataSourceProperty("prepStmtCacheSize", 250);
      ds.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
      ds.addDataSourceProperty("useServerPrepStmts", true);
      ds.addDataSourceProperty("useSSL", false);
      ds.addDataSourceProperty("allowPublicKeyRetrieval",true);
      return ds;
    } catch (Exception e) {
      throw new RuntimeException("Failed to initialise datasource");
    }
  }

  /**
   * Multitenant connection provider
   *
   * @return
   */
  @Bean
  @ConditionalOnBean(name = "multitenancy")
  public MultiTenantConnectionProvider multiTenantConnectionProvider(DataSource dataSource) {
    return new TenantConnectionProvider(dataSource);
  }

  /**
   * Multitenant schema resolver
   *
   * @return
   */
  @Bean
  @ConditionalOnBean(name = "multitenancy")
  public CurrentTenantIdentifierResolver currentTenantIdentifierResolver() {
    return new TenantSchemaResolver();
  }

  /**
   * Entity manager factory bean creation
   *
   * @throws SQLException
   */
  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory(
      DataSource dataSource,
      @Autowired(required = false) MultiTenantConnectionProvider multiTenantConnectionProviderImpl,
      @Autowired(required = false)
          CurrentTenantIdentifierResolver currentTenantIdentifierResolverImpl)
      throws SQLException {
    LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
    HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    vendorAdapter.setGenerateDdl(true);
    if (this.isMultenant && dataBaseType.equals("postgres")) {
      Map<String, Object> jpaPropertiesMap = new HashMap<>();
      jpaPropertiesMap.put(Environment.MULTI_TENANT, MultiTenancyStrategy.SCHEMA);
      jpaPropertiesMap.put(
          Environment.MULTI_TENANT_CONNECTION_PROVIDER, multiTenantConnectionProviderImpl);
      jpaPropertiesMap.put(
          Environment.MULTI_TENANT_IDENTIFIER_RESOLVER, currentTenantIdentifierResolverImpl);
      factory.setJpaPropertyMap(jpaPropertiesMap);
    }
    Properties jpaProperties = new Properties();
    if (this.showSQL) {
      vendorAdapter.setShowSql(true);
      jpaProperties.put("hibernate.format_sql", true);
    }
    factory.setJpaProperties(jpaProperties);
    factory.setJpaVendorAdapter(vendorAdapter);
    factory.setDataSource(dataSource);
    List<String> entityPackageList = new ArrayList<>();
    entityPackageList.add(entityPackageName);
    if (this.isSecurityEnabled) {
      entityPackageList.add(packageName);
    }
    factory.setPackagesToScan(entityPackageList.toArray(new String[entityPackageList.size()]));
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

  /**
   * Auditor Provider bean.
   *
   * <p>Do not modify this method
   *
   * @return
   */
  @Bean
  public AuditorAware<String> auditorProvider() {
    String userIdentifier = "unknown";
    // To-Do Need to move to Spring Security interceptor
    //    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    //    if (authentication != null) {
    //      userIdentifier = authentication.getName();
    //    }
    if (AppContext.getCurrentUserId() != null) {
      userIdentifier = AppContext.getCurrentUserId();
    }
    final String id = userIdentifier;
    return () -> Optional.ofNullable(id);
  }
}
