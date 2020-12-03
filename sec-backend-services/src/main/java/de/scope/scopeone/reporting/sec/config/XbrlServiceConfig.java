package de.scope.scopeone.reporting.sec.config;

import de.scope.scopeone.reporting.sec.module.scoperepository.config.ScopeRepositoryConfig;
import de.scope.scopeone.reporting.sec.module.xbrlgenerator.config.XbrlGeneratorConf;
import de.scope.scopeone.reporting.sec.module.xbrlrepository.config.XbrlRepositoryConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages = "de.scope.scopeone.reporting.sec")
@Import({ScopeRepositoryConfig.class, XbrlGeneratorConf.class, XbrlRepositoryConfig.class})
public class XbrlServiceConfig {

}
