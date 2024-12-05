package edu.zoomass.uhabit.backend.habit.record;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
public class HabitRecordServiceImpl implements HabitRecordService {
    @Autowired
    private HabitRecordRepository repository;

    @Override
    public List<HabitRecord> getUserActiveHabitRecords(final long userId) {
        return repository.getActiveHabitsByUserId(userId);
    }

    @Override
    @Transactional
    public void submitDailyHabitRecord(long userId, long habitId) {
        HabitRecord record = repository.findByUserIdAndAndHabitId(userId, habitId)
                .orElseGet(() -> {
                    HabitRecord newRecord = HabitRecord.builder()
                            .userId(userId)
                            .habitId(habitId)
                            .status(HabitRecordStatus.ACTIVE)
                            .privateHabit(false)

                            // Initialize with today's date to have it removed after
                            .completedDates(new HashSet<>(Set.of(LocalDate.now())))
                            .build();
                    return repository.save(newRecord);
                });

        System.out.println("record.getCompletedDates() = " + record.getCompletedDates());

        // Remove on second click
        if (record.getCompletedDates().contains(LocalDate.now())) {
            record.getCompletedDates().remove(LocalDate.now());
            repository.save(record);
            return;
        }

        // add on first click
        record.getCompletedDates().add(LocalDate.now());
        repository.save(record);
    }

    @Override
    public Optional<HabitRecord> getHabitRecord(long userId, long habitId) {
        return repository.findByUserIdAndAndHabitId(userId, habitId);
    }
}
