package edu.zoomass.uhabit.backend.habit.visibility;

import edu.zoomass.uhabit.backend.habit.Habit;
import edu.zoomass.uhabit.backend.user.User;
import org.springframework.stereotype.Component;

@Component
public interface HabitVisibilityEvaluator {
    boolean hasVisibility(Habit habit, User viewer);
}