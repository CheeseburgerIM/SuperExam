package com.example.sexam.repository;

import com.example.sexam.embed.relations_key;
import com.example.sexam.entity.relations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RelationRepository extends JpaRepository<relations, relations_key> {

    @Query("select r from relations r where r.id.teacherUsername=?1 and r.examState=0")
    List<relations> findAllIncorrectExamByTeacherUsernameAndExamState(String teacherUsername);


    @Query("select r from relations r where r.id.teacherUsername=?1 and (r.examState=1 or r.examState=2)")
    List<relations> findAllCorrectExamByTeacherUsernameAndExamState(String teacherUsername);

    @Query("select r from relations r where r.id.studentUsername=?1")
    List<relations> findAllExamByStudentUsername(String studentUsername);

    @Query("select r from relations r where r.id.eid=?1 and (r.examState=1 or r.examState=2)")
    List<relations> findAllCorrectExamByEidAndExamState(String eid);

    @Query("select r from relations r where r.id.studentUsername=?1 and r.examState=-1")
    List<relations> findAllUnDoExamByStudentUsernameAndExamState(String studentUsername);
}
