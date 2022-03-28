package com.example.sexam.controller;

import com.example.sexam.VO.Correct;
import com.example.sexam.VO.Exam;
import com.example.sexam.VO.Question;
import com.example.sexam.embed.*;
import com.example.sexam.entity.*;
import com.example.sexam.repository.*;
import com.example.sexam.utils.MyJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.example.sexam.utils.MyFunction.getTime;
import static com.example.sexam.utils.MyFunction.isLoggedIn;

@RestController
@RequestMapping(value = "/exam/api")
public class ExamController {

    @Autowired
    ExamRepository examRepository;

    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    UserInfoRepository userInfoRepository;

    @Autowired
    DoneQuestionRepository doneQuestionRepository;

    @Autowired
    RelationRepository relationRepository;

    @Autowired
    ClassExamRepository classExamRepository;

    @Autowired
    UserCalendarRepository userCalendarRepository;

    @Autowired
    UserLogRepository userLogRepository;

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    ClassStudentRepository classStudentRepository;

    @RequestMapping(value = "/create")
    private MyJson create(HttpServletRequest request,
                          @RequestParam("teacherUsername") String teacherUsername,
                          @RequestParam("title") String title,
                          @RequestParam("tips") String tips,
                          @RequestParam("startTime") String startTime,
                          @RequestParam("endTime") String endTime,
                          @RequestParam("examClass") String examClass,
                          @RequestParam("course") String course,
                          @RequestParam("duration") int duration,
                          @RequestParam("totalScore") double totalScore,
                          @RequestParam("questionCnt") int questionCnt,
                          @RequestBody module[] modules) {
        // 判断用户登陆状态
        MyJson myJson = isLoggedIn(request);
        if (myJson.getStatus() == 403) return myJson;
        String eid = UUID.randomUUID().toString().replaceAll("-", "");
//        划分班级

        String[] classes = examClass.split("-");
        for (String aClass : classes) {
            classexam_key clk = new classexam_key(aClass, eid);
            classexam cl = new classexam(clk);
            classExamRepository.save(cl);
        }
        // 创建模块
        String modulesId = "";
        for (module m : modules) {
            String mid = UUID.randomUUID().toString().replaceAll("-", "");
            m.setMid(mid);
            moduleRepository.save(m);
            modulesId = modulesId + "-" + mid;
        }
        // 创建考试
        exam e = new exam(eid, teacherUsername, tips, title, startTime, endTime, course, duration, modulesId, totalScore, questionCnt);
        examRepository.save(e);

//        创建relations
        for (String aClass : classes) {
            List<classstudent> cll = classStudentRepository.findAllStudentsByCid(aClass);
            for (classstudent cl : cll) {
                relations_key rek = new relations_key(eid, teacherUsername, cl.getId().getUsername());
                if (!relationRepository.existsById(rek)) {
                    relations re = new relations(rek, 0, startTime, "", 0, -1);
                    relationRepository.save(re);
                }
            }
        }
        myJson.setResult(eid);
        myJson.setStatus(200);
        myJson.setMessage("试卷创建成功");
        return myJson;
    }

    //    编辑试卷
    @RequestMapping(value = "/edit")
    private MyJson edit(HttpServletRequest request,
                        @RequestParam("eid") String eid,
                        @RequestParam("teacherUsername") String teacherUsername,
                        @RequestParam("title") String title,
                        @RequestParam("tips") String tips,
                        @RequestParam("startTime") String startTime,
                        @RequestParam("endTime") String endTime,
                        @RequestParam("examClass") String examClass,
                        @RequestParam("course") String course,
                        @RequestParam("duration") int duration,
                        @RequestParam("totalScore") double totalScore,
                        @RequestParam("questionCnt") int questionCnt,
                        @RequestBody module[] modules) {
        MyJson myJson = isLoggedIn(request);
        if (myJson.getStatus() == 403) return myJson;
//        划分班级
        String[] classes = examClass.split("-");
        Set<String> s = classExamRepository.findAllClassByEid(eid);
        for (String s1 : s) {
            classexam_key clk = new classexam_key(s1, eid);
            classExamRepository.deleteById(clk);
        }
        for (String aClass : classes) {
            classexam_key clk = new classexam_key(aClass, eid);
            classexam cl = new classexam(clk);
            classExamRepository.save(cl);
        }
//        取出试卷
        exam e = examRepository.getById(eid);
        e.setTeacherUsername(teacherUsername);
        e.setTitle(title);
        e.setTips(tips);
        e.setStartTime(startTime);
        e.setEndTime(endTime);
        e.setCourse(course);
        e.setTotalScore(totalScore);
        e.setQuestionCnt(questionCnt);
        e.setDuration(duration);
        String modulesId = "";
        // 重新创建模块
        for (module m : modules) {
            String mid = UUID.randomUUID().toString().replaceAll("-", "");
            m.setMid(mid);
            moduleRepository.save(m);
            modulesId = modulesId + "-" + mid;
        }
//        覆盖之前的
        e.setModulesId(modulesId);
        examRepository.save(e);
        myJson.setResult(eid);
        myJson.setStatus(200);
        myJson.setMessage("试卷创建成功");
        return myJson;
    }

