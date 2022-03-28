package com.example.sexam.controller;

import com.example.sexam.entity.module;
import com.example.sexam.entity.question;
import com.example.sexam.repository.ModuleRepository;
import com.example.sexam.repository.QuestionRepository;
import com.example.sexam.utils.MyJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.example.sexam.utils.MyFunction.isLoggedIn;

@RestController
@RequestMapping(value = "/module/api")
public class ModuleController {

    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    QuestionRepository questionRepository;

    @RequestMapping(value = "/create")
    private MyJson create(HttpServletRequest request,
                          @RequestParam("title") String title,
                          @RequestParam("questionsId") String questionsId) {
        // 判断用户登陆状态
        MyJson myJson = isLoggedIn(request);
        if (myJson.getStatus() == 403) return myJson;
        String mid = UUID.randomUUID().toString().replaceAll("-", "");
        module m = new module(mid, title, questionsId);
        moduleRepository.save(m);
        myJson.setStatus(200);
        myJson.setResult(mid);
        myJson.setMessage("模块创建成功");
        return myJson;
    }

    //    教师端某一个模块的分析
    @RequestMapping(value = "/analyze")
    private MyJson analyze(HttpServletRequest request,
                           @RequestParam("mid") String mid) {
        // 判断用户登陆状态
        MyJson myJson = isLoggedIn(request);
        if (myJson.getStatus() == 403) return myJson;
        module m = moduleRepository.getById(mid);
//        获取题目列表
        String questionsId = m.getQuestionId();
        String[] questions = questionsId.split("-");
        Map<Integer, Double> countMap = new HashMap<>();
        for (int i = 0; i < questions.length; i++) {
            question q = questionRepository.getById(questions[i]);
//            计算平均分
            double average = (q.getRightCnt() / q.getCount() * q.getScore());
            countMap.put(i, average);
        }
        myJson.setStatus(200);
        myJson.setResult(countMap);
        return myJson;
    }
}
