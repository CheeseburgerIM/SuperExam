package com.example.sexam.controller;

import com.example.sexam.embed.classexam_key;
import com.example.sexam.embed.classstudent_key;
import com.example.sexam.embed.messages_key;
import com.example.sexam.entity.*;
import com.example.sexam.repository.*;
import com.example.sexam.utils.MyJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import java.util.*;

import static com.example.sexam.utils.MyFunction.getTime;
import static com.example.sexam.utils.MyFunction.isLoggedIn;

@RestController
@RequestMapping(value = "/class/api")
public class ClassController {

    @Autowired
    ClassRepository classRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ClassStudentRepository classStudentRepository;

    @Autowired
    ClassExamRepository classExamRepository;

    @Autowired
    RelationRepository relationRepository;

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    UserInfoRepository userInfoRepository;

    @RequestMapping(value = "/create")
    private MyJson create(HttpServletRequest request,
                          @RequestParam("username") String username,
                          @RequestParam("cname") String cname,
                          @RequestParam("teacherName") String teacherName) {
        MyJson myJson = isLoggedIn(request);
        if (myJson.getStatus() == 403) return myJson;
        sexam_user u = userRepository.getById(username);
        if (u == null) {
            myJson.setStatus(403);
            myJson.setMessage("用户不存在");
            return myJson;
        }
        String cid = UUID.randomUUID().toString().replaceAll("-", "");
        classes c = new classes(cid, username, cname, teacherName, 0, null, cid.substring(0, 7).toUpperCase(), 0);
        classRepository.save(c);
        myJson.setStatus(200);
        myJson.setMessage("班级创建成功");
        return myJson;
    }

    @RequestMapping(value = "/edit")
    private MyJson edit(HttpServletRequest request,
                        @RequestParam("cid") String cid,
                        @RequestParam("cname") String cname) {
        MyJson myJson = isLoggedIn(request);
        if (myJson.getStatus() == 403) return myJson;
        classes c = classRepository.getById(cid);
        if (c == null) {
            myJson.setStatus(403);
            myJson.setMessage("编辑班级信息");
            return myJson;
        }
        c.setCname(cname);
        classRepository.save(c);
        myJson.setStatus(200);
        return myJson;
    }


    @RequestMapping(value = "/enter")
    private MyJson enter(HttpServletRequest request,
                         @RequestParam("username") String username,
                         @RequestParam("command") String command) {
        MyJson myJson = isLoggedIn(request);
        if (myJson.getStatus() == 403) return myJson;
        sexam_user u = userRepository.getById(username);
        classes c = classRepository.findClassByCommand(command);
        if (u == null || c == null) {
            myJson.setStatus(403);
            myJson.setMessage("用户或班级不存在");
            return myJson;
        }
        classstudent cs = new classstudent(new classstudent_key(username, c.getCid()));
        classStudentRepository.save(cs);
        myJson.setMessage("班级加入成功");
        myJson.setStatus(200);
        c.setStudentNumber(classStudentRepository.findAllStudentsByCid(cs.getId().getCid()).size());
        myJson.setResult(c);
        classRepository.save(c);
        return myJson;
    }

    @RequestMapping(value = "/invite")
    private MyJson invite(HttpServletRequest request,
                          @RequestParam("cid") String cid,
                          @RequestParam("username") String username,
                          @RequestParam("teacherName") String teacherName) {
        MyJson myJson = isLoggedIn(request);
        if (myJson.getStatus() == 403) return myJson;
        if (!userRepository.existsById(username) || !classRepository.existsById(cid)) {
            myJson.setStatus(403);
            myJson.setMessage("用户或班级不存在");
            return myJson;
        }
        classes c = classRepository.getById(cid);
        if (classExamRepository.existsById(new classexam_key(username, cid))) {
            myJson.setStatus(403);
            myJson.setMessage("学生已加入该班级");
            return myJson;
        }
        classstudent cs = new classstudent(new classstudent_key(username, cid));
        classStudentRepository.save(cs);
        messages m = new messages(new messages_key(UUID.randomUUID().toString().replaceAll("-", ""), username), "班级邀请", getTime(), teacherName + "老师邀请您加入" + c.getCname() + "班级", 0, "class");
        messageRepository.save(m);
        myJson.setMessage("邀请学生加入班级");
        myJson.setStatus(200);
        c.setStudentNumber(classStudentRepository.findAllStudentsByCid(cid).size());
        classRepository.save(c);
        return myJson;
    }