    //    获取试卷信息
    @RequestMapping(value = "/getInfo")
    private MyJson getInfo(HttpServletRequest request,
                           @RequestParam("eid") String eid
    ) {
        MyJson myJson = isLoggedIn(request);
        if (myJson.getStatus() == 403) return myJson;
        exam e = examRepository.findExamById(eid);
        Set<String> classexamList = classExamRepository.findAllClassByEid(eid);
        Map<String, Object> result = new HashMap<>();
        result.put("exam", e);
        result.put("class", classexamList);
        myJson.setStatus(200);
        myJson.setResult(result);
        myJson.setMessage("success");
        return myJson;
    }

    //    开始考试
    @RequestMapping(value = "/startExam")
    private MyJson startExam(HttpServletRequest request,
                             @RequestParam("eid") String eid,
                             @RequestParam("username") String username) {
        MyJson myJson = isLoggedIn(request);
        if (myJson.getStatus() == 400) return myJson;
        exam e = examRepository.getById(eid);
        relations_key rek = new relations_key(eid, e.getTeacherUsername(), username);
        String startTime = getTime();
        relations re = relationRepository.getById(rek);
        re.setStartTime(startTime);
        relationRepository.save(re);
//        活跃度增加
        user_calendar c = userCalendarRepository.getById(new user_calendar_key(username, getTime().substring(0, 10)));
        if (c.getActivity() < 4) {
            c.setActivity(c.getActivity() + 1);
        }
        myJson.setStatus(200);
        myJson.setResult(eid);
        myJson.setMessage("考试顺利");
        user_log l = new user_log(new user_log_key(username, getTime()), "参加了一场考试:" + e.getTitle());
        userLogRepository.save(l);
        return myJson;
    }

    //    获取试卷题目
    @RequestMapping(value = "/getQuestions")
    private MyJson getQuestions(HttpServletRequest request,
                                @RequestParam("eid") String eid) {
        MyJson myJson = isLoggedIn(request);
        if (myJson.getStatus() == 403) return myJson;
        exam e = examRepository.getById(eid);
        String[] modulesId = e.getModulesId().split("-");
        List<Object> modules = new ArrayList<>();
        for (int i = 1; i < modulesId.length; i++) {
            Map<String, Object> map = new HashMap<>();
            module m = moduleRepository.getById(modulesId[i]);
            map.put("title", m.getTitle());
            map.put("mid", m.getMid());
            String[] questionsId = m.getQuestionId().split("-");
            List<Object> questionList = new ArrayList<>();
            for (int j = 0; j < questionsId.length; j++) {
                question q = questionRepository.findQuestionById(questionsId[j]);
//                questionList.add(q);
                Map<String, Object> question = new HashMap<>();
                question.put("id", q.getQid());
                question.put("title", q.getTitle());
                question.put("score", q.getScore());
                question.put("questionType", q.getQtype());
//                简答题不需要
                if (q.getQtype() != 4) {
                    String[] tmp = q.getItems().split("<sep1>");//题目之间使用<sep1>
                    List<Object> items = new ArrayList<>();
                    for (String s : tmp) {
                        String[] cnt = s.split("<sep2>");//前缀与选项内容使用<sep2>
                        Map<String, Object> obj = new HashMap<>();
                        obj.put("prefix", cnt[0]);
                        obj.put("content", cnt[1]);
                        items.add(obj);
                    }
                    question.put("items", items);
                }
                questionList.add(question);
            }
            map.put("questionList", questionList);
            modules.add(map);
        }
        myJson.setResult(modules);
        myJson.setStatus(200);
        myJson.setMessage("success");
        return myJson;
    }


