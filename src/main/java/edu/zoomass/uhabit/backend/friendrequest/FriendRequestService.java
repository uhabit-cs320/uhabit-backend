package edu.zoomass.uhabit.backend.friendrequest;

import edu.zoomass.uhabit.backend.user.UserProfile;

import java.util.List;

public interface FriendRequestService {
    void sendFriendRequest(final UserProfile self, final UserProfile target);

    List<UserProfile> getAllIncomingFriendRequests(final UserProfile user);
}
