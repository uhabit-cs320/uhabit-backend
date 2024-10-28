package edu.zoomass.uhabit.backend.friendrequest;

import edu.zoomass.uhabit.backend.user.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

    @Query("SELECT f FROM FriendRequest f WHERE f.senderId = :senderId")
    FriendRequest findAllBySenderId(long senderId);

    @Query("SELECT f FROM FriendRequest f WHERE f.receiverId = :receiverId")
    FriendRequest findAllByReceiverId(long receiverId);

    @Query("SELECT u FROM UserProfile u JOIN FriendRequest f ON u.id = f.senderId WHERE f.receiverId = :userId")
    List<UserProfile> getAllIncomingFriendRequests(long userId);

    @Query("SELECT u FROM UserProfile u JOIN FriendRequest f ON u.id = f.receiverId WHERE f.senderId = :userId")
    List<UserProfile> getAllOutgoingFriendRequests(long userId);
}
