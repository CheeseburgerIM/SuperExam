package com.example.sexam.repository;

import com.example.sexam.embed.classexam_key;
import com.example.sexam.entity.classexam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface ClassExamRepository extends JpaRepository<classexam, classexam_key> {

    @Query("select e.id.eid from classexam e where e.id.cid=?1")
    Set<String> findAllExamIdByCid(String cid);

    @Query("select e.id.cid from classexam e where e.id.eid=?1")
    Set<String> findAllClassByEid(String eid);

}
