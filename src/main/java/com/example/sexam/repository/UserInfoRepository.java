package com.example.sexam.repository;

import com.example.sexam.entity.user_info;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserInfoRepository extends JpaRepository<user_info,String> {

    @Query("select i from user_info i where i.username=?1")
    user_info findInfoByUsername(String username);

}
