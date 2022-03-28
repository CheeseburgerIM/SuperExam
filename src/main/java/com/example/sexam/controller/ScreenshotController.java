package com.example.sexam.controller;

import com.example.sexam.embed.screenshot_key;
import com.example.sexam.entity.screenshot;
import com.example.sexam.repository.ScreenshotRepository;
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
import java.util.List;
import java.util.UUID;

import static com.example.sexam.utils.MyFunction.isLoggedIn;

@RestController
@RequestMapping(value = "/screenshot/api")
public class ScreenshotController {

    @Autowired
    ScreenshotRepository screenshotRepository;

    @Value("${root}")
    private String root;

    @RequestMapping(value = "take")
    private MyJson take(HttpServletRequest request,
                        @RequestParam("eid") String eid,
                        @RequestParam("studentUsername") String studentUsername,
                        @RequestParam("image") MultipartFile image) {
        MyJson myJson = isLoggedIn(request);
        if (myJson.getStatus() == 403) return myJson;
        File directory = new File(root + "/screenshot/");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        String fileName = UUID.randomUUID().toString().replaceAll("-", "") + ".png";
        String filePath = new File(root).getAbsolutePath() + "/screenshot/" + fileName;
        try {
            image.transferTo(new File(filePath));
            screenshot s = new screenshot(new screenshot_key(eid, studentUsername), "/screenshot/" + fileName);
            screenshotRepository.save(s);
        } catch (IOException e) {
            e.printStackTrace();
            myJson.setStatus(403);
            myJson.setMessage("Unexpected Error");
        }
        myJson.setStatus(200);
        myJson.setMessage("保存截图");
        myJson.setResult("/screenshot/" + fileName);
        return myJson;
    }

    @RequestMapping(value = "get")
    private MyJson get(HttpServletRequest request,
                       @RequestParam("eid") String eid,
                       @RequestParam("studentUsername") String studentUsername) {
        MyJson myJson = isLoggedIn(request);
        if (myJson.getStatus() == 403) return myJson;
        myJson.setMessage("获取本考试" + studentUsername + "学生的全部截图");
        myJson.setStatus(200);
        List<screenshot> result = screenshotRepository.findAllScreenShotByEidAndStudentUsername(eid, studentUsername);
        myJson.setResult(result);
        return myJson;
    }

}
