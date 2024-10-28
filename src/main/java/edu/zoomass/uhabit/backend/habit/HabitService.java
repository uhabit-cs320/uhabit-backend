package edu.zoomass.uhabit.backend.habit;

import java.util.List;
import java.util.Optional;

public interface HabitService {
    List<Habit> getUserHabits(final long userId);

    List<Habit> getPublicHabits();

    Optional<Habit> getHabitById(final long habitId);

    List<Habit> getSuggestedHabits(final long userId);
}
