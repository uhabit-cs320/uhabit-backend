package edu.zoomass.uhabit.backend.friendrequest;

import edu.zoomass.uhabit.backend.friend.FriendService;
import edu.zoomass.uhabit.backend.user.UserProfile;
import edu.zoomass.uhabit.backend.user.exception.InvalidUserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class FriendRequestServiceImpl implements FriendRequestService {

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @Autowired
    private FriendService friendService;

    @Override
    public FriendRequest sendFriendRequest(final long self, final long target) {
        final FriendRequest friendRequest = FriendRequest.builder()
                .receiverId(target)
                .senderId(self)
                .status(FriendRequestStatus.PENDING)
                .date(new Date())
                .build();

        this.friendRequestRepository.save(friendRequest);

        return friendRequest;
    }

    @Override
    public List<FriendRequest> getAllIncomingFriendRequests(final long user) {
        return friendRequestRepository.getAllIncomingFriendRequests(user);
    }

    @Override
    public List<FriendRequest> getAllOutgoingFriendRequests(long self) {
        return friendRequestRepository.getAllOutgoingFriendRequests(self);
    }

    @Override
    public FriendRequest acceptFriendRequest(long self, long target) {
        friendService.addFriend(self, target);
        return _handleFriendRequest(target, self, FriendRequestStatus.ACCEPTED);
    }

    @Override
    public FriendRequest rejectFriendRequest(long self, long target) {
        return _handleFriendRequest(target, self, FriendRequestStatus.REJECTED);
    }

    @Override
    public FriendRequest cancelFriendRequest(long self, long target) {
        return _handleFriendRequest(self, target, FriendRequestStatus.RESCINDED);
    }

    private FriendRequest _handleFriendRequest(long sender, long target, FriendRequestStatus status) {
        final FriendRequest friendRequest = friendRequestRepository.findBySenderIdAndReceiverId(sender, target);
        if (friendRequest == null) {
            throw new InvalidUserException("Friend request not found");
        }

        friendRequest.setStatus(status);
        friendRequestRepository.save(friendRequest);

        return friendRequest;
    }
}
