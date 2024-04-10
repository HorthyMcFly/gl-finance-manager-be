package com.gl.financemanager.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {

    @Schema(description = "User name", example = "username", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;

    @Schema(description = "User role", example = "ROLE_USER", requiredMode = Schema.RequiredMode.REQUIRED)
    private String role;

    @Schema(description = "JWT access token", requiredMode = Schema.RequiredMode.REQUIRED)
    private String accessToken;
}
