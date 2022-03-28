package com.example.sexam.repository;

import com.example.sexam.embed.user_log_key;
import com.example.sexam.entity.user_log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserLogRepository extends JpaRepository<user_log, user_log_key> {

    @Query("select l from user_log l where l.id.username=?1")
    List<user_log> findAllLogByUsername(String username);

}
