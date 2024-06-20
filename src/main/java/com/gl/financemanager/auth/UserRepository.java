package com.gl.financemanager.auth;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<FmUser, Integer> {

    Optional<FmUser> findByUsername(String username);

    FmUser findTopByOrderById();
}