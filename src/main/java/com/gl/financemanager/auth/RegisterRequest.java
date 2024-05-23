package com.gl.financemanager.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterRequest {

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    @Size(min = 5, max = 20)
    private String username;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    @Size(min = 8, max = 30)
    private String password;
}
