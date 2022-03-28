package com.example.sexam.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.Date;

@Component
@RestController
public class refresher {

    @Value("${root}")
    private String root;

    // 初始化项目
    @Scheduled(cron = "0 0 5 * * ?") // 每天早上5点
//    @Scheduled(cron = "0 */1 * * * ?") // 每隔1分钟
    @RequestMapping(value = "/pyz/refresh")
    public String scheduledRefresh() {
        refresh();
        return "refresh";
    }

    void recursionDeleteFile(File file) {
        if(file.exists()) {
            if (file.isDirectory()) {
                if (file.listFiles().length > 0) {
                    for (File temp : file.listFiles()) {
                        recursionDeleteFile(temp);
                    }
                    file.delete();
                }
                file.delete();
            } else if (file.isFile()) {
                file.delete();
            }
        }
    }

    private void refresh() {
        System.out.println("------- refresh -------");
        System.out.println(new Date());
//        System.out.print("Start - ");
//        System.out.println("End");
//        File file=new File("/pyz/sexamApi/VClass/");
////        File file=new File(root+"/SExam/");
//        if(file.exists()) {
//            System.out.println("File Exist True");
//            System.out.println(file.getName()+" - "+file.getAbsolutePath());
//            if(file.isFile()) System.out.println("File");
//            else if(file.isDirectory()) System.out.println("Directory");
//            recursionDeleteFile(file);
//            System.out.println("Delete");
//        }
//        else System.out.println("File Exist False");
        System.out.println("------- ------- -------");
    }

}
