package edu.zoomass.uhabit.backend.friendrequest;

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

    @Override
    public void sendFriendRequest(final UserProfile self, final UserProfile target) {
        final FriendRequest friendRequest = FriendRequest.builder()
                .receiverId(target.getId())
                .senderId(self.getId())
                .status(FriendRequestStatus.PENDING)
                .date(new Date())
                .build();

        this.friendRequestRepository.save(friendRequest);
    }

    @Override
    public List<UserProfile> getAllIncomingFriendRequests(final UserProfile user) {
        if (user == null) {
            throw new InvalidUserException("User is null");
        }

        return friendRequestRepository.getAllIncomingFriendRequests(user.getId());
    }
}