    //    机器判卷
    @RequestMapping(value = "/submit")
    private MyJson submit(HttpServletRequest request,
                          @RequestParam("username") String username,
                          @RequestParam("eid") String eid,
                          @RequestBody Question[] questionList
    ) {
        MyJson myJson = isLoggedIn(request);
        if (myJson.getStatus() == 403) return myJson;
//        更改学生参加的考核次数和做题次数
//        更改relations,doneQuestions,question
//        修改正确率等指标
        user_info u = userInfoRepository.getById(username);
        u.setWarTimes(u.getWarTimes() + 1);
        u.setQuestions(u.getQuestions() + questionList.length);
        userInfoRepository.save(u);
        int correctCnt = 0;
        double total = 0;
//        获取提交时间
        String submitTime = getTime();
        exam e = examRepository.getById(eid);
        relations_key rek1 = new relations_key(eid, e.getTeacherUsername(), username);
        relations re1 = relationRepository.getById(rek1);
        re1.setSubmitTime(submitTime);
        int duration = calculatetimeGapMinute(re1.getStartTime(), submitTime);
        if (duration >= 10) {
            re1.setScore(0);
            re1.setExamState(2);//作弊被发现
            re1.setRightCnt(0);
            relationRepository.save(re1);
            myJson.setStatus(200);
            myJson.setMessage("cheat!");
            return myJson;
        }
        relationRepository.save(re1);
        boolean hasShortAnswer = false;
        for (Question myq : questionList) {
            doneQuestion dq = null;
            doneQuestion_key dqk = new doneQuestion_key(myq.getQid(), eid, username);
            question q = questionRepository.getById(myq.getQid());
            q.setCount(q.getCount() + 1);
            if (myq.getQtype() != 4 && myq.getQtype() != 5) {
//                非简答排序
//                单选多选判断
                Set<String> myAnswerSet = new HashSet<>();
                for (int i = 0; i < myq.getAnswer().length(); i++) {
                    myAnswerSet.add(String.valueOf(myq.getAnswer().charAt(i)));
                }
                Set<String> answerSet = new HashSet<>();
                for (int i = 0; i < q.getAnswer().length(); i++) {
                    answerSet.add(String.valueOf(q.getAnswer().charAt(i)));
                }
                if (setEquals(myAnswerSet, answerSet)) {
//                     答案正确
                    total += q.getScore();
                    correctCnt += 1;
                    q.setRightCnt(q.getRightCnt() + 1);
                    dq = new doneQuestion(dqk, q.getAnswer(), q.getScore(), 1, 1, myq.getQtype(), myq.getAnswer());
                } else {
//                    答案错误
                    dq = new doneQuestion(dqk, q.getAnswer(), 0, -1, 0, myq.getQtype(), myq.getAnswer());
                }
            } else if (myq.getQtype() == 4) {
//                简答题
                hasShortAnswer = true;
                dq = new doneQuestion(dqk, q.getAnswer(), 0, 0, 0, myq.getQtype(), myq.getAnswer());
            } else {
//              排序题
                if (myq.getAnswer().equals(q.getAnswer())) {
//                    答案正确
                    total += q.getScore();
                    correctCnt += 1;
                    q.setRightCnt(q.getRightCnt() + 1);
                    dq = new doneQuestion(dqk, q.getAnswer(), q.getScore(), 1, 1, myq.getQtype(), myq.getAnswer());
                } else {
                    dq = new doneQuestion(dqk, q.getAnswer(), 0, -1, 0, myq.getQtype(), myq.getAnswer());
                }

            }
//            判完一道题
            doneQuestionRepository.save(dq);
        }

        relations_key rek2 = new relations_key(eid, e.getTeacherUsername(), username);
        relations re2 = relationRepository.getById(rek2);
        re2.setRightCnt(correctCnt);
        re2.setScore(total);
        if (hasShortAnswer) {
//            有简答题需要进一步待批改
            re2.setExamState(0);
        } else {
//            试卷批改完成
            re2.setExamState(1);
            updateMyattack(eid, username, correctCnt, total, duration);
        }
        relationRepository.save(re2);
        myJson.setStatus(200);
        myJson.setMessage("success");
        return myJson;
    }

