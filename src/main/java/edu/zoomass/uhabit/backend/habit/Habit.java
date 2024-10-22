package edu.zoomass.uhabit.backend.habit;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Habit {

    @Id
    private long id;

    @NotNull
    @NotBlank
    private String name;
}
