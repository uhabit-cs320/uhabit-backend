package edu.zoomass.uhabit.backend.habit.visibility.impl;

import edu.zoomass.uhabit.backend.habit.Habit;
import edu.zoomass.uhabit.backend.habit.HabitVisibility;
import edu.zoomass.uhabit.backend.habit.visibility.AbstractHabitVisibilityEvaluator;
import edu.zoomass.uhabit.backend.user.User;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class PublicHabitVisibilityEvaluator extends AbstractHabitVisibilityEvaluator {
    
    @Override
    public boolean hasVisibility(Habit habit, User viewer) {
        return isSystemGenerated(habit) || 
               (habit.getVisibility() == HabitVisibility.PUBLIC);
    }
}