    private boolean setEquals(Set<?> set1, Set<?> set2) {
        if (set1 == null || set2 == null) {//null就直接不比了
            return false;
        }
        if (set1.size() != set2.size()) {//大小不同也不用比了
            return false;
        }
        return set1.containsAll(set2) && set2.containsAll(set1);//最后比containsAll
    }

    //    查看待审批或者已审批完成的试卷
    @RequestMapping(value = "/getAnswerPaper")
    private MyJson getAnswerPaper(HttpServletRequest request,
                                  @RequestParam("studentUsername") String studentUsername,
                                  @RequestParam("eid") String eid
    ) {
        MyJson myJson = isLoggedIn(request);
        if (myJson.getStatus() == 403) return myJson;
//        根据eid获取题目
        Map<String, Object> result = new HashMap<>();
        exam e = examRepository.getById(eid);
        result.put("studentUsername", studentUsername);
        result.put("title", e.getTitle());
        String[] modulesId = e.getModulesId().split("-");
        List<Object> modules = new ArrayList<>();
        for (int i = 1; i < modulesId.length; i++) {
            Map<String, Object> map = new HashMap<>();
            module m = moduleRepository.getById(modulesId[i]);
            map.put("title", m.getTitle());
            map.put("mid", m.getMid());
            String[] questionsId = m.getQuestionId().split("-");
            List<Object> questionList = new ArrayList<>();
            for (int j = 0; j < questionsId.length; j++) {
//                获取题目
                question q = questionRepository.getById(questionsId[j]);
                doneQuestion_key dqk = new doneQuestion_key(q.getQid(), eid, studentUsername);
//                获取学生的回答
                doneQuestion dq = doneQuestionRepository.getById(dqk);
//                questionList.add(q);
                Map<String, Object> question = new HashMap<>();
                question.put("id", q.getQid());
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
                questionList.add(question);
            }
            map.put("questionList", questionList);
            modules.add(map);
        }
        myJson.setResult(modules);
        myJson.setStatus(200);
        myJson.setMessage("success");
        return myJson;
    }

    //    提交审批
    @RequestMapping(value = "/correct")
    private MyJson correct(HttpServletRequest request,
                           @RequestParam("studentUsername") String studentUsername,
                           @RequestParam("eid") String eid,
                           @RequestParam("teacherUsername") String teacherUsername,
                           @RequestBody Correct[] correctList) {
        MyJson myJson = isLoggedIn(request);
        if (myJson.getStatus() == 403) return myJson;
        double total = 0;
        int correctCnt = 0;
        for (Correct correct : correctList) {
            if (correct.getQtype() == 4) {
//                寻找简答题
                question q = questionRepository.getById(correct.getQid());
                doneQuestion_key dqk = new doneQuestion_key(correct.getQid(), eid, studentUsername);
                doneQuestion dq = doneQuestionRepository.getById(dqk);
                total += correct.getScore();
                if (correct.getScore() == q.getScore()) {
//                    满分
                    dq.setStatus(1);
                    dq.setScore(correct.getScore());
                    dq.setIsOvercome(1);
                    q.setRightCnt(q.getRightCnt() + 1);
                    correctCnt += 1;
                } else {
//                    扣分了都为错误
                    dq.setStatus(-1);
                    dq.setScore(correct.getScore());
                }
                doneQuestionRepository.save(dq);
            }
        }
        relations_key rek = new relations_key(eid, teacherUsername, studentUsername);
        relations re = relationRepository.getById(rek);
        re.setScore(re.getScore() + total);
        re.setRightCnt(re.getRightCnt() + correctCnt);
        re.setExamState(1);
        relationRepository.save(re);
        int duration = calculatetimeGapMinute(re.getStartTime(), re.getSubmitTime());
        updateMyattack(eid, studentUsername, re.getRightCnt(), re.getScore(), duration);
        messages m = new messages(new messages_key(UUID.randomUUID().toString().replaceAll("-", ""), studentUsername), "查看试卷结果", getTime(), "您有一张新的试卷已经审批完成！赶紧去查看考试结果吧~", 0, "test");
        messageRepository.save(m);
        myJson.setStatus(200);
        myJson.setMessage("success");
        return myJson;
    }

//    //    查看老师判完的试卷
//    @RequestMapping(value = "/getCorrect")
//    private MyJson getCorrect(HttpServletRequest request,
//                              @RequestParam("studentUsername") String studentUsername,
//                              @RequestParam("eid") String eid
//    ) {
//        MyJson myJson = isLoggedIn(request);
//        if (myJson.getStatus() == 403) return myJson;
////        根据eid获取题目
//        exam e = examRepository.getById(eid);
//        Map<String, Object> result = new HashMap<>();
//        String[] modulesId = e.getModulesId().split("-");
//        List<Object> modules = new ArrayList<>();
//        for (int i = 1; i < modulesId.length; i++) {
//            Map<String, Object> map = new HashMap<>();
//            module m = moduleRepository.getById(modulesId[i]);
//            map.put("title", m.getTitle());
//            map.put("mid", m.getMid());
//            String[] questionsId = m.getQuestionId().split("-");
//            List<Object> questionList = new ArrayList<>();
//            for (int j = 0; j < questionsId.length; j++) {
////                获取题目
//                question q = questionRepository.getById(questionsId[j]);
//                doneQuestion_key dqk = new doneQuestion_key(q.getQid(), eid, studentUsername);
////                获取学生的回答
//                doneQuestion dq = doneQuestionRepository.getById(dqk);
////                questionList.add(q);
//                Map<String, Object> question = new HashMap<>();
//                question.put("id", q.getQid());
//                question.put("questionType", q.getQtype());
//                question.put("difficult", q.getDifficulty());
//                question.put("title", q.getTitle());
//                question.put("status", dq.getStatus());
//                question.put("answer", q.getAnswer());
//                question.put("studentAnswer", dq.getStudentAnswer());
//                question.put("score", q.getScore());
//                question.put("studentScore", dq.getScore());
//                question.put("analyze", q.getAnalysis());
//                if (q.getQtype() != 4) {
////                   已经机判完成的非简答题
//                    question.put("items", q.getItems());
//                }
//                questionList.add(question);
//            }
//            map.put("questionList", questionList);
//            modules.add(map);
//        }
//        myJson.setResult(modules);
//        myJson.setStatus(200);
//        myJson.setMessage("success");
//        return myJson;
//    }

    //    教师端答卷管理
    @RequestMapping(value = "/search")
    private MyJson search(HttpServletRequest request,
                          @RequestParam("teacherUsername") String teacherUsername,
                          @RequestParam("examState") int examState) {
        MyJson myJson = isLoggedIn(request);
        if (myJson.getStatus() == 403) return myJson;
        List<Object> result = new ArrayList<>();
        List<relations> relationsList = new ArrayList<>();
        if (examState == 0) {
            relationsList = relationRepository.findAllIncorrectExamByTeacherUsernameAndExamState(teacherUsername);
        } else if (examState == 1) {
            relationsList = relationRepository.findAllCorrectExamByTeacherUsernameAndExamState(teacherUsername);
        }
        for (relations r : relationsList) {
            Map<String, Object> m = new HashMap<>();
            m.put("eid", r.getId().getEid());
            m.put("examState", r.getExamState());
            m.put("title", examRepository.getById(r.getId().getEid()).getTitle());
            m.put("studentName", userInfoRepository.getById(r.getId().getStudentUsername()).getName());
            m.put("studentUsername", r.getId().getStudentUsername());
            m.put("score", r.getScore());
            m.put("accuracy", r.getRightCnt() * 100 / examRepository.getById(r.getId().getEid()).getQuestionCnt());
            m.put("duration", calculatetimeGapMinute(r.getStartTime(), r.getSubmitTime()));
            m.put("submitTime", r.getSubmitTime());
            result.add(m);
        }
        myJson.setResult(result);
        myJson.setStatus(200);
        myJson.setMessage(teacherUsername + "老师" + (examState == 0 ? "待批试卷" : "已批试卷"));
        return myJson;
    }

