package edu.zoomass.uhabit.backend.user;

import com.fasterxml.jackson.annotation.JsonView;
import edu.zoomass.uhabit.backend.Views;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    @JsonView(Views.Private.class)
    public UserProfile getUserProfile(@AuthenticationPrincipal UserProfile profile) {
        return profile;
    }

    @GetMapping("/public-profile/{id}")
    @JsonView(Views.Public.class)
    public UserProfile getPublicProfile(@AuthenticationPrincipal UserProfile profile, @PathVariable("id") long id) {
        if (profile != null && profile.getId() == id) {
            return profile;
        }

        return userService.findById(id);
    }
}