    @RequestMapping(value = "/dismiss")
    private MyJson dismiss(HttpServletRequest request,
                           @RequestParam("cid") String cid) {
        MyJson myJson = isLoggedIn(request);
        if (myJson.getStatus() == 403) return myJson;
        classes c = classRepository.getById(cid);
        if (c == null) {
            myJson.setStatus(403);
            myJson.setMessage("班级不存在");
            return myJson;
        }
        classStudentRepository.deleteclassstudentByCid(cid);
        classRepository.deleteById(cid);
        myJson.setStatus(200);
        myJson.setMessage("班级解散成功");
        return myJson;
    }

    @RequestMapping(value = "/kick")
    private MyJson kick(HttpServletRequest request,
                        @RequestParam("cid") String cid,
                        @RequestParam("studentUsername") String studentUsername) {
        MyJson myJson = isLoggedIn(request);
        if (myJson.getStatus() == 403) return myJson;
        classes c = classRepository.getById(cid);
        sexam_user u = userRepository.getById(studentUsername);
        if (!classRepository.existsById(cid) || !userRepository.existsById(studentUsername)) {
            myJson.setStatus(403);
            myJson.setMessage("班级或学生不存在");
            return myJson;
        }
        if (!classStudentRepository.existsById(new classstudent_key(studentUsername, cid))) {
            myJson.setStatus(403);
            myJson.setMessage("学生不在该班级, 无法踢出");
            return myJson;
        }
        classStudentRepository.deleteById(new classstudent_key(studentUsername, cid));
        myJson.setStatus(200);
        myJson.setMessage("已踢出该学生");
        c.setStudentNumber(classStudentRepository.findAllStudentsByCid(cid).size());
        classRepository.save(c);
        return myJson;
    }

    //    搜索班级
    @RequestMapping(value = "/searchClass")
    private MyJson searchClass(HttpServletRequest request,
                               @RequestParam("command") String command) {
        MyJson myJson = isLoggedIn(request);
        if (myJson.getStatus() == 403) return myJson;
        List<Object> result = new ArrayList<>();
        List<classes> classList = classRepository.findAll();
        for (classes cl : classList) {
            if (cl.getCid().startsWith(command)) {
                result.add(cl);
            }
        }
        myJson.setStatus(200);
        myJson.setResult(result);
        return myJson;
    }

    @RequestMapping(value = "/get/single")
    private MyJson getSingle(HttpServletRequest request,
                             @RequestParam("cid") String cid) {
        MyJson myJson = isLoggedIn(request);
        if (myJson.getStatus() == 403) return myJson;
        classes c = classRepository.getById(cid);
        if (c == null) {
            myJson.setStatus(403);
            myJson.setMessage("班级不存在");
            return myJson;
        }
        List<classstudent> classstudentList = classStudentRepository.findAllStudentsByCid(cid);
        c.setStudentNumber(classstudentList.size());
        classRepository.save(c);
        myJson.setResult(classRepository.getById(cid));
        myJson.setStatus(200);
        myJson.setMessage("班级信息");
        return myJson;
    }

    @RequestMapping(value = "/get/student")
    private MyJson getStudent(HttpServletRequest request,
                              @RequestParam("cid") String cid) {
        MyJson myJson = isLoggedIn(request);
        if (myJson.getStatus() == 403) return myJson;
        classes c = classRepository.getById(cid);
        if (c == null) {
            myJson.setMessage("班级不存在");
            myJson.setStatus(403);
            return myJson;
        }
        List<classstudent> classstudentList = classStudentRepository.findAllStudentsByCid(cid);
        List<user_info> studentList = new ArrayList<>();
        for (classstudent cs : classstudentList) {
            user_info i = userInfoRepository.findInfoByUsername(cs.getId().getUsername());
            studentList.add(i);
        }
        myJson.setResult(studentList);
        myJson.setMessage("获取班级全部学生");
        myJson.setStatus(200);
        return myJson;
    }

    @RequestMapping(value = "/get/all")
    private MyJson getAll(HttpServletRequest request,
                          @RequestParam("teacherUsername") String teacherUsername) {
        MyJson myJson = isLoggedIn(request);
        if (myJson.getStatus() == 403) return myJson;
        myJson.setResult(classRepository.findAllClassesByUsername(teacherUsername));
        myJson.setStatus(200);
        myJson.setMessage(teacherUsername + "创建的全部班级");
        return myJson;
    }

