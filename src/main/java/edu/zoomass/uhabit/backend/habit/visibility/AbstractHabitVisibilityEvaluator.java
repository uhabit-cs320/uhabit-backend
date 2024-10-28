package edu.zoomass.uhabit.backend.habit.visibility;

import edu.zoomass.uhabit.backend.habit.Habit;
import edu.zoomass.uhabit.backend.user.User;
import org.springframework.stereotype.Component;

@Component
public abstract class AbstractHabitVisibilityEvaluator implements HabitVisibilityEvaluator {
    
    protected boolean isOwner(Habit habit, User viewer) {
        return habit.getOwnerId() == viewer.getId();
    }
    
    protected boolean isSystemGenerated(Habit habit) {
        return habit.getOwnerId() == 0; // Assuming system-generated habits have ownerId = 0
    }
}