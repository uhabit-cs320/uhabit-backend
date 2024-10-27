package edu.zoomass.uhabit.backend.friendrequest;

import edu.zoomass.uhabit.backend.user.User;
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
    public void sendFriendRequest(final User self, final User target) {
        final FriendRequest friendRequest = FriendRequest.builder()
                .receiverId(target.getId())
                .senderId(self.getId())
                .status(FriendRequestStatus.PENDING)
                .date(new Date())
                .build();

        this.friendRequestRepository.save(friendRequest);
    }

    @Override
    public List<User> getAllIncomingFriendRequests(final User user) {
        if (user == null) {
            throw new InvalidUserException("User is null");
        }

        return friendRequestRepository.getAllIncomingFriendRequests(user.getId());
    }
}
