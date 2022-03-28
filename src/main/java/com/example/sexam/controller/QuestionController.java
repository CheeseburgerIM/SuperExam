package com.example.sexam.controller;

import com.example.sexam.embed.doneQuestion_key;
import com.example.sexam.embed.user_log_key;
import com.example.sexam.entity.doneQuestion;
import com.example.sexam.entity.question;
import com.example.sexam.entity.user_info;
import com.example.sexam.entity.user_log;
import com.example.sexam.repository.DoneQuestionRepository;
import com.example.sexam.repository.QuestionRepository;
import com.example.sexam.repository.UserInfoRepository;
import com.example.sexam.repository.UserLogRepository;
import com.example.sexam.utils.MyJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.example.sexam.utils.MyFunction.getTime;
import static com.example.sexam.utils.MyFunction.isLoggedIn;


@RestController
@RequestMapping(value = "/question/api")
public class QuestionController {

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    DoneQuestionRepository doneQuestionRepository;

    @Autowired
    UserLogRepository userLogRepository;

    @Autowired
    UserInfoRepository userInfoRepository;

    @Value("${root}")
    private String root;

    @RequestMapping(value = "/upload")
    private MyJson upload(HttpServletRequest request,
                          @RequestParam("teacherUsername") String teacherUsername,
                          @RequestParam("teacherName") String teacherName,
                          @RequestParam("title") String title,
                          @RequestParam("answer") String answer,
                          @RequestParam("analysis") String analysis,
                          @RequestParam("items") String items,
                          @RequestParam("score") double score,
                          @RequestParam("difficulty") double difficulty,
                          @RequestParam("qtype") int qtype) {
        // 判断登陆状态
        MyJson myJson = isLoggedIn(request);
        if (myJson.getStatus() == 403) return myJson;
        String qid = UUID.randomUUID().toString().replaceAll("-", "");
        question q = new question(qid, teacherUsername, teacherName, title, answer, analysis, items, score, difficulty, 0, 0, qtype);
        questionRepository.save(q);
        myJson.setStatus(200);
        myJson.setMessage("题目上传成功");
        return myJson;
    }

    @RequestMapping(value = "/edit")
    private MyJson edit(HttpServletRequest request,
                        @RequestParam("qid") String qid,
                        @RequestParam("title") String title,
                        @RequestParam("answer") String answer,
                        @RequestParam("analysis") String analysis,
                        @RequestParam("items") String items,
                        @RequestParam("score") double score,
                        @RequestParam("difficulty") double difficulty,
                        @RequestParam("qtype") int qtype) {
        // 判断登陆状态
        MyJson myJson = isLoggedIn(request);
        if (myJson.getStatus() == 403) return myJson;
        question q = questionRepository.getById(qid);
        q.setTitle(title);
        q.setAnswer(answer);
        q.setAnalysis(analysis);
        q.setItems(items);
        q.setScore(score);
        q.setDifficulty(difficulty);
        q.setQtype(qtype);
        questionRepository.save(q);
        myJson.setStatus(200);
        myJson.setMessage("题目修改成功");
        return myJson;
    }

