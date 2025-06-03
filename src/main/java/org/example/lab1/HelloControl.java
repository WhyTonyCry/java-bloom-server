package org.example.lab1;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloControl {
    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }
}
