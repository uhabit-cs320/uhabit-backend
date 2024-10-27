package edu.zoomass.uhabit.backend.friend;

import edu.zoomass.uhabit.backend.user.User;
import edu.zoomass.uhabit.backend.user.exception.InvalidUserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class FriendServiceImpl implements FriendService{

    @Autowired
    private FriendRepository friendRepository;

    @Override
    public Set<User> getFriends(final User user) {
        if (user == null) {
            throw new InvalidUserException("User is null");
        }

        return friendRepository.getFriends(user);
    }

    @Override
    public void addFriend(User user, User friend) {
        final Friend friendEntity = Friend.builder()
                .userId(user.getId())
                .friendId(friend.getId())
                .status(FriendStatus.FRIEND)
                .createdDate(new Date())
                .updatedDate(new Date())
                .build();

        friendRepository.save(friendEntity);

        final Friend otherFriendEntity = Friend.builder()
                .userId(friend.getId())
                .friendId(user.getId())
                .status(FriendStatus.FRIEND)
                .createdDate(new Date())
                .updatedDate(new Date())
                .build();

        friendRepository.save(otherFriendEntity);
    }

    @Override
    public void removeFriend(User user, User friend) {
        final Friend friendEntity = friendRepository.findByUserIdAndFriendId(user.getId(), friend.getId());
        final Friend otherFriendEntity = friendRepository.findByUserIdAndFriendId(friend.getId(), user.getId());

        _unfriend(friendEntity);
        _unfriend(otherFriendEntity);
    }

    private void _unfriend(Friend friend) {
        friend.setStatus(FriendStatus.REMOVED);
        friend.setUpdatedDate(new Date());
        friendRepository.save(friend);
    }
}
