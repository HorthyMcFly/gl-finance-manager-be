package com.gl.financemanager.admin;

import com.gl.financemanager.auth.FmUser;
import com.gl.financemanager.auth.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AdminService {

    private final UserRepository userRespository;

    private final PasswordEncoder passwordEncoder;

    public List<FmUser> getUsers() {
        return userRespository.findAll();
    }

    public FmUser createUser(FmUser newUser) {
        newUser.setPassword(passwordEncoder.encode(newUser.getUsername()));
      return userRespository.saveAndFlush(newUser);
    }

    public FmUser modifyUser(FmUser modifiedUser) {
        if (modifiedUser.getPassword().equals("reset")) {
            modifiedUser.setPassword(passwordEncoder.encode(modifiedUser.getUsername()));
        }
      return userRespository.saveAndFlush(modifiedUser);
    }
}
