package edu.zoomass.uhabit.backend.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<UserProfile, Long> {
    UserProfile findByEmail(String email);

    @Query("SELECT u FROM UserProfile u WHERE u.name LIKE %?1% OR u.email LIKE %?1%")
    Set<UserProfile> searchUsers(String query);
}
