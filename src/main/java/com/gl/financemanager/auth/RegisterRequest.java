package com.gl.financemanager.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterRequest {

    @Schema(description = "User name", example = "username", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;

    @Schema(description = "Password", example = "password", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;
}
