package de.scope.scopeone.reporting.sec.module.xbrlrepository.config;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@ComponentScan(basePackages = "de.scope.scopeone.reporting.sec.module.xbrlrepository")
//@EnableTransactionManagement
public class XbrlRepositoryConfig {

  @Value("${app.config.xbrlrepository.datasource.schema}")
  public String schema;

  @Bean("xbrlRepositoryDataSource")
  @ConfigurationProperties(prefix = "app.config.xbrlrepository.datasource")
  public DataSource xbrlRepositoryDataSource() {
    return DataSourceBuilder.create().build();
  }

  @Bean(name = "xbrlRepositoryTransactionManager")
  public PlatformTransactionManager transactionManager() {
    return new DataSourceTransactionManager(xbrlRepositoryDataSource());
  }

  @Bean(name = "xbrlRepositoryJdbcTemplate")
  public JdbcTemplate xbrlRepositoryJdbcTemplate() {
    return new JdbcTemplate(xbrlRepositoryDataSource());
  }
}
