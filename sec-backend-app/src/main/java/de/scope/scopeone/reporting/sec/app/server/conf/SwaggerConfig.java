package de.scope.scopeone.reporting.sec.app.server.conf;

import java.util.Collections;
import java.util.Locale;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.BasicAuth;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
class SwaggerConfig {

  private static final String BASE_PACKAGE = "de.scope.scopeone.reporting.sec.app.server";

  private static Docket newDocket() {
    final Docket docket = new Docket(DocumentationType.SWAGGER_2)
        .apiInfo(apiInfo())
        .useDefaultResponseMessages(false)
        .securitySchemes(Collections.singletonList(new BasicAuth("basicAuth")));
    addGlobalConfiguration(docket);
    return docket;
  }

  private static ApiInfo apiInfo() {
    return new ApiInfoBuilder().title("ScopeOne MDM Wrapper API").build();
  }

  private static void addGlobalConfiguration(final Docket docket) {
    docket.directModelSubstitute(Locale.class, String.class);
  }

  @Bean
  public Docket mainApi() {
    return newDocket().groupName("Main")
        .select().apis(RequestHandlerSelectors.basePackage(BASE_PACKAGE))
        .build();
  }
}

