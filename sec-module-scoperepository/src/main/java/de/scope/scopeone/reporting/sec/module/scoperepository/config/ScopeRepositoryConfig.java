package de.scope.scopeone.reporting.sec.module.scoperepository.config;

import java.util.Properties;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = "de.scope.scopeone.reporting.sec.module.scoperepository")
@EntityScan("de.scope.scopeone.reporting.sec.module.scoperepository")
@EnableJpaRepositories(basePackages = "de.scope.scopeone.reporting.sec.module.scoperepository",
    entityManagerFactoryRef = "scoperepositoryEntityManagerFactory",
    transactionManagerRef = "scoperepositoryTransatctionManager")
//@EnableTransactionManagement
public class ScopeRepositoryConfig {

  @Value("${spring.jpa.properties.hibernate.dialect}")
  private String dialect;

  @Bean("scopeRepositoryDataSource")
  @ConfigurationProperties(prefix = "app.config.scoperepository.datasource")
  public DataSource dataSource() {
    return DataSourceBuilder.create().build();
  }

  @Bean(name = "scoperepositoryEntityManager")
  public EntityManager entityManager() {
    return entityManagerFactory().createEntityManager();
  }

  @Bean(name = "scoperepositoryEntityManagerFactory")
  public EntityManagerFactory entityManagerFactory() {
    Properties properties = new Properties();
    properties.setProperty("hibernate.dialect", dialect);

    HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();

    LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
    emf.setDataSource(dataSource());
    emf.setJpaVendorAdapter(jpaVendorAdapter);
    emf.setPackagesToScan("de.scope.scopeone.reporting.sec.module.scoperepository");
    emf.setPersistenceUnitName("scoperepositoryPersistenceUnit");
    emf.setJpaProperties(properties);
    emf.afterPropertiesSet();
    return emf.getObject();
  }

  @Bean(name = "scoperepositoryTransatctionManager")
  public PlatformTransactionManager transactionManager() {
    return new JpaTransactionManager(entityManagerFactory());
  }
}
