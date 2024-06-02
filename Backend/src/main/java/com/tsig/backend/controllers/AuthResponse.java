package com.tsig.backend.controllers;

import com.tsig.backend.enums.ERole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    String message;
    String token; 
    String username;
    String role;
}
