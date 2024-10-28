package edu.zoomass.uhabit.backend.habit.record;

import java.util.List;
import java.util.Optional;

public interface HabitRecordService {
    List<HabitRecord> getUserActiveHabitRecords(final long userId);

    void submitDailyHabitRecord(final long userId, final long habitId);

    Optional<HabitRecord> getHabitRecord(final long userId, final long habitId);
}
