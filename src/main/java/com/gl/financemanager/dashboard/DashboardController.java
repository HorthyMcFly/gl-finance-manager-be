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
        return new ResponseEntity<>(dashboardService.getDashboardDataForLoggedInUser(),
            HttpStatusCode.valueOf(200));
    }
}
