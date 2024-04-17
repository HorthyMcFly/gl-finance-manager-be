package com.gl.financemanager.admin;

import com.gl.financemanager.auth.FmUser;
import com.gl.financemanager.auth.Roles;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

  @PostMapping("/user")
  @ResponseBody
  public ResponseEntity<FmUser> createUser(@Valid @RequestBody FmUser user) {
    var createdUser = this.adminService.createUser(user);
    createdUser.setPassword("");
    return new ResponseEntity<>(createdUser, HttpStatusCode.valueOf(201));
  }

  @PutMapping("/user")
  @ResponseBody
  public ResponseEntity<FmUser> modifyUser(@Valid @RequestBody FmUser user) {
    var modifiedUser = this.adminService.modifyUser(user);
    modifiedUser.setPassword("");
    return new ResponseEntity<>(modifiedUser, HttpStatusCode.valueOf(200));
  }
}
