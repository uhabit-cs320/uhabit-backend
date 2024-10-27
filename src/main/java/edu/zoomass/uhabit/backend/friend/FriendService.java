package edu.zoomass.uhabit.backend.friend;

import edu.zoomass.uhabit.backend.user.User;

import java.util.Set;

public interface FriendService {
    Set<User> getFriends(final User user);

    void addFriend(final User user, final User friend);

    void removeFriend(final User user, final User friend);
}
