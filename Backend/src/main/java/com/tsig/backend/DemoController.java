package com.tsig.backend;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "http://localhost:5173/")
@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class DemoController {
    
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "admin")
    public String welcomeAdmin()
    {
        return "Welcome Admin!";
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping(value = "user")
    public String welcomeUser()
    {
        return "Welcome User!";
    }
}
