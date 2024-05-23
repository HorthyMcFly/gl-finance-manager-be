package com.gl.financemanager.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequest {

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  @NotNull
  @Size(min = 8, max = 30)
  private String newPassword;

}
