package edu.zoomass.uhabit.backend.friendrequest;

import edu.zoomass.uhabit.backend.user.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

    @Query("SELECT f FROM FriendRequest f WHERE f.senderId = :senderId")
    Set<FriendRequest> findAllBySenderId(long senderId);

    @Query("SELECT f FROM FriendRequest f WHERE f.receiverId = :receiverId")
    Set<FriendRequest> findAllByReceiverId(long receiverId);

    @Query("SELECT f FROM FriendRequest f WHERE f.senderId = :senderId AND f.receiverId = :receiverId ORDER BY f.id LIMIT 1")
    FriendRequest findBySenderIdAndReceiverId(long senderId, long receiverId);

    @Query("SELECT f FROM FriendRequest f WHERE f.receiverId = :userId")
    List<FriendRequest> getAllIncomingFriendRequests(long userId);

    @Query("SELECT f FROM FriendRequest f WHERE f.senderId = :userId")
    List<FriendRequest> getAllOutgoingFriendRequests(long userId);
}
