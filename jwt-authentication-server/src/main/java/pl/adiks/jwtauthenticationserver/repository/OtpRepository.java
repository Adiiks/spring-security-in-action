package pl.adiks.jwtauthenticationserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.adiks.jwtauthenticationserver.domain.Otp;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<Otp, String> {

    Optional<Otp> findByUsername(String username);
}
