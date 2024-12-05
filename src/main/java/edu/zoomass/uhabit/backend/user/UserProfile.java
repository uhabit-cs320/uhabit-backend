package edu.zoomass.uhabit.backend.user;

import com.fasterxml.jackson.annotation.JsonView;
import edu.zoomass.uhabit.backend.Views;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(Views.Public.class)
    private long id;

    @Nonnull
    @JsonView(Views.Private.class)
    private String email;

    @Nonnull
    @JsonView(Views.Public.class)
    private String name;

    @Nonnull
    @JsonView(Views.Public.class)
    private String picture;
}
