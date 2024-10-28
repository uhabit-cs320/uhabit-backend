package edu.zoomass.uhabit.backend.habit.visibility.impl;

import edu.zoomass.uhabit.backend.friend.FriendService;
import edu.zoomass.uhabit.backend.habit.Habit;
import edu.zoomass.uhabit.backend.habit.HabitVisibility;
import edu.zoomass.uhabit.backend.habit.visibility.AbstractHabitVisibilityEvaluator;
import edu.zoomass.uhabit.backend.user.UserProfile;
import edu.zoomass.uhabit.backend.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
public class FriendsOnlyHabitVisibilityEvaluator extends AbstractHabitVisibilityEvaluator {

    @Autowired
    private UserService userService;
    @Autowired
    private FriendService friendService;
    

    @Override
    public boolean hasVisibility(Habit habit, UserProfile viewer) {
        if (habit.getVisibility() != HabitVisibility.FRIENDS_ONLY) {
            return false;
        }
        
        if (isOwner(habit, viewer)) {
            return true;
        }
        
        UserProfile owner = userService.findById(habit.getOwnerId());
        return friendService.getActiveFriendsIds(owner.getId()).contains(viewer.getId());
    }
}