package com.gl.financemanager.admin;

import com.gl.financemanager.auth.FmUser;
import com.gl.financemanager.auth.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AdminService {

    private final UserRepository userRespository;

    public List<FmUser> getUsers() {
        return userRespository.findAll();
    }

    public FmUser createUser(FmUser newUser) {
        var savedUser = userRespository.saveAndFlush(newUser);
        return savedUser;
    }

    public FmUser modifyUser(FmUser modifiedUser) {
        var savedUser = userRespository.saveAndFlush(modifiedUser);
        return savedUser;
    }
}
