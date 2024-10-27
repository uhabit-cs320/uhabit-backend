package edu.zoomass.uhabit.backend.friendrequest;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity
public class FriendRequest {

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private long senderId;

    @Column(nullable = false)
    private long receiverId;

    @Column(nullable = false)
    private Date date;

    @Column(nullable = false)
    private FriendRequestStatus status;
}
