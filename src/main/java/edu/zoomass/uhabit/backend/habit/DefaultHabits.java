package edu.zoomass.uhabit.backend.habit;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DefaultHabits {
    private final HabitRepository habitRepository;
    private static final long SYSTEM_OWNER_ID = 0L;

    public DefaultHabits(HabitRepository habitRepository) {
        this.habitRepository = habitRepository;
    }

    @PostConstruct
    public void initializeDefaultHabits() {
        List<Habit> defaultHabits = Arrays.asList(
            createDefaultHabit(1L, "Workout", "Maintain physical health with daily exercise"),
            createDefaultHabit(2L, "Read 30 Minutes", "Develop reading habit for mental growth"),
            createDefaultHabit(3L, "Drink Water", "Stay hydrated throughout the day"),
            createDefaultHabit(4L, "Meditate", "Practice mindfulness for mental wellbeing"),
            createDefaultHabit(5L, "Sleep 8 Hours", "Maintain healthy sleep schedule")
        );

        for (Habit habit : defaultHabits) {
            if (!habitRepository.existsById(habit.getId())) {
                habitRepository.save(habit);
            }
        }
    }

    private Habit createDefaultHabit(long id, String name, String description) {
        Habit habit = new Habit();
        habit.setId(id);
        habit.setName(name);
        habit.setOwnerId(SYSTEM_OWNER_ID);
        habit.setVisibility(HabitVisibility.PUBLIC);
        return habit;
    }

    public List<Long> getDefaultHabitIds() {
        return Arrays.asList(1L, 2L, 3L, 4L, 5L);
    }
}