    //    学生端我的提交
    @RequestMapping(value = "getExams")
    private MyJson getExams(HttpServletRequest request,
                            @RequestParam("username") String username) {
        MyJson myJson = isLoggedIn(request);
        if (myJson.getStatus() == 403) return myJson;
        List<Object> result = new ArrayList<>();
        List<relations> relationsList = relationRepository.findAllExamByStudentUsername(username);
        for (relations r : relationsList) {
            Map<String, Object> obj = new HashMap<>();
            exam e = examRepository.getById(r.getId().getEid());
            obj.put("eid", e.getEid());
            obj.put("title", e.getTitle());
            obj.put("subject", e.getCourse());
            obj.put("submitTime", r.getSubmitTime());
            obj.put("questionCnt", e.getQuestionCnt());
            obj.put("examState", r.getExamState());
            obj.put("totalScore", e.getTotalScore());
            result.add(obj);
        }
        myJson.setStatus(200);
        myJson.setMessage(username + "同学的试卷");
        myJson.setResult(result);
        return myJson;
    }

    //    计算时间差多少min
    private int calculatetimeGapMinute(String time1, String time2) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        int minute = 0;
        try {
            Date date1, date2;
            date1 = simpleDateFormat.parse(time1);
            date2 = simpleDateFormat.parse(time2);
            double millisecond = date2.getTime() - date1.getTime();
            minute = (int) millisecond / (60 * 1000);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return minute;
    }


    private void updateMyattack(String eid, String username, int rightCnt, double total, int duration) {
        System.out.println(rightCnt);
        System.out.println(total);
        user_info u = userInfoRepository.getById(username);
        exam e = examRepository.getById(eid);
//        先计算学生的精准度
        int accuracy = (rightCnt / e.getQuestionCnt() * 100 + u.getAccuracy() * (u.getWarTimes() - 1)) / u.getWarTimes();
        u.setAccuracy(accuracy);
//        计算得分率
        int averagePoint = (int) (total / e.getTotalScore() * 100 + u.getAveragePoint() * (u.getWarTimes() - 1)) / u.getWarTimes();
        u.setAveragePoint(averagePoint);
//      计算优秀次数
        if (total / e.getTotalScore() * 100 >= 80) {
//            计算得出之前的优秀次数
            double tmp = u.getExcellent() / 100 * (u.getWarTimes() - 1);
//            加一
            tmp += 1;
//            赋值新的分值
            u.setExcellent((int) tmp / (u.getWarTimes()) * 100);
        }
//        计算速度
        if (duration / e.getDuration() * 100 <= 80) {
//            原理同上
            double tmp = u.getVelocity() / 100 * (u.getWarTimes() - 1);
            tmp += 1;
            u.setVelocity((int) tmp / (u.getWarTimes()) * 100);
        }
        userInfoRepository.save(u);
    }

