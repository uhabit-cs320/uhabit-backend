package edu.zoomass.uhabit.backend.friendrequest;

import edu.zoomass.uhabit.backend.user.User;

import java.util.List;

public interface FriendRequestService {
    void sendFriendRequest(final User self, final User target);

    List<User> getAllIncomingFriendRequests(final User user);
}
