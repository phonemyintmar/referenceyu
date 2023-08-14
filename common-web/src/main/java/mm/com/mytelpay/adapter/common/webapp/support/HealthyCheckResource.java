package mm.com.mytelpay.adapter.common.webapp.support;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import mm.com.mytelpay.adapter.common.dto.response.Response;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "Healthy Check Resource")
@RestController
public class HealthyCheckResource {

    @ApiOperation("Root path")
    @GetMapping("")
    Response<Void> check() {
        return Response.ok();
    }

    @ApiOperation("Check Health")
    @GetMapping("/health")
    Response<Void> health() {
        return Response.ok();
    }
}
