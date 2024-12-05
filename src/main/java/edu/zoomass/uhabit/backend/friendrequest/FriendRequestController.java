package edu.zoomass.uhabit.backend.friendrequest;

import edu.zoomass.uhabit.backend.user.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/friend-requests")
public class FriendRequestController {

    @Autowired
    private FriendRequestService friendRequestService;

    @PostMapping("/{targetId}")
    public ResponseEntity<FriendRequest> sendFriendRequest(@AuthenticationPrincipal UserProfile profile, @PathVariable long targetId) {
        if (profile == null) {
            return ResponseEntity.badRequest().build();
        }
        final FriendRequest request = friendRequestService.sendFriendRequest(profile.getId(), targetId);
        return ResponseEntity.ok(request);
    }

    @GetMapping("/incoming")
    public ResponseEntity<List<FriendRequest>> getIncomingFriendRequests(@AuthenticationPrincipal UserProfile profile) {
        if (profile == null) {
            return ResponseEntity.badRequest().build();
        }
        List<FriendRequest> incomingRequests = friendRequestService.getAllIncomingFriendRequests(profile.getId());
        return ResponseEntity.ok(incomingRequests);
    }

    @GetMapping("/outgoing")
    public ResponseEntity<List<FriendRequest>> getOutgoingFriendRequests(@AuthenticationPrincipal UserProfile profile) {
        if (profile == null) {
            return ResponseEntity.badRequest().build();
        }
        List<FriendRequest> outgoingRequests = friendRequestService.getAllOutgoingFriendRequests(profile.getId());
        return ResponseEntity.ok(outgoingRequests);
    }

    @PostMapping("/accept/{senderId}")
    public ResponseEntity<FriendRequest> acceptFriendRequest(@AuthenticationPrincipal UserProfile profile, @PathVariable long senderId) {
        if (profile == null) {
            return ResponseEntity.badRequest().build();
        }
        final FriendRequest request = friendRequestService.acceptFriendRequest(profile.getId(), senderId);
        return ResponseEntity.ok(request);
    }

    @PostMapping("/reject/{senderId}")
    public ResponseEntity<FriendRequest> rejectFriendRequest(@AuthenticationPrincipal UserProfile profile, @PathVariable long senderId) {
        if (profile == null) {
            return ResponseEntity.badRequest().build();
        }
        final FriendRequest request = friendRequestService.rejectFriendRequest(profile.getId(), senderId);
        return ResponseEntity.ok(request);
    }

    @PostMapping("/cancel/{targetId}")
    public ResponseEntity<FriendRequest> cancelFriendRequest(@AuthenticationPrincipal UserProfile profile, @PathVariable long targetId) {
        if (profile == null) {
            return ResponseEntity.badRequest().build();
        }
        final FriendRequest request = friendRequestService.cancelFriendRequest(profile.getId(), targetId);
        return ResponseEntity.ok(request);
    }


}