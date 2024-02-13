package com.gl.financemanager.dashboard;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @GetMapping
    @ResponseBody
    public ResponseEntity<DashboardResponse> getDashboardData(JwtAuthenticationToken jwtAuthenticationToken) {
        // TODO: implement this
        var dashboardResponse = new DashboardResponse("placeholder");
        return new ResponseEntity<>(dashboardResponse, HttpStatusCode.valueOf(200));
    }
}
