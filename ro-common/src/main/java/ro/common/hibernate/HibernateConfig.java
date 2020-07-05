package ro.common.hibernate;

import java.util.Optional;
import java.util.Properties;

import org.hibernate.SessionFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

import ro.common.utils.Utils;

/**
 * Hibernate configuration params
 *
 * @author r.krishnakumar
 */
@Configuration
@EnableTransactionManagement
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@DependsOn({"log", "cache"})
public class HibernateConfig implements ApplicationContextAware {

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

  @Autowired private String dataBaseType;

  private ApplicationContext applicationContext;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
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
      Utils.dataBaseType = this.dataBaseType;
      HikariDataSource ds = new HikariDataSource();
      if (dataBaseType.equals("mysql")) {
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.setJdbcUrl("jdbc:mysql://" + host + ":3306/" + databaseName);
      } else if (dataBaseType.equals("postgres")) {
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

  /**
   * Method to configure Hibernate session factory by datasource
   *
   * @return
   */
  @Bean
  public LocalSessionFactoryBean sessionFactory() {
    LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
    sessionFactory.setDataSource(dataSource());
    sessionFactory.setPackagesToScan(entityPackageName);
    Properties hibernateProperties = new Properties();
    hibernateProperties.put("hibernate.show_sql", showSQL);
    hibernateProperties.put("hibernate.hbm2ddl.auto", "validate");
    sessionFactory.setHibernateProperties(hibernateProperties);
    return sessionFactory;
  }

  /**
   * Method to configure Hibernate transaction manager
   *
   * @return
   */
  @Bean
  public HibernateTransactionManager transactionManager() {
    HibernateTransactionManager transactionManager = new HibernateTransactionManager();
    transactionManager.setSessionFactory(sessionFactory().getObject());
    return transactionManager;
  }

  /**
   * Method to generate genericDAO
   *
   * @return
   */
  @Bean
  @DependsOn("transactionManager")
  public GenericDAO genericDAO() {
    SessionFactory sessionFactory = (SessionFactory) applicationContext.getBean("sessionFactory");
    return new GenericDAO(sessionFactory);
  }

  /**
   * Auditor Provider bean
   *
   * @return
   */
  @Bean
  public AuditorAware<String> auditorProvider() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null) {
      return () -> Optional.ofNullable("unknown");
    }
    return () -> Optional.ofNullable(authentication.getName());
  }
}
