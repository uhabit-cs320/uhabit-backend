package edu.zoomass.uhabit.backend.friend;

import edu.zoomass.uhabit.backend.user.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {

    @Query("SELECT f FROM Friend f WHERE f.userId = ?1")
    Set<Friend> getFriendsByUserId(long userId);

    Friend findByUserIdAndFriendId(long userId, long friendId);

    // Query for 5 random people
    @Query(value = "SELECT * FROM user_profile ORDER BY RANDOM() LIMIT 6", nativeQuery = true)
    Set<Long> findRandomFriends();

    // Query for n random people
    @Query(value = "SELECT * FROM user_profile ORDER BY RANDOM() LIMIT ?1", nativeQuery = true)
    Set<Long> findRandomFriends(int n);
}
