package de.scope.scopeone.reporting.sec.app.server.controller;

import de.scope.scopeone.reporting.sec.module.xbrlrepository.model.Job;
import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import java.net.HttpURLConnection;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Timed
@RequestMapping("/api/v1/jobs")
public interface JobController {

  @ApiOperation(value = "Get latest jobs with offset and limit", authorizations = {@Authorization(value = "basicAuth")})
  @ApiResponses(value = {@ApiResponse(code = HttpURLConnection.HTTP_INTERNAL_ERROR, message = "Internal server error")})
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  List<Job> getJobs(@ApiParam(value = "offset", defaultValue = "0") @RequestParam(name = "offset", defaultValue = "0") Integer offset,
                    @ApiParam(value = "limit", defaultValue = "10") @RequestParam(name = "limit", defaultValue = "10") Integer limit);
}
