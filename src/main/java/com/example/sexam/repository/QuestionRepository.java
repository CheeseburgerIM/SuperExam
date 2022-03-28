package com.example.sexam.repository;

import com.example.sexam.entity.question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuestionRepository extends JpaRepository<question, String> {

    @Query(nativeQuery = true, value = "select * from SExam.question q WHERE IF(?1='empty', 1=1, ?1 like q.teacherName) AND IF(?2=-1, 1=1, q.difficulty=?2) AND IF(?3=-1, 1=1, q.score=?3) AND IF(?4=-1, 1=1, q.qtype=?4);")
    List<question> findAllQuestionWithMultiCondition(String teacherUsername, double difficulty, double score, int qtype);

    @Query("select q from question q where q.qid=?1")
    question findQuestionById(String qid);
}
