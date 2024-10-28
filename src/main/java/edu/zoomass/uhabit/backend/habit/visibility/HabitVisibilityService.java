package edu.zoomass.uhabit.backend.habit.visibility;

import edu.zoomass.uhabit.backend.habit.Habit;
import edu.zoomass.uhabit.backend.user.UserProfile;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HabitVisibilityService {
    
    private final List<HabitVisibilityEvaluator> evaluators;
    
    public HabitVisibilityService(List<HabitVisibilityEvaluator> evaluators) {
        this.evaluators = evaluators;
    }
    
    public boolean hasVisibility(Habit habit, UserProfile viewer) {
        return evaluators.stream()
                .anyMatch(evaluator -> evaluator.hasVisibility(habit, viewer));
    }
}