    //班级顶部结果分析
    @RequestMapping(value = "/getExamAnalyze")
    private MyJson getExamAnalyze(HttpServletRequest request,
                                  @RequestParam("eid") String eid,
                                  @RequestParam("teacherUsername") String teacherUsername
    ) {
        MyJson myJson = isLoggedIn(request);
        if (myJson.getStatus() == 403) return myJson;
        Map<String, Object> ans = new HashMap<>();
//        获取所有批改完的试卷
        List<relations> lr = relationRepository.findAllCorrectExamByEidAndExamState(eid);
        if (lr.size() <= 0) {
            myJson.setStatus(200);
            myJson.setMessage("无已判试卷");
            return myJson;
        }
//        按照分数进行排序
        Collections.sort(lr, new Comparator<relations>() {
            @Override
            public int compare(relations o1, relations o2) {
                if (o1.getScore() >= o2.getScore()) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
        relations[] arr = lr.toArray(new relations[lr.size()]);
//        排序完成取最高分和最低分,平均分和中位分
        int max = (int) arr[arr.length - 1].getScore();
        double min = (int) arr[0].getScore();
        int center = 0;
        if (lr.size() % 2 != 0) {
            center = (int) arr[(arr.length - 1) / 2].getScore();
        } else {
            center = (int) arr[arr.length / 2 - 1].getScore();
        }
//        统计每一个分数的人数，同时计算总分来为后面平均分做准备
        double sum = 0;
        Map<Double, Integer> countMap = new HashMap<>();
        for (relations relation : arr) {
            sum += relation.getScore();
            Double score = relation.getScore();
            if (countMap.get(score) == null) {
                countMap.put(score, 1);
            } else {
                countMap.put(score, countMap.get(score) + 1);
            }
        }
        int average = (int) sum / arr.length;
        ans.put("firstPoint", max);
        ans.put("lastPoint", min);
        ans.put("averagePoint", average);
        ans.put("centerPoint", center);
        ans.put("countMap", countMap);
        myJson.setStatus(200);
        myJson.setMessage("success");
        myJson.setResult(ans);
        return myJson;
    }


    //    学生端试卷分析
    @RequestMapping(value = "/getMyAnalyze")
    private MyJson getMyAnalyze(HttpServletRequest request,
                                @RequestParam("username") String username,
                                @RequestParam("eid") String eid) {
        MyJson myJson = isLoggedIn(request);
        if (myJson.getStatus() == 403) return myJson;
        Map<String, Object> ans = new HashMap<>();
//        首先获取排名
        List<relations> rel = relationRepository.findAllCorrectExamByEidAndExamState(eid);
//        按照分数进行排序
        Collections.sort(rel, new Comparator<relations>() {
            @Override
            public int compare(relations o1, relations o2) {
                if (o1.getScore() >= o2.getScore()) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
//        排名
        int rank = 0;
//        列表中自己的位置
        int idx = 0;
        List<Double> l = new ArrayList<>();
//        relations[] arr = (relations[]) rel.toArray();
        relations[] arr = rel.toArray(new relations[rel.size()]);
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].getId().getStudentUsername().equals(username)) {
//                找到自己的排名
                rank = i + 1;
//                散点图数据生成
//                前面有5个点
                int start = i >= 5 ? i - 5 : 0;
                for (int j = start; j < i; j++) {
                    l.add(arr[j].getScore());
                }
//                记录下表索引
                idx = l.size();
                l.add(arr[i].getScore());
                int end = i + 5 < arr.length ? i + 5 : arr.length - 1;
                for (int j = i + 1; j <= end; j++) {
                    l.add(arr[j].getScore());
                }
            }
        }
        ans.put("rank", arr.length - rank + 1);
        ans.put("idx", idx);
        ans.put("myList", l);
//        试卷各模块平均分
        exam e = examRepository.getById(eid);
        List<Map<String, Object>> moduleSumList = new ArrayList<>();
        List<Double> moduleAverageList = new ArrayList<>();
        List<Double> myModuleScoreList = new ArrayList<>();
        String[] modulesId = e.getModulesId().split("-");
        for (int i = 1; i < modulesId.length; i++) {
            Map<String, Object> moduleSum = getModuleSum(modulesId[i]);
            moduleSumList.add(moduleSum);
            double average = getModuleAverage(modulesId[i]);
            moduleAverageList.add(average);
            double myModuleScore = getMyModuleScore(eid, modulesId[i], username);
            myModuleScoreList.add(myModuleScore);
        }
        ans.put("moduleSumList", moduleSumList);
        ans.put("moduleAverageList", moduleAverageList);
        ans.put("myModuleScoreList", myModuleScoreList);
        ans.put("peopleNum", arr.length);
        myJson.setStatus(200);
        myJson.setResult(ans);
        user_log log = new user_log(new user_log_key(username, getTime()), "查看了试卷" + e.getTitle() + "的结果分析");
        userLogRepository.save(log);
        return myJson;
    }

    private Map<String, Object> getModuleSum(String mid) {
        module m = moduleRepository.getById(mid);
        Map<String, Object> moduleSum = new HashMap();
        moduleSum.put("name", m.getTitle());
        moduleSum.put("color", "#000000");
        String questionsId = m.getQuestionId();
        String[] questions = questionsId.split("-");
        double sum = 0;
        for (int i = 0; i < questions.length; i++) {
            question q = questionRepository.getById(questions[i]);
            sum += q.getScore();
        }
        moduleSum.put("max", sum);
        return moduleSum;
    }

    //
    private double getModuleAverage(String mid) {
        module m = moduleRepository.getById(mid);
//        获取题目列表
        String questionsId = m.getQuestionId();
        String[] questions = questionsId.split("-");
        double sum = 0;
        for (int i = 0; i < questions.length; i++) {
            question q = questionRepository.getById(questions[i]);
//            计算平均分
            double average = (q.getRightCnt() * q.getScore() / q.getCount());
            sum += average;
        }
        return sum;
    }

    private double getMyModuleScore(String eid, String mid, String studentUsername) {
        module m = moduleRepository.getById(mid);
//        获取题目列表
        String questionsId = m.getQuestionId();
        String[] questions = questionsId.split("-");
        double sum = 0;
        for (int i = 0; i < questions.length; i++) {
            doneQuestion dq = doneQuestionRepository.getById(new doneQuestion_key(questions[i], eid, studentUsername));
            sum += dq.getScore();
        }
        return sum;
    }


    //    获取某一老师全部试卷
    @RequestMapping(value = "/getExamsByTeacher")
    private MyJson getExamsByTeacher(HttpServletRequest request,
                                     @RequestParam("teacherUsername") String teacherUsername) {
        MyJson myJson = isLoggedIn(request);
        if (myJson.getStatus() == 403) return myJson;
        myJson.setMessage(teacherUsername + "老师的全部试卷");
        myJson.setStatus(200);
        myJson.setResult(examRepository.findAllExamByTeacherUsername(teacherUsername));
        return myJson;
    }

    //    获取自己的待考试列表
    @RequestMapping(value = "/getExamByUsername")
    private MyJson getExamByUsername(HttpServletRequest request,
                                     @RequestParam("username") String username) {
        MyJson myJson = isLoggedIn(request);
        if (myJson.getStatus() == 403) return myJson;
//        首先通过username获取所有未完成的试卷
        List<relations> rel = relationRepository.findAllUnDoExamByStudentUsernameAndExamState(username);
        List<Object> result = new ArrayList<>();
        for (relations re : rel) {
            exam e = examRepository.findExamById(re.getId().getEid());
            result.add(e);
        }
        myJson.setResult(result);
        myJson.setStatus(200);
        return myJson;
    }

    //    认定为作弊
    @RequestMapping("/cheat")
    private MyJson cheat(HttpServletRequest request,
                         @RequestParam("eid") String eid,
                         @RequestParam("teacherUsername") String teacherUsername,
                         @RequestParam("studentUsername") String studentUsername) {
        MyJson myJson = isLoggedIn(request);
        if (myJson.getStatus() == 403) return myJson;
        relations re = relationRepository.getById(new relations_key(eid, teacherUsername, studentUsername));
        re.setExamState(2);
        re.setScore(0);
        re.setRightCnt(0);
        relationRepository.save(re);
        myJson.setStatus(200);
        return myJson;
    }

    //    教师端analyze试卷分析页面所需要的试题列表
    @RequestMapping(value = "/getAnalyzeQuestions")
    private MyJson getAnalyzeQuestions(HttpServletRequest request,
                                       @RequestParam("eid") String eid) {
        MyJson myJson = isLoggedIn(request);
        if (myJson.getStatus() == 403) return myJson;
        exam e = examRepository.getById(eid);
        String[] modulesId = e.getModulesId().split("-");
        List<Object> modules = new ArrayList<>();
        for (int i = 1; i < modulesId.length; i++) {
            Map<String, Object> map = new HashMap<>();
            module m = moduleRepository.getById(modulesId[i]);
            map.put("title", m.getTitle());
            map.put("mid", m.getMid());
            String[] questionsId = m.getQuestionId().split("-");
            List<Object> questionList = new ArrayList<>();
            for (int j = 0; j < questionsId.length; j++) {
                question q = questionRepository.findQuestionById(questionsId[j]);
//                questionList.add(q);
                Map<String, Object> question = new HashMap<>();
                question.put("answer", q.getAnswer());
                question.put("difficult", q.getDifficulty());
                question.put("id", q.getQid());
                question.put("title", q.getTitle());
                question.put("score", q.getScore());
                question.put("questionType", q.getQtype());
                question.put("analyze", q.getAnalysis());
//                简答题不需要
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

                    }
                    if (q.getQtype() == 5) {
//                        排序题的答案也需要额外处理
                        List<Object> answer = new ArrayList<>();
                        for (int k = 0; k < q.getAnswer().length(); k++) {
                            for (int l = 0; l < items.size(); l++) {
                                Map<String, String> answerObj = new HashMap<>();
                                if (items.get(l).get("prefix").equals(String.valueOf(q.getAnswer().charAt(k)))) {
                                    answerObj.put("prefix", items.get(l).get("prefix"));
                                    answerObj.put("content", items.get(l).get("content"));
                                    answer.add(answerObj);
                                }
                            }
                        }
                        question.put("answer", answer);
                    }
                }
                questionList.add(question);
            }
            map.put("questionList", questionList);
            modules.add(map);
        }
        myJson.setResult(modules);
        myJson.setStatus(200);
        myJson.setMessage("success");
        return myJson;
    }

}