    @RequestMapping(value = "/search")
    private MyJson search(HttpServletRequest request
//                          @RequestParam("cname") String cname,
//                          @RequestParam("pageIndex") int pageIndex,
//                          @RequestParam("pageSize") int pageSize
    ) {
        MyJson myJson = isLoggedIn(request);
        if (myJson.getStatus() == 403) return myJson;
//        int fromIndex = pageSize * (pageIndex - 1);
//        int toIndex = pageSize * pageIndex;
//        List<classes> classesList = new ArrayList<>();
//        if (!cname.equals("empty")) {
//            classesList = classRepository.findClassByCnameInPage(cname);
//        } else {
//            classesList = classRepository.findClassInPage();
//        }
//        if (toIndex >= classesList.size()) {
//            toIndex = classesList.size();
//        }
//        myJson.setResult(classesList.subList(fromIndex, toIndex));
        myJson.setResult(classRepository.findAll());
        myJson.setStatus(200);
        myJson.setMessage("班级筛选");
        return myJson;
    }

    @RequestMapping(value = "/info")
    private MyJson info(HttpServletRequest request,
                        @RequestParam("username") String username) {
        MyJson myJson = isLoggedIn(request);

        if (myJson.getStatus() == 403) return myJson;
        sexam_user u = userRepository.getById(username);
        if (u == null) {
            myJson.setStatus(403);
            myJson.setMessage("教师不存在");
            return myJson;
        }
        int classCnt, studentCnt, examCnt, incorrectExamCnt;
        // 计算班级数量
        List<classes> classesList = classRepository.findAllClassesByUsername(username);
        classCnt = classesList.size();
        // 计算学生人数
        Set<String> studentUsernameSet = new HashSet<>();
        for (classes c : classesList) {
            studentUsernameSet.addAll(classStudentRepository.findAllStudentUsernameByCid(c.getCid()));
        }
        studentCnt = studentUsernameSet.size();
        // 计算试卷种数
        Set<String> examIdSet = new HashSet<>();
        for (classes c : classesList) {
            examIdSet.addAll(classExamRepository.findAllExamIdByCid(c.getCid()));
        }
        examCnt = examIdSet.size();
        // 计算待批改试卷数量
        incorrectExamCnt = relationRepository.findAllIncorrectExamByTeacherUsernameAndExamState(username).size();
        // 班级人数分布
        Map<String, Integer> classStudentCnt = new HashMap<>();
        for (classes c : classesList) {
            classStudentCnt.put(c.getCname(), c.getStudentNumber());
        }
        // 班级试卷分布
        Map<String, Integer> classExamCnt = new HashMap<>();
        for (classes c : classesList) {
            classExamCnt.put(c.getCname(), classExamRepository.findAllExamIdByCid(c.getCid()).size());
        }
        Map<String, Object> result = new HashMap<>();
        result.put("ClassCnt", classCnt);
        result.put("StudentCnt", studentCnt);
        result.put("ExamCnt", examCnt);
        result.put("LeftExamCnt", incorrectExamCnt);
        result.put("ClassStudentCnt", classStudentCnt);
        result.put("ClassExamCnt", classExamCnt);
        myJson.setResult(result);
        myJson.setStatus(200);
        myJson.setMessage("管理端主页概览");
        return myJson;
    }

    @RequestMapping(value = "/get/student/class")
    private MyJson getStudentClass(HttpServletRequest request,
                                   @RequestParam("username") String username) {
        MyJson myJson = isLoggedIn(request);
        if (myJson.getStatus() == 403) return myJson;
        sexam_user u = userRepository.getById(username);
        if (u == null) {
            myJson.setStatus(403);
            myJson.setMessage("教师不存在");
            return myJson;
        }
        Set<classes> classesSet = new HashSet<>();
        List<classstudent> classstudentList = classStudentRepository.findAllClassesByUsername(username);
        for (classstudent cs : classstudentList) {
            classesSet.add(classRepository.findClassByCid(cs.getId().getCid()));
        }
        myJson.setResult(classesSet);
        myJson.setStatus(200);
        myJson.setMessage("获取" + username + "学生加入的的所有班级");
        return myJson;
    }

}
