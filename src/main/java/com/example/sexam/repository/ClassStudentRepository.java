package com.example.sexam.repository;

import com.example.sexam.embed.classstudent_key;
import com.example.sexam.entity.classes;
import com.example.sexam.entity.classstudent;
import com.example.sexam.entity.sexam_user;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

public interface ClassStudentRepository extends JpaRepository<classstudent, classstudent_key> {

    @Query("select c from classstudent c where c.id.username=?1")
    List<classstudent> findAllClassesByUsername(String username);

    @Query("select s from classstudent s where s.id.cid=?1")
    List<classstudent> findAllStudentsByCid(String cid);

    @Transactional
    @Modifying
    @Query("delete from classstudent c where c.id.cid=?1")
    void deleteclassstudentByCid(String cid);

    @Query("select s.id.username from classstudent s where s.id.cid=?1")
    Set<String> findAllStudentUsernameByCid(String cid);

}
