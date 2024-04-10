package com.gl.financemanager.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtEncoder encoder;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(JwtEncoder encoder,
                          UserRepository userRepository,
                          PasswordEncoder passwordEncoder) {
        this.encoder = encoder;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<LoginResponse> auth(Authentication authentication) {
        Instant now = Instant.now();
        long expiry = 36000L;
        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        // user always has exactly one role
        var userRole = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).toList()
                .get(0);
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiry))
                .subject(authentication.getName())
                .claim("scope", scope)
                .build();
        String accessToken = this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        var loginResponse = new LoginResponse(authentication.getName(), userRole, accessToken);
        return new ResponseEntity<>(loginResponse, HttpStatusCode.valueOf(200));
    }

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<Void> register(@RequestBody RegisterRequest registerRequest) {
        var userToSave = FmUser.builder()
                .username(registerRequest.getUsername())
                .password(this.passwordEncoder.encode(registerRequest.getPassword()))
                .build();
        this.userRepository.saveAndFlush(userToSave);
        return new ResponseEntity<>(HttpStatusCode.valueOf(200));
    }

}
