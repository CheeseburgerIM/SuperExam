package com.example.sexam.repository;

import com.example.sexam.embed.doneQuestion_key;
import com.example.sexam.entity.doneQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DoneQuestionRepository extends JpaRepository<doneQuestion, doneQuestion_key> {

    @Query("select d from doneQuestion d where d.id.studentUsername=?1 and d.isOvercome=0 and d.status=-1")
    List<doneQuestion> findAllUnkilledWrongQuestion(String username);

    @Query("select d from doneQuestion d where d.id.qid=?1")
    List<doneQuestion> findAllByQid(String qid);
}
