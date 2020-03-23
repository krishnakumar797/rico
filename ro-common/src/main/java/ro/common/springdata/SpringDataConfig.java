package ro.common.springdata;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
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
 *
 */
@Configuration
@EnableJpaRepositories
@EnableTransactionManagement
public class SpringDataConfig {

	@Value("${ro.db.hosts}")
	private List<String> host;
	
	@Value("${ro.db.database}")
	private String databaseName;
	
	@Value("${ro.db.username}")
	private String userName;
	
	@Value("${ro.db.password}")
	private String password;

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

		Properties prop = new Properties();
		String file = "projectInfo.properties";
		try (HikariDataSource ds = new HikariDataSource();
				InputStream fins = getClass().getClassLoader().getResourceAsStream(file)) {
			if (fins == null) {
				throw new RuntimeException("Project info file not found");
			}
			prop.load(fins);
			String hostName = host.get(0);
			String dataBaseName = prop.getProperty("database");
			if (dataBaseName.equals("mysql")) {
				ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
				ds.setJdbcUrl("jdbc:mysql://" + hostName + ":3306/" + databaseName);
			} else if (dataBaseName.equals("postgres")) {

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
		} catch (IOException e) {
			throw new RuntimeException("Project info file not found");
		} catch (Exception e) {
			throw new RuntimeException("Failed to initialise datasource");
		}
	}

	/**
	 * Entity manager factory bean creation
	 */
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		vendorAdapter.setGenerateDdl(true);
		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setJpaVendorAdapter(vendorAdapter);
		factory.setPackagesToScan("com.acme.domain");
		factory.setDataSource(dataSource());
		return factory;
	}

	/**
	 * JPA transaction manager bean creation
	 */
	@Bean
	public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
		JpaTransactionManager txManager = new JpaTransactionManager();
		txManager.setEntityManagerFactory(entityManagerFactory);
		return txManager;
	}

}
