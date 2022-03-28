package com.example.sexam.controller;

import com.example.sexam.embed.user_calendar_key;
import com.example.sexam.embed.user_log_key;
import com.example.sexam.entity.sexam_user;
import com.example.sexam.entity.user_calendar;
import com.example.sexam.entity.user_info;
import com.example.sexam.entity.user_log;
import com.example.sexam.repository.UserCalendarRepository;
import com.example.sexam.repository.UserInfoRepository;
import com.example.sexam.repository.UserLogRepository;
import com.example.sexam.repository.UserRepository;
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
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.example.sexam.utils.MyFunction.*;
import static com.example.sexam.utils.Token.tokenMap;

@RestController
@RequestMapping(value = "/user/api")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserInfoRepository userInfoRepository;

    @Autowired
    UserCalendarRepository userCalendarRepository;

    @Autowired
    UserLogRepository userLogRepository;

    @Value("${root}")
    private String root;

    @RequestMapping(value = "/register")
    private MyJson register(@RequestParam("username") String username,
                            @RequestParam("password") String password,
                            @RequestParam("name") String name,
                            @RequestParam("email") String email,
                            @RequestParam("phoneNumber") String phoneNumber,
                            @RequestParam("school") String school,
                            @RequestParam("sex") String sex,
                            @RequestParam("avatar") MultipartFile avatar) {
        MyJson myJson = new MyJson();
        // 判断账号是否存在
        if (userRepository.existsById(username)) {
            myJson.setMessage("用户已存在");
            myJson.setStatus(500);
            return myJson;
        }
        // 新建user存储在数据库中
        sexam_user user = new sexam_user(username, password, "student");
        userRepository.save(user);
        // 存储用户头像
        File directory = new File(root + "/userAvatar/");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        String filePath = new File(root).getAbsolutePath() + "/userAvatar/" + username + ".png";
        try {
            avatar.transferTo(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
            myJson.setStatus(500);
            myJson.setMessage("Unexpected Error");
            return myJson;
        }
        // 新建info存储在数据库中
        user_info info = new user_info(username, email, name, phoneNumber, school, sex, "/userAvatar/" + username + ".png", getTime(), 0, 0, 0, 0, 0, 0, 0);
        userInfoRepository.save(info);
        myJson.setMessage("注册成功");
        myJson.setStatus(200);
        // 注册成功
        return myJson;
    }

    @RequestMapping(value = "/login")
    private MyJson login(@RequestParam("username") String username,
                         @RequestParam("password") String password) {
        MyJson myJson = new MyJson();
        // 判断账号是否存在
        sexam_user u = userRepository.findByUsername(username);
        if (u == null || !u.getPassword().equals(password)) {
            myJson.setStatus(403);
            myJson.setMessage("Wrong username or password!");
            return myJson;
        }
        String temp = findKeyByValue(username, tokenMap);
        String token = temp == null ? UUID.randomUUID().toString().replaceAll("-", "") : temp;
        tokenMap.put(token, username);
        Map<String, String> result = new HashMap<>();
        result.put("token", token);
        result.put("status", u.getStatus());
        myJson.setResult(result);
        myJson.setStatus(200);
        myJson.setMessage("Welcome " + username);
        String timestamp = getTime();
        if(!userCalendarRepository.existsById(new user_calendar_key(username, timestamp.substring(0, 10)))){
            userCalendarRepository.save(new user_calendar(new user_calendar_key(username, timestamp.substring(0, 10)),1));
        }
        user_log l = new user_log(new user_log_key(username, getTime()), "登录");
        userLogRepository.save(l);
        return myJson;
    }

    @RequestMapping(value = "/change/info")
    private MyJson changeInfo(HttpServletRequest request,
                              @RequestParam("username") String username,
                              @RequestParam("name") String name,
                              @RequestParam("sex") String sex,
                              @RequestParam("email") String email,
                              @RequestParam("school") String school) {
        MyJson myJson = isLoggedIn(request);
        if (myJson.getStatus() == 403) return myJson;
        sexam_user u = userRepository.findByUsername(username);
        user_info i = userInfoRepository.findInfoByUsername(username);
        if (u == null || i == null) {
            myJson.setStatus(403);
            myJson.setMessage("用户不存在");
            return myJson;
        }
        i.setName(name);
        i.setSex(sex);
        i.setEmail(email);
        i.setSchool(school);
        userInfoRepository.save(i);
        myJson.setStatus(200);
        myJson.setMessage("信息修改成功");
        user_log l = new user_log(new user_log_key(username, getTime()), "修改个人信息");
        userLogRepository.save(l);
        return myJson;
    }

    @RequestMapping(value = "/change/password")
    private MyJson changePassword(HttpServletRequest request,
                                  @RequestParam("username") String username,
                                  @RequestParam("oldPwd") String oldPwd,
                                  @RequestParam("newPwd") String newPwd) {
        MyJson myJson = isLoggedIn(request);
        if (myJson.getStatus() == 403) return myJson;
        sexam_user u = userRepository.findByUsername(username);
        if (u == null) {
            myJson.setStatus(403);
            myJson.setMessage("用户不存在");
            return myJson;
        }
        if (oldPwd.equals(u.getPassword())) {
            u.setPassword(newPwd);
            userRepository.save(u);
            myJson.setStatus(200);
            myJson.setMessage("密码修改成功");
            return myJson;
        }
        myJson.setMessage("原密码错误, 验证失败");
        myJson.setStatus(403);
        return myJson;
    }

    @RequestMapping(value = "/get/info")
    private MyJson getInfo(HttpServletRequest request,
                           @RequestParam("username") String username) {
        MyJson myJson = isLoggedIn(request);
        if (myJson.getStatus() == 403) return myJson;
        myJson.setStatus(200);
        myJson.setMessage("返还" + username + "学生的个人信息");
        myJson.setResult(userInfoRepository.findInfoByUsername(username));
        return myJson;
    }

    @RequestMapping(value = "/get/all")
    private MyJson getAll(HttpServletRequest request) {
        MyJson myJson = isLoggedIn(request);
        if (myJson.getStatus() == 403) return myJson;
        myJson.setStatus(200);
        myJson.setMessage("获取全部学生");
        myJson.setResult(userInfoRepository.findAll());
        return myJson;
    }

    @RequestMapping(value = "/get/activity")
    private MyJson getActivity(HttpServletRequest request,
                               @RequestParam("username") String username) {
        MyJson myJson = isLoggedIn(request);
        if (myJson.getStatus() == 403) return myJson;
        myJson.setResult(userCalendarRepository.findAllCalendarByUsername(username));
        myJson.setStatus(200);
        myJson.setMessage("获取" + username + "用户的活跃度");
        return myJson;
    }

    @RequestMapping(value = "/get/log")
    private MyJson getLog(HttpServletRequest request,
                          @RequestParam("username") String username) {
        MyJson myJson = isLoggedIn(request);
        if (myJson.getStatus() == 403) return myJson;
        myJson.setResult(userLogRepository.findAllLogByUsername(username));
        myJson.setStatus(200);
        myJson.setMessage("获取" + username + "用户的日志");
        return myJson;
    }

}
