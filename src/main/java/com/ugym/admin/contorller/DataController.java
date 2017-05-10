package com.ugym.admin.contorller;

import com.ugym.admin.service.DataService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by xuzi on 2017/5/10.
 */
@Controller
@RequestMapping("/ugym/api")
public class DataController {
   @Resource
    private DataService dataService;

    @RequestMapping("/getdata.action")
    @ResponseBody
    public Map getData(){
        return dataService.getData();
    }

}