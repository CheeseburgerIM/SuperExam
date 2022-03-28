package com.example.sexam.repository;

import com.example.sexam.embed.messages_key;
import com.example.sexam.entity.messages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MessageRepository extends JpaRepository<messages, messages_key> {

    @Query("select m from messages m where m.id.username=?1")
    List<messages> findAllMessageByUsername(String username);

    @Query("select m from messages m where m.id.mid=?1")
    messages findMessageByMid(String mid);

}
