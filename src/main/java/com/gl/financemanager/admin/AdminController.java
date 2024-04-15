package com.gl.financemanager.admin;

import com.gl.financemanager.auth.FmUser;
import com.gl.financemanager.auth.Roles;
import jakarta.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/admin")
@RolesAllowed(Roles.ADMIN)
@AllArgsConstructor
public class AdminController {

  private final AdminService adminService;

  @GetMapping("/users")
  @ResponseBody
  public ResponseEntity<List<FmUser>> getUsers() {
    var usersWithoutPassword = this.adminService.getUsers().stream().peek(
        fmUser -> fmUser.setPassword("")
    ).toList();
    return new ResponseEntity<>(usersWithoutPassword,
        HttpStatusCode.valueOf(200));
  }
}
