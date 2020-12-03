package de.scope.scopeone.reporting.sec.app.server.conf;

import de.scope.scopeone.reporting.sec.config.XbrlServiceConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({XbrlServiceConfig.class})
public class AppConfig {

}
