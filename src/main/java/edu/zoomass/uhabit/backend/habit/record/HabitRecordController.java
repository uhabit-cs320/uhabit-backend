package edu.zoomass.uhabit.backend.habit.record;

import edu.zoomass.uhabit.backend.habit.DefaultHabits;
import edu.zoomass.uhabit.backend.user.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/habits/record")
public class HabitRecordController {

    @Autowired
    private HabitRecordService habitRecordService;

    @Autowired
    private DefaultHabits defaultHabits;

    @GetMapping("/active")
    public ResponseEntity<List<HabitRecord>> getHabits(@AuthenticationPrincipal final UserProfile profile) {
        if (profile == null) {
            return ResponseEntity.badRequest().build();
        }

        List<HabitRecord> habitRecords = habitRecordService.getUserActiveHabitRecords(profile.getId());

        if (habitRecords == null) {
            return ResponseEntity.notFound().build();
        }

        if (habitRecords.isEmpty()) {
            // add default habits
            subscribeToDefaultHabits(profile.getId());

            // Fetch the newly created records
            habitRecords = habitRecordService.getUserActiveHabitRecords(profile.getId());
            if (habitRecords.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
        }

        return ResponseEntity.ok(
                habitRecords
        );
    }

    @PostMapping("/active/{habitId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<HabitRecord> createHabitRecord(@AuthenticationPrincipal final UserProfile profile, @PathVariable String habitId) {
        if (profile == null) {
            return ResponseEntity.badRequest().build();
        }

        System.out.println("Creating habit record for habitId: " + habitId + " userId: " + profile.getEmail());

        habitRecordService.submitDailyHabitRecord(profile.getId(), Long.parseLong(habitId));

        final HabitRecord habitRecord = habitRecordService.getHabitRecord(profile.getId(), Long.parseLong(habitId))
                .orElse(null);

        if (habitRecord == null) {
            System.out.println("Habit record not found!!! habitId: " + habitId);
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(
                habitRecord
        );
    }

    private void subscribeToDefaultHabits(long userId) {
        for (Long habitId : defaultHabits.getDefaultHabitIds()) {
            habitRecordService.submitDailyHabitRecord(userId, habitId);
        }
    }
}
