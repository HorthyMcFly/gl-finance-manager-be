package com.gl.financemanager.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class FmUserDto {
  private Integer id;

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  @NotNull
  @Size(min = 5, max = 20)
  private String username;

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  private boolean admin;

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  private boolean active;

  @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  private boolean resetPassword;
}
