package com.auto.trade.controller;

import com.auto.trade.services.DataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;

@Controller
public class MainController {
    private static final Logger log = LoggerFactory.getLogger(MainController.class);

    @Autowired
    private DataService dataService;


    @RequestMapping("/main")
    public String getMain(HashMap<String,Object> map,
                      @RequestParam(value="userId",defaultValue="userId") String userId) {
        // 获取请求body
        map.put("userId",userId);

        return "main";
    }

}
