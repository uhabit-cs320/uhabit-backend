package edu.zoomass.uhabit.backend.friend;

import edu.zoomass.uhabit.backend.user.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FriendServiceImpl implements FriendService{

    @Autowired
    private FriendRepository friendRepository;

    @Override
    public Set<Friend> getFriends(final long userId) {
        return friendRepository.getFriendsByUserId(userId);
    }

    @Override
    public void addFriend(long userId, long friendId) {
        final Friend friendEntity = Friend.builder()
                .userId(userId)
                .friendId(friendId)
                .status(FriendStatus.FRIEND)
                .createdDate(new Date())
                .updatedDate(new Date())
                .build();

        friendRepository.save(friendEntity);

        final Friend otherFriendEntity = Friend.builder()
                .userId(friendId)
                .friendId(userId)
                .status(FriendStatus.FRIEND)
                .createdDate(new Date())
                .updatedDate(new Date())
                .build();

        friendRepository.save(otherFriendEntity);
    }

    @Override
    public void removeFriend(long userId, long friendId) {
        final Friend friendEntity = friendRepository.findByUserIdAndFriendId(userId, friendId);
        final Friend otherFriendEntity = friendRepository.findByUserIdAndFriendId(friendId, userId);

        _unfriend(friendEntity);
        _unfriend(otherFriendEntity);
    }

    @Override
    public boolean isFriend(long userId, long friendId) {
        final Friend friendEntity = friendRepository.findByUserIdAndFriendId(userId, friendId);
        return friendEntity != null && friendEntity.getStatus() == FriendStatus.FRIEND;
    }

    @Override
    public Set<Long> getSuggestedFriends(long userId) {
        final Set<Friend> friends = friendRepository.getFriendsByUserId(userId);

        if (friends.isEmpty()) {
            return friendRepository.findRandomFriends();
        }

        final Set<Friend> friendsOfFriends = friends.stream()
                .map(friend -> friendRepository.getFriendsByUserId(friend.getFriendId()))
                .flatMap(Set::stream)
                .collect(Collectors.toSet());

        final Set<Long> friendSet = friendsOfFriends.stream()
                .filter(friend -> !friends.contains(friend))
                .map(Friend::getFriendId)
                .collect(Collectors.toSet());

        if (friendSet.size() < 6) {
            friendSet.addAll(friendRepository.findRandomFriends(6 - friendSet.size()));
        }

        return friendSet;
    }

    private void _unfriend(Friend friend) {
        friend.setStatus(FriendStatus.REMOVED);
        friend.setUpdatedDate(new Date());
        friendRepository.save(friend);
    }


}
