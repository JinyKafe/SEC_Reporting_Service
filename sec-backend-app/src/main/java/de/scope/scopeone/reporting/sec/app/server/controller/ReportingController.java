package de.scope.scopeone.reporting.sec.app.server.controller;

import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import java.net.HttpURLConnection;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Timed
@RequestMapping("/api/v1/xbrl")
public interface ReportingController {

  @ApiOperation(value = "Produce xbrl files", authorizations = {@Authorization(value = "basicAuth")})
  @ApiResponses(value = {@ApiResponse(code = HttpURLConnection.HTTP_INTERNAL_ERROR, message = "Internal server error")})
  @PostMapping("/produce")
  @ResponseBody
  Void produceXblrFiles();

}

