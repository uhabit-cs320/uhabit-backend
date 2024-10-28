package edu.zoomass.uhabit.backend.habit.record;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class HabitRecord {
    @Id
    private long id;

    private long habidId;
    private long userId;

    private boolean privateHabit;

    @ElementCollection
    private List<Date> completedDates;


}
