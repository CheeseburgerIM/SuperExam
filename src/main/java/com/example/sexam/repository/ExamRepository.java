package com.example.sexam.repository;

import com.example.sexam.entity.exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ExamRepository extends JpaRepository<exam, String> {
    @Query("select e from exam e where e.eid=?1")
    exam findExamById(String eid);

    @Query("select e from exam e where e.teacherUsername=?1")
    List<exam> findAllExamByTeacherUsername(String teacherUsername);
}
