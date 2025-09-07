package pl.radek.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StartController
{
    private ObjectMapper objectMapper;
    
    @GetMapping("/")
    public String start() {
        return "Job's done!";
    }
}