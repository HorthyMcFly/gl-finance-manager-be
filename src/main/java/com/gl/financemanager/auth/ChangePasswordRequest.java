package com.gl.financemanager.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequest {

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  private String newPassword;

}
