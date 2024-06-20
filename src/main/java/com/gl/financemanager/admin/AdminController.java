package com.gl.financemanager.admin;

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
  public ResponseEntity<List<FmUserDto>> getUsers() {
    return new ResponseEntity<>(this.adminService.getUsers(),
        HttpStatusCode.valueOf(200));
  }

  @PostMapping("/user")
  @ResponseBody
  public ResponseEntity<FmUserDto> createUser(@RequestBody @Valid FmUserDto fmUserDto) {
    return new ResponseEntity<>(this.adminService.createUser(fmUserDto), HttpStatusCode.valueOf(201));
  }

  @PutMapping("/user")
  @ResponseBody
  public ResponseEntity<FmUserDto> modifyUser(@RequestBody FmUserDto fmUserDto) {
    return new ResponseEntity<>(this.adminService.modifyUser(fmUserDto), HttpStatusCode.valueOf(200));
  }

  @PostMapping("/create-first-period")
  public ResponseEntity<Void> createFirstPeriod() {
    adminService.createFirstPeriod();
    return new ResponseEntity<>(HttpStatusCode.valueOf(201));
  }

  @PostMapping("/close-active-period")
  public ResponseEntity<Void> closePeriod() {
    adminService.closeActivePeriod();
    return new ResponseEntity<>(HttpStatusCode.valueOf(200));
  }
}
