package pl.adiks.jwtauthenticationserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.adiks.jwtauthenticationserver.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByUsername(String username);
}
