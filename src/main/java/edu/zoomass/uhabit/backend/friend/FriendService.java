package edu.zoomass.uhabit.backend.friend;

import java.util.Set;
import java.util.stream.Collectors;

public interface FriendService {
    Set<Friend> getFriends(final long userId);

    default Set<Long> getActiveFriendsIds(final long userId) {
        return getFriends(userId).stream()
                .filter(friend -> friend.getStatus() == FriendStatus.FRIEND)
                .map(Friend::getFriendId)
                .collect(Collectors.toSet());
    }

    void addFriend(final long userId, final long friendId);

    void removeFriend(final long userId, final long friendId);

    boolean isFriend(final long userId, final long friendId);

    Set<Long> getSuggestedFriends(final long userId);
}
