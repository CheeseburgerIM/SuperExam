package com.example.sexam.controller;

import com.example.sexam.entity.messages;
import com.example.sexam.repository.MessageRepository;
import com.example.sexam.utils.MyJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static com.example.sexam.utils.MyFunction.isLoggedIn;

@RestController
@RequestMapping(value = "/message/api")
public class MessageController {

    @Autowired
    MessageRepository messageRepository;

    @RequestMapping(value = "/get/all")
    private MyJson getAll(HttpServletRequest request,
                          @RequestParam("username") String username) {
        MyJson myJson = isLoggedIn(request);
        if (myJson.getStatus() == 403) return myJson;
        myJson.setStatus(200);
        myJson.setMessage("获取用户全部消息");
        myJson.setResult(messageRepository.findAllMessageByUsername(username));
        return myJson;
    }

    @RequestMapping(value = "/read")
    private MyJson read(HttpServletRequest request,
                        @RequestParam("mid") String mid) {
        MyJson myJson = isLoggedIn(request);
        if (myJson.getStatus() == 403) return myJson;
        messages m = messageRepository.findMessageByMid(mid);
        if (m == null) {
            myJson.setStatus(403);
            myJson.setMessage("消息不存在");
            return myJson;
        }
        m.setMstate(1);
        messageRepository.save(m);
        myJson.setStatus(200);
        myJson.setMessage("消息已读");
        return myJson;
    }

}
