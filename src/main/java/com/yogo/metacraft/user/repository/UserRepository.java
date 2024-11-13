package com.yogo.metacraft.user.repository;


import com.yogo.metacraft.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(String id);
    void deleteById(String id);
}

