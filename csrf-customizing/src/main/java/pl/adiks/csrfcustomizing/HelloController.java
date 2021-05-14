package pl.adiks.csrfcustomizing;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @PostMapping("/hello")
    public String hello() {
        return "Hello !";
    }

    @PostMapping("/ciao")
    public String ciao() {
        return "Ciao !";
    }

    @GetMapping("/hello")
    public String getHello() {
        return "Get Hello!";
    }
}
