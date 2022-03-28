package com.example.sexam.repository;

import com.example.sexam.embed.user_calendar_key;
import com.example.sexam.entity.user_calendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserCalendarRepository extends JpaRepository<user_calendar, user_calendar_key> {

    @Query("select c from user_calendar c where c.id.username=?1")
    List<user_calendar> findAllCalendarByUsername(String username);

}
