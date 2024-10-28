package edu.zoomass.uhabit.backend.friend;

import edu.zoomass.uhabit.backend.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface FriendRepository extends JpaRepository<Friend, Long> {

    @Query("SELECT f FROM Friend f WHERE f.userId = ?1")
    Set<Friend> getFriendsByUserId(long userId);

    Friend findByUserIdAndFriendId(long userId, long friendId);


}
