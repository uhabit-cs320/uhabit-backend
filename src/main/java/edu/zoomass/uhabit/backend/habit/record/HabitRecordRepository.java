package edu.zoomass.uhabit.backend.habit.record;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HabitRecordRepository extends JpaRepository<HabitRecord, Long> {

    @Query("SELECT hr FROM HabitRecord hr WHERE hr.userId = :userId AND hr.status = edu.zoomass.uhabit.backend.habit.record.HabitRecordStatus.ACTIVE")
    List<HabitRecord> getActiveHabitsByUserId(final long userId);

    @Query("SELECT hr FROM HabitRecord hr WHERE hr.userId = ?1 AND hr.habitId = ?2")
    Optional<HabitRecord> findByUserIdAndAndHabitId(final long userId, final long habitId);
}
