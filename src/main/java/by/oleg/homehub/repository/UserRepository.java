package by.oleg.homehub.repository;

import by.oleg.homehub.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String name);
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
}
