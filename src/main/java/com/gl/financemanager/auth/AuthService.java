package com.gl.financemanager.auth;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AuthService {

  private final JwtEncoder encoder;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public LoginResponse login(Authentication authentication) {
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
    return new LoginResponse(authentication.getName(), userRole, accessToken);
  }

  @Transactional
  public void register(RegisterRequest registerRequest) {
    var userToSave = FmUser.builder()
        .username(registerRequest.getUsername())
        .password(passwordEncoder.encode(registerRequest.getPassword()))
        .active(true)
        .build();
    this.userRepository.save(userToSave);
  }

  @Transactional
  public void changePassword(ChangePasswordRequest changePasswordRequest) {
    var loggedInUsername = SecurityContextHolder.getContext().getAuthentication().getName();
    var loggedInUserOpt = userRepository.findByUsername(loggedInUsername);
    assert loggedInUserOpt.isPresent();
    var loggedInUser = loggedInUserOpt.get();
    loggedInUser.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
    userRepository.save(loggedInUser);
  }
}
