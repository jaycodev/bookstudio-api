package com.bookstudio.dashboard.presentation;

import com.bookstudio.dashboard.application.DashboardService;
import com.bookstudio.dashboard.domain.model.DashboardData;
import com.bookstudio.shared.domain.dto.response.ApiErrorResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<?> getDashboardData(
            @RequestParam(defaultValue = "#{T(java.time.Year).now().value}") int year1,
            @RequestParam(defaultValue = "#{T(java.time.Year).now().value - 1}") int year2) {

        try {
            DashboardData data = dashboardService.getDashboardData(year1, year2);
            return ResponseEntity.ok(data);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiErrorResponse(false, "Error retrieving dashboard data", "server_error", 500));
        }
    }
}
