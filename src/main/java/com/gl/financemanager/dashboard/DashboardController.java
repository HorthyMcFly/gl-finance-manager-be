package com.gl.financemanager.dashboard;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@AllArgsConstructor
public class DashboardController {

    private DashboardService dashboardService;

    @GetMapping
    @ResponseBody
    public ResponseEntity<DashboardData> getDashboardData() {
        var dashboardData = dashboardService.getDashboardDataForLoggedInUser();
        return new ResponseEntity<>(dashboardData, HttpStatusCode.valueOf(200));
    }
}
