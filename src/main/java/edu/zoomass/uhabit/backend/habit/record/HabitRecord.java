package edu.zoomass.uhabit.backend.habit.record;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HabitRecord {
    @Id
    @GeneratedValue
    private long id;

    private long habitId;

    private long userId;

    private HabitRecordStatus status;

    private boolean privateHabit;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<LocalDate> completedDates = new HashSet<>();


}
