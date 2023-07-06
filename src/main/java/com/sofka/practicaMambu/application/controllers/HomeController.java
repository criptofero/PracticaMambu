package com.sofka.practicaMambu.application.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
@RestController
@RequestMapping("/home")
public class HomeController {
    @GetMapping("/time")
    public String GetCurrentTime(){
        /*RestTemplate restTemplate = new RestTemplate();
        restTemplate.getForObject()*/
        return Instant.now().toString();
    }

    @GetMapping
    public String index(){
        var systemName = System.getProperty("os.name");
        var currentTime = Instant.now().minusSeconds(5 * 60 * 60).toString();
        return "Running on %s at %s".formatted(systemName, currentTime);
    }
}