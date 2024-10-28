package edu.zoomass.uhabit.backend.habit;

import edu.zoomass.uhabit.backend.user.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/habits")
public class HabitController {

    @Autowired
    private HabitService habitService;

    @GetMapping("/public")
    public ResponseEntity<List<Habit>> getPublicHabits() {
        return ResponseEntity.ok(habitService.getPublicHabits());
    }

    @GetMapping("/id/{habitId}")
    public ResponseEntity<Habit> getHabit(@AuthenticationPrincipal final UserProfile profile, @PathVariable String habitId) {
        if (profile == null) {
            return ResponseEntity.badRequest().build();
        }

        final long habitIdLong = Long.parseLong(habitId);
        final Optional<Habit> habit = habitService.getHabitById(habitIdLong);

        if (habit.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        final Habit habitValue = habit.get();

        if (habitValue.getVisibility() == HabitVisibility.PRIVATE && habitValue.getOwnerId() != profile.getId()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(habitValue);
    }
}
