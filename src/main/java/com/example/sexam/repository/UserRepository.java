package com.example.sexam.repository;

import com.example.sexam.entity.sexam_user;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<sexam_user,String> {

    @Query("select u from sexam_user u where u.username=?1")
    sexam_user findByUsername(String username);

}
