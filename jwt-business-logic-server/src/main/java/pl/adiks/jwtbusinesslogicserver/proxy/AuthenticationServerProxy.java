package pl.adiks.jwtbusinesslogicserver.proxy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import pl.adiks.jwtbusinesslogicserver.domain.User;

@Component
public class AuthenticationServerProxy {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${auth.server.base.url}")
    private String baseUrl;

    public void sendAuth(String username, String password) {
        String url = baseUrl + "/user/auth";

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        HttpEntity<User> userHttpEntity = new HttpEntity<>(user);

        restTemplate.postForEntity(url, userHttpEntity, Void.class);
    }

    public boolean sendOtp(String username, String code) {
        String url = baseUrl + "/otp/check";

        User user = new User();
        user.setUsername(username);
        user.setCode(code);

        HttpEntity<User> userHttpEntity = new HttpEntity<>(user);

        ResponseEntity<Void> response = restTemplate.postForEntity(url, userHttpEntity, Void.class);

        return response.getStatusCode().equals(HttpStatus.OK);
    }
}
