package com.example.sexam.repository;

import com.example.sexam.entity.classes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface ClassRepository extends JpaRepository<classes,String> {

    @Query("select c from classes c where c.command=?1")
    classes findClassByCommand(String command);

    @Query("select c from classes c where c.username=?1")
    List<classes> findAllClassesByUsername(String username);

    @Query("select c from classes c")
    List<classes> findClassInPage();

    @Query("select c from classes c where c.cname=?1")
    List<classes> findClassByCnameInPage(String cname);

    @Query("select c from classes c where c.cid=?1")
    classes findClassByCid(String cid);

}
