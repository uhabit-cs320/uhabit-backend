package edu.zoomass.uhabit.backend.habit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HabitRepository extends JpaRepository<Habit, Long> {

    List<Habit> findByOwnerId(long ownerId);
    List<Habit> findByVisibility(HabitVisibility visibility);
    List<Habit> findByVisibilityAndOwnerIdNot(HabitVisibility visibility, long ownerId);
    boolean existsById(Long id);
}
