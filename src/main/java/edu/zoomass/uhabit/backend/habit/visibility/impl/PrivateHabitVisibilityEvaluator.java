package edu.zoomass.uhabit.backend.habit.visibility.impl;

import edu.zoomass.uhabit.backend.habit.Habit;
import edu.zoomass.uhabit.backend.habit.HabitVisibility;
import edu.zoomass.uhabit.backend.habit.visibility.AbstractHabitVisibilityEvaluator;
import edu.zoomass.uhabit.backend.user.UserProfile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(3)
public class PrivateHabitVisibilityEvaluator extends AbstractHabitVisibilityEvaluator {
    
    @Override
    public boolean hasVisibility(Habit habit, UserProfile viewer) {
        return habit.getVisibility() == HabitVisibility.PRIVATE && 
               isOwner(habit, viewer);
    }
}