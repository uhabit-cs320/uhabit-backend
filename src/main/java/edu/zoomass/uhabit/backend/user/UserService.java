package edu.zoomass.uhabit.backend.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserProfile saveUser(UserProfile user) {
        return userRepository.save(user);
    }

    public UserProfile getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<UserProfile> getAllUsers() {
        return userRepository.findAll();
    }

    public UserProfile findById(long id) {
        return userRepository.findById(id).orElse(null);
    }

    public List<UserProfile> getAllByHabit(long habitId) {
        return List.of();
    }
}
