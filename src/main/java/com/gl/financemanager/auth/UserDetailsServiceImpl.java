package com.gl.financemanager.auth;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {


    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<FmUser> userByUsername = userRepository.findByUsername(username);
        if (userByUsername.isEmpty()) {
            throw new UsernameNotFoundException("Invalid credentials!");
        }
        FmUser user = userByUsername.get();
        var role = user.isAdmin() ? Roles.ADMIN : Roles.USER;

        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(role)
                .disabled(!user.isActive())
                .build();
    }

}
