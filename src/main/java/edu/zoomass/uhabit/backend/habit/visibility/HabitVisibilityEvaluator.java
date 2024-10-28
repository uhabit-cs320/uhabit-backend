package edu.zoomass.uhabit.backend.habit.visibility;

import edu.zoomass.uhabit.backend.habit.Habit;
import edu.zoomass.uhabit.backend.user.UserProfile;
import org.springframework.stereotype.Component;

@Component
public interface HabitVisibilityEvaluator {
    boolean hasVisibility(Habit habit, UserProfile viewer);
}