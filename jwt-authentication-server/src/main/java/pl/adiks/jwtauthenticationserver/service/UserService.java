package pl.adiks.jwtauthenticationserver.service;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.adiks.jwtauthenticationserver.domain.Otp;
import pl.adiks.jwtauthenticationserver.domain.User;
import pl.adiks.jwtauthenticationserver.repository.OtpRepository;
import pl.adiks.jwtauthenticationserver.repository.UserRepository;
import pl.adiks.jwtauthenticationserver.utility.GenerateCodeUtil;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final OtpRepository otpRepository;

    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository, OtpRepository otpRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.otpRepository = otpRepository;
    }

    public void addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public void auth(User user) {
        Optional<User> userOptional = userRepository.findByUsername(user.getUsername());

        if (userOptional.isPresent()) {
            User userToVerify = userOptional.get();
            if (passwordEncoder.matches(user.getPassword(), userToVerify.getPassword())) {
                renewOtp(userToVerify);
            } else {
                throw new BadCredentialsException("Bad credentials !");
            }
        } else
            throw new BadCredentialsException("Bad credentials !");
    }

    private void renewOtp(User user) {
        String code = GenerateCodeUtil.generateCode();

        Optional<Otp> otpOptional = otpRepository.findByUsername(user.getUsername());
        if (otpOptional.isPresent()) {
            Otp otp = otpOptional.get();
            otp.setCode(code);
        } else {
            Otp otp = new Otp(user.getUsername(), code);
            otpRepository.save(otp);
        }
    }

    public boolean checkOtp(Otp otpToValidate) {
        Optional<Otp> otpOptional = otpRepository.findByUsername(otpToValidate.getUsername());

        if (otpOptional.isPresent()) {
            if ((otpToValidate.getCode().equals(otpOptional.get().getCode()))) {
                return true;
            }
        }

        return false;
    }
}
