package edu.zoomass.uhabit.backend.habit;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class HabitServiceImpl implements HabitService {
    private final HabitRepository habitRepository;
    private final DefaultHabits defaultHabits;

    public HabitServiceImpl(HabitRepository habitRepository, DefaultHabits defaultHabits) {
        this.habitRepository = habitRepository;
        this.defaultHabits = defaultHabits;
    }

    @Override
    public List<Habit> getUserHabits(long userId) {
        return habitRepository.findByOwnerId(userId);
    }

    @Override
    public Optional<Habit> getHabitById(long habitId) {
        return habitRepository.findById(habitId);
    }

    @Override
    public List<Habit> getPublicHabits() {
        return habitRepository.findByVisibility(HabitVisibility.PUBLIC);
    }

    @Override
    public List<Habit> getSuggestedHabits(long userId) {
        return habitRepository.findByVisibilityAndOwnerIdNot(HabitVisibility.PUBLIC, userId);
    }
}