package edu.zoomass.uhabit.backend.health;

import edu.zoomass.uhabit.backend.user.UserProfile;
import lombok.Builder;
import lombok.Data;
import org.apache.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/health")
    public String health() {
        return "OK!";
    }

    @GetMapping("/health/user")
    public ResponseEntity<HealthResponse> healthUser(@AuthenticationPrincipal UserProfile user) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.SC_FORBIDDEN).build();
        }

        return ResponseEntity.ok(HealthResponse.builder().status("OK").build());
    }

    @Data
    @Builder
    public static class HealthResponse {
        private String status;
    }
}
