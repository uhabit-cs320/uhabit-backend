package edu.zoomass.uhabit.backend;

import edu.zoomass.uhabit.backend.friend.FriendService;
import edu.zoomass.uhabit.backend.friendrequest.FriendRequestService;
import edu.zoomass.uhabit.backend.user.UserProfile;
import edu.zoomass.uhabit.backend.user.UserService;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.List;
import java.util.Random;

@ShellComponent
public class ShellCommands {

    @Autowired
    private UserService userService;

    @Autowired
    private FriendRequestService friendRequestService;

    @Autowired
    private FriendService friendService;

    @ShellMethod("Generate fake users")
    public String generateUsers(@ShellOption(defaultValue = "100") int number) {
        Faker faker = new Faker();
        for (int i = 0; i < number; i++) {
            UserProfile user = new UserProfile();
            user.setEmail(faker.internet().emailAddress());
            user.setName(faker.name().fullName());
            user.setPicture(faker.avatar().image());
            userService.saveUser(user);
        }
        return number + " users generated.";
    }

    @ShellMethod("Generate fake friend requests")
    public String generateFriendRequests(@ShellOption(defaultValue = "50") int number) {
        List<UserProfile> users = userService.getAllUsers();
        Random random = new Random();
        for (int i = 0; i < number; i++) {
            UserProfile sender = users.get(random.nextInt(users.size()));
            UserProfile receiver = users.get(random.nextInt(users.size()));
            if (sender.getId() != receiver.getId()) {
                friendRequestService.sendFriendRequest(sender.getId(), receiver.getId());
            }
        }
        return number + " friend requests generated.";
    }

    @ShellMethod("Force two users to become friends")
    public String forceFriendship(@ShellOption long userId1, @ShellOption long userId2) {
        if (userId1 == userId2) {
            return "A user cannot be friends with themselves.";
        }

        friendService.addFriend(userId1, userId2);
        return "Users " + userId1 + " and " + userId2 + " are now friends.";
    }
}