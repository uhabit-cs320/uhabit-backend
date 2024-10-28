package edu.zoomass.uhabit.backend.user;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class UserProfile {
    @Id
    @GeneratedValue
    private long id;

    @Nonnull
    private String email;
}