    @RequestMapping(value = "/upload/image")
    private MyJson uploadImage(HttpServletRequest request,
                               @RequestParam("image") MultipartFile image) {
        MyJson myJson = isLoggedIn(request);
        if (myJson.getStatus() == 403) return myJson;
        String imageName = UUID.randomUUID().toString().replaceAll("-", "");
        String imagePath = new File(root).getAbsolutePath() + "/questionImage/" + imageName + ".png";
        File directory = new File(new File(root).getAbsolutePath() + "/questionImage/");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        try {
            image.transferTo(new File(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
            myJson.setStatus(403);
            myJson.setMessage("Unexpected Error");
            return myJson;
        }
        myJson.setStatus(200);
        myJson.setMessage("图片上传成功");
        myJson.setResult("/questionImage/" + imageName + ".png");
        return myJson;
    }

    @RequestMapping(value = "/get")
    private MyJson get(HttpServletRequest request,
                       @RequestParam("qid") String qid) {
        MyJson myJson = isLoggedIn(request);
        if (myJson.getStatus() == 403) return myJson;
        question q = questionRepository.getById(qid);

        myJson.setResult(q);
        myJson.setStatus(200);
        myJson.setMessage("返还问题信息");
        return myJson;
    }

    @RequestMapping(value = "/search")
    private MyJson search(HttpServletRequest request
//                          String teacherName, double difficulty, double score, int qtype,
//                          @RequestParam("pageIndex") int pageIndex,
//                          @RequestParam("pageSize") int pageSize
    ) {
        MyJson myJson = isLoggedIn(request);
        if (myJson.getStatus() == 403) return myJson;
//        int fromIndex = (pageIndex - 1) * pageSize;
//        int toIndex = pageIndex * pageSize;
//        List<question> questionList = questionRepository.findAllQuestionWithMultiCondition(teacherName, difficulty, score, qtype);
//        if (toIndex >= questionList.size()) {
//            toIndex = questionList.size();
//        }
//        myJson.setResult(questionList.subList(fromIndex, toIndex));
        myJson.setResult(questionRepository.findAll());
        myJson.setStatus(200);
        myJson.setMessage("问题列表");
        return myJson;
    }

    @RequestMapping(value = "/wrong")
    private MyJson wrong(HttpServletRequest request,
                         @RequestParam("username") String username) {
        MyJson myJson = isLoggedIn(request);
        if (myJson.getStatus() == 403) return myJson;
        List<Object> result = new ArrayList<>();
        List<doneQuestion> dql = doneQuestionRepository.findAllUnkilledWrongQuestion(username);
        for (doneQuestion dq : dql) {
            question q = questionRepository.getById(dq.getId().getQid());
            Map<String, Object> question = new HashMap<>();
            question.put("id", q.getQid());
            question.put("eid", dq.getId().getEid());
            question.put("questionType", q.getQtype());
            question.put("difficult", q.getDifficulty());
            question.put("title", q.getTitle());
            question.put("status", dq.getStatus());
            question.put("answer", q.getAnswer());
            question.put("studentAnswer", dq.getStudentAnswer());
            question.put("score", q.getScore());
            question.put("studentScore", dq.getScore());
            question.put("analyze", q.getAnalysis());
            if (q.getQtype() != 4) {
//                    已经机判完成的非简答题
                String[] tmp = q.getItems().split("<sep1>");//题目之间使用<sep1>
                List<Map<String, String>> items = new ArrayList<>();
                for (String s : tmp) {
                    String[] cnt = s.split("<sep2>");//前缀与选项内容使用<sep2>
                    Map<String, String> obj = new HashMap<>();
                    obj.put("prefix", cnt[0]);
                    obj.put("content", cnt[1]);
                    items.add(obj);
                }
                question.put("items", items);
//                    多选题答案需要额外处理
                if (q.getQtype() == 2) {
                    List<String> answer = new ArrayList<>();
                    for (int k = 0; k < q.getAnswer().length(); k++) {
                        answer.add(String.valueOf(q.getAnswer().charAt(k)));
                    }
                    question.put("answer", answer);
                    List<String> studentAnswer = new ArrayList<>();
                    for (int k = 0; k < dq.getStudentAnswer().length(); k++) {
                        studentAnswer.add(String.valueOf(dq.getStudentAnswer().charAt(k)));
                    }
                    question.put("studentAnswer", studentAnswer);
                }
                if (q.getQtype() == 5) {
//                        排序题的答案也需要额外处理
                    List<Object> answer = new ArrayList<>();
                    List<Object> studentAnswer = new ArrayList<>();
                    for (int k = 0; k < q.getAnswer().length(); k++) {
                        for (int l = 0; l < items.size(); l++) {
                            Map<String, String> answerObj = new HashMap<>();
                            Map<String, String> studentAnswerObj = new HashMap<>();
                            if (items.get(l).get("prefix").equals(String.valueOf(q.getAnswer().charAt(k)))) {
                                answerObj.put("prefix", items.get(l).get("prefix"));
                                answerObj.put("content", items.get(l).get("content"));
                                answer.add(answerObj);
                            }
                            if (items.get(l).get("prefix").equals(String.valueOf(dq.getStudentAnswer().charAt(k)))) {
                                studentAnswerObj.put("prefix", items.get(l).get("prefix"));
                                studentAnswerObj.put("content", items.get(l).get("content"));
                                studentAnswer.add(studentAnswerObj);
                            }
                        }
                    }
                    question.put("answer", answer);
                    question.put("studentAnswer", studentAnswer);
                }
            }
            result.add(question);
        }
        myJson.setResult(result);
        myJson.setStatus(200);
        myJson.setMessage("错题本");
        return myJson;
    }

    @RequestMapping(value = "/kill")
    private MyJson kill(HttpServletRequest request,
                        @RequestParam("qid") String qid,
                        @RequestParam("eid") String eid,
                        @RequestParam("studentUsername") String studentUsername) {
        MyJson myJson = isLoggedIn(request);
        if (myJson.getStatus() == 403) return myJson;
        doneQuestion dq = doneQuestionRepository.getById(new doneQuestion_key(qid, eid, studentUsername));
        dq.setIsOvercome(1);
        myJson.setMessage("已斩错题");
        myJson.setStatus(200);
        user_log l = new user_log(new user_log_key(studentUsername, getTime()), "斩掉一道错题");
        userLogRepository.save(l);
        user_info i = userInfoRepository.findInfoByUsername(studentUsername);
        i.setOvercomes(i.getOvercomes() + 1);
        userInfoRepository.save(i);
        return myJson;
    }

    //    题目分析
    @RequestMapping(value = "/analyze")
    private MyJson analyze(HttpServletRequest request,
                           @RequestParam("qid") String qid) {
        MyJson myJson = isLoggedIn(request);
        if (myJson.getStatus() == 403) return myJson;
        List<doneQuestion> dql = doneQuestionRepository.findAllByQid(qid);
        Map<String, Integer> ans = new HashMap<>();
        for (doneQuestion dq : dql) {
//            单选题或者判断题
            if (dq.getQtype() == 1 || dq.getQtype() == 3) {
//                统计每一个选项的频率 A
                if (ans.get(dq.getStudentAnswer()) == null) {
                    ans.put(dq.getStudentAnswer(), 1);
                } else {
                    ans.put(dq.getStudentAnswer(), ans.get(dq.getStudentAnswer()) + 1);
                }
            } else if (dq.getQtype() == 2) {
//                多选题 ACD
                for (char ch : dq.getStudentAnswer().toCharArray()) {
                    String c = String.valueOf(ch);
                    if (ans.get(c) == null) {
                        ans.put(c, 1);
                    } else {
                        ans.put(c, ans.get(c) + 1);
                    }
                }
            } else if (dq.getQtype() == 4) {
//                简答题，获取每一个得分对应的个数
                String score = String.valueOf(dq.getScore());
                if (ans.get(score) == null) {
                    ans.put(score, 1);
                } else {
                    ans.put(score, ans.get(score) + 1);
                }
            } else {
//                排序题，获取排第一、第二的选项个数
                char[] answer = dq.getStudentAnswer().toCharArray();
                for (int i = 1; i <= answer.length; i++) {
                    for (char c : answer) {
                        String key = i + String.valueOf(c);
                        if (ans.get(key) == null) {
                            ans.put(key, 1);
                        } else {
                            ans.put(key, ans.get(key) + 1);
                        }
                    }
                }
            }

        }
        myJson.setStatus(200);
        myJson.setResult(ans);
        return myJson;
    }
}
