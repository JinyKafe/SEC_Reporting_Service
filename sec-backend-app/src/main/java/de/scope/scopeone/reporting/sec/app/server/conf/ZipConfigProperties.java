package de.scope.scopeone.reporting.sec.app.server.conf;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.config.zipfile")
@Data
public class ZipConfigProperties {

  private String nameprefix;

}
