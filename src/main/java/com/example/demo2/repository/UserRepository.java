package com.example.demo2.repository;

import com.example.demo2.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
//    public User findByName(String name);

    @Query("select u from User u where u.username = ?1")
    User findByUser(String username);

    @Query("select u.id from User u where u.username = ?1")
    int findById2(String username);

    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);

//    Boolean existsByPhone_number(String email);



}
