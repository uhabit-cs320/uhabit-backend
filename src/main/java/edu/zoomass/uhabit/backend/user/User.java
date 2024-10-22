package edu.zoomass.uhabit.backend.user;

import edu.zoomass.uhabit.backend.habit.Habit;
import edu.zoomass.uhabit.backend.habit.record.HabitRecord;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Entity
@Data
@NoArgsConstructor
public class User {
    @Id
    private long id;

    @Email
    @Nonnull
    @NotBlank
    private String email;

    @ManyToMany(cascade = CascadeType.ALL)
    private Map<Habit, HabitRecord> habits;
}
