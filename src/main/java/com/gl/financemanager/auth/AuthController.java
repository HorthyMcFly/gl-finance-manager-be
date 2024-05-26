package com.gl.financemanager.auth;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<LoginResponse> login(Authentication authentication) {
        return new ResponseEntity<>(authService.login(authentication),
            HttpStatusCode.valueOf(200));
    }

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<Void> register(@RequestBody @Valid  RegisterRequest registerRequest) {
        try {
            authService.register(registerRequest);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatusCode.valueOf(409));
        }
        return new ResponseEntity<>(HttpStatusCode.valueOf(200));
    }

    @PostMapping("/change-password")
    @ResponseBody
    public ResponseEntity<Void> changePassword(@RequestBody @Valid  ChangePasswordRequest changePasswordRequest) {
        authService.changePassword(changePasswordRequest);
        return new ResponseEntity<>(HttpStatusCode.valueOf(200));
    }

}
