package edu.zoomass.uhabit.backend.friend;

import edu.zoomass.uhabit.backend.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    @Query("SELECT u FROM User u JOIN Friend f ON u.id = f.friendId WHERE f.userId = :userId")
    Set<User> getFriends(final User user);

    Friend findByUserIdAndFriendId(long userId, long friendId);


}
