package com.example.sexam.repository;

import com.example.sexam.embed.screenshot_key;
import com.example.sexam.entity.screenshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ScreenshotRepository extends JpaRepository<screenshot, screenshot_key> {
    @Query("select  sc from screenshot sc where sc.id.eid=?1 and sc.id.studentUsername=?2")
    List<screenshot> findAllScreenShotByEidAndStudentUsername(String eid, String studentUsername);
}
