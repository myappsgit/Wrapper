package myapps.solutions.wrapper.config;

import java.beans.PropertyVetoException;
import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.mchange.v2.c3p0.ComboPooledDataSource;

@Configuration
@EnableTransactionManagement
@PropertySource(value = { "classpath:application.properties" })
public class HuddilConfig {

	@Autowired
	private Environment environment;

	@Bean(name = "huddilEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean huddilEntityManagerFactory() {
		LocalContainerEntityManagerFactoryBean entityManager = new LocalContainerEntityManagerFactoryBean();
		entityManager.setDataSource(huddilDataSource());
		entityManager.setPackagesToScan(new String[] { "myapps.solutions.wrapper.model" });
		entityManager.setPersistenceUnitName("huddil");

		JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		entityManager.setJpaVendorAdapter(vendorAdapter);
		entityManager.setJpaProperties(hibernateProperties());
		return entityManager;
	}

	@Bean
	public DataSource huddilDataSource() {
		ComboPooledDataSource dataSource = new ComboPooledDataSource();
		try {
			dataSource.setDriverClass(environment.getRequiredProperty("jdbc.driverClassName"));
		} catch (IllegalStateException | PropertyVetoException e) {
			e.printStackTrace();
		}
		dataSource.setJdbcUrl(environment.getRequiredProperty("huddil.jdbc.url"));
		dataSource.setUser(environment.getRequiredProperty("huddil.jdbc.username"));
		dataSource.setPassword(environment.getRequiredProperty("huddil.jdbc.password"));
		dataSource.setMinPoolSize(Integer.parseInt(environment.getRequiredProperty("hibernate.c3p0.max_size")));
		dataSource.setMaxPoolSize(Integer.parseInt(environment.getRequiredProperty("hibernate.c3p0.min_size")));
		dataSource.setMaxIdleTime(Integer.parseInt(environment.getRequiredProperty("hibernate.c3p0.idle_test_period")));
		dataSource.setMaxStatements(Integer.parseInt(environment.getRequiredProperty("hibernate.c3p0.max_statements")));
		return dataSource;
	}

	@Bean(name = "huddilTranscationManager")
	public PlatformTransactionManager huddilTransactionManager() {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(huddilEntityManagerFactory().getObject());
		return transactionManager;
	}

	private Properties hibernateProperties() {
		Properties properties = new Properties();
		properties.put("hibernate.dialect", environment.getRequiredProperty("hibernate.dialect"));
		properties.put("hibernate.show_sql", environment.getRequiredProperty("hibernate.show_sql"));
		properties.put("hibernate.format_sql", environment.getRequiredProperty("hibernate.format_sql"));
		return properties;
	}
}
