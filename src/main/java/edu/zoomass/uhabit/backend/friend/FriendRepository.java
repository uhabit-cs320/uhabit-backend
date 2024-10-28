package edu.zoomass.uhabit.backend.friend;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {

    @Query("SELECT f FROM Friend f WHERE f.userId = ?1")
    Set<Friend> getFriendsByUserId(long userId);

    Friend findByUserIdAndFriendId(long userId, long friendId);


}
