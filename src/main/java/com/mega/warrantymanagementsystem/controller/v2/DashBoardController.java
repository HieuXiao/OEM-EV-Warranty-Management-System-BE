package com.mega.warrantymanagementsystem.controller.v2;

import com.mega.warrantymanagementsystem.service.v2.DashBoardService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin
@SecurityRequirement(name = "api")
public class DashBoardController {

    @Autowired
    DashBoardService dashBoardService;

    @GetMapping("/stats")
    public ResponseEntity getDashboardStats(){
        Map<String,Object> stats = dashBoardService.getDashboardStats();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/claims/monthly")
    public ResponseEntity<?> getClaimsByMonth(@RequestParam(required = false) Integer year) {
        int y = (year == null) ? java.time.LocalDate.now().getYear() : year;
        return ResponseEntity.ok(dashBoardService.getClaimsByMonth(y));
    }


    @GetMapping("/claims/daily")
    public ResponseEntity<?> getClaimsByDay(@RequestParam Integer year,
                                            @RequestParam Integer month) {
        return ResponseEntity.ok(dashBoardService.getClaimsByDay(year, month));
    }


    @GetMapping("/accounts/by-role")
    public ResponseEntity<?> getAccountStats() {
        return ResponseEntity.ok(dashBoardService.getAccountStats());
    }

}
