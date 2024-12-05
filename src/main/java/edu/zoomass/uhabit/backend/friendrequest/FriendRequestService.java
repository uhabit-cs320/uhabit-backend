package edu.zoomass.uhabit.backend.friendrequest;

import edu.zoomass.uhabit.backend.friend.Friend;
import edu.zoomass.uhabit.backend.user.UserProfile;

import java.util.List;

public interface FriendRequestService {
    FriendRequest sendFriendRequest(final long self, final long target);

    List<FriendRequest> getAllIncomingFriendRequests(final long user);

    List<FriendRequest> getAllOutgoingFriendRequests(final long self);

    FriendRequest acceptFriendRequest(final long self, final long target);

    FriendRequest rejectFriendRequest(final long self, final long target);

    FriendRequest cancelFriendRequest(final long self, final long target);


}
