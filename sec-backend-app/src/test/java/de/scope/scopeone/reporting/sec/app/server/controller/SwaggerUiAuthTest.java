package de.scope.scopeone.reporting.sec.app.server.controller;

import static io.restassured.RestAssured.given;

import de.scope.scopeone.reporting.sec.app.BaseIntegrationTest;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

public class SwaggerUiAuthTest extends BaseIntegrationTest {


  private final String HEALTH = "/health";
  private final String PROMETHEUS = "/prometheus";
  private final String UP = "UP";
  private final String SWAGGERUI = "/swagger-ui.html";

  @Value("${management.endpoints.web.base-path}")
  private String managementBasePath;

  @Test
  public void checkHealthEndPoint() {

    given().log().all()
        .auth().basic(getUsername(), getPassword()).
        when().port(getPort())
        .request("GET", getHeathEndPoint())
        .then().log().all()
        .statusCode(200)
        .body("status", Matchers.equalTo(UP))
        .body("components.ping.status", Matchers.equalTo(UP));

  }

  @Test
  public void checkPrometheusEndPoint() {

    given().log().all()
        .auth().basic(getUsername(), getPassword()).
        when().port(getPort())
        .request("GET", getPrometheusEnd())
        .then().log().all()
        .statusCode(200);

  }

  @Test
  public void checkUnauthorizedRequest() {

    given().log().all().
        when().port(getPort())
        .request("GET", getHeathEndPoint())
        .then().statusCode(401);

  }

  @Test
  public void swaggerUiRequest() {

    given().log().all().auth().basic(getUsername(), getPassword()).
        when().port(getPort()).request("GET", SWAGGERUI)
        .then().log().all()
        .statusCode(200)
    .contentType(Matchers.equalTo(ContentType.HTML.toString()));

  }

  private String buildManagementEndPoint(String subPath){
    return new StringBuilder(managementBasePath)
        .append(subPath)
        .toString();
  }

  private String getHeathEndPoint() {
    return buildManagementEndPoint(HEALTH);
  }

  private String getPrometheusEnd() {
    return buildManagementEndPoint(PROMETHEUS);
  }
}
