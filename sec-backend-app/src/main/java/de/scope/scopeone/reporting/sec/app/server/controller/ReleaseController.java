package de.scope.scopeone.reporting.sec.app.server.controller;

import de.scope.scopeone.reporting.sec.app.server.controller.dto.ReleaseDto;
import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import java.net.HttpURLConnection;
import java.util.List;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Timed
@RequestMapping("/api/v1/release")
public interface ReleaseController {

  @ApiOperation(value = "Get latest zip file", authorizations = {@Authorization(value = "basicAuth")})
  @ApiResponses(value = {@ApiResponse(code = HttpURLConnection.HTTP_INTERNAL_ERROR, message = "Internal server error")})
  @GetMapping(value = "/latest/zip", produces = "application/zip")
  @ResponseBody
  ResponseEntity<Resource> downloadLatestZipFile();


  @ApiOperation(value = "Get release meta info by jobId", authorizations = {@Authorization(value = "basicAuth")})
  @ApiResponses(value = {@ApiResponse(code = HttpURLConnection.HTTP_INTERNAL_ERROR, message = "Internal server error")})
  @GetMapping(value = "with_job_id/{job_id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  List<ReleaseDto> getReleasesByJobId(@ApiParam(value = "job_id", required = true) @PathVariable("job_id") String jobId);

  @ApiOperation(value = "Get release meta info by id", authorizations = {@Authorization(value = "basicAuth")})
  @ApiResponses(value = {@ApiResponse(code = HttpURLConnection.HTTP_INTERNAL_ERROR, message = "Internal server error")})
  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  ReleaseDto getReleaseById(@ApiParam(value = "id", required = true) @PathVariable("id") String id);

  @ApiOperation(value = "Get latest releases with offset and limit", authorizations = {@Authorization(value = "basicAuth")})
  @ApiResponses(value = {@ApiResponse(code = HttpURLConnection.HTTP_INTERNAL_ERROR, message = "Internal server error")})
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  List<ReleaseDto> getReleases(@ApiParam(value = "offset" ,defaultValue = "0") @RequestParam(name = "offset", defaultValue = "0") Integer offset,
                               @ApiParam(value = "limit" ,defaultValue = "10") @RequestParam(name = "limit", defaultValue = "10")Integer limit);

}
