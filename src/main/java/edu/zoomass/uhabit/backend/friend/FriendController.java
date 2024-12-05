package edu.zoomass.uhabit.backend.friend;

import edu.zoomass.uhabit.backend.user.UserProfile;
import edu.zoomass.uhabit.backend.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/friends")
public class FriendController {

    @Autowired
    private FriendService friendService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<Set<Friend>> getFriends(@AuthenticationPrincipal UserProfile profile) {
        if (profile == null) {
            return ResponseEntity.badRequest().build();
        }
        Set<Friend> friends = friendService.getFriends(profile.getId());
        return ResponseEntity.ok(friends);
    }

    @DeleteMapping("/{friendId}")
    public ResponseEntity<Void> removeFriend(@AuthenticationPrincipal UserProfile profile, @PathVariable long friendId) {
        if (profile == null) {
            return ResponseEntity.badRequest().build();
        }
        friendService.removeFriend(profile.getId(), friendId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/isFriend/{friendId}")
    public ResponseEntity<Boolean> isFriend(@AuthenticationPrincipal UserProfile profile, @PathVariable long friendId) {
        if (profile == null) {
            return ResponseEntity.badRequest().build();
        }
        boolean isFriend = friendService.isFriend(profile.getId(), friendId);
        return ResponseEntity.ok(isFriend);
    }

    @GetMapping("/suggested")
    public ResponseEntity<Set<UserProfile>> getSuggestedFriends(@AuthenticationPrincipal UserProfile profile) {
        if (profile == null) {
            return ResponseEntity.badRequest().build();
        }
        Set<Long> suggestedFriends = friendService.getSuggestedFriends(profile.getId());
        Set<UserProfile> profiles = suggestedFriends.stream()
                .map(userService::findById)
                .collect(Collectors.toSet());
        return ResponseEntity.ok(profiles);
    }
}