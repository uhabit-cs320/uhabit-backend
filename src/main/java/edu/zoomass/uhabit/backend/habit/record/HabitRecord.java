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

    @ElementCollection
    private List<Date> completedDates;
}
