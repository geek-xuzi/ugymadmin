package com.ugym.admin.contorller;

import com.ugym.admin.bean.Motion;
import com.ugym.admin.service.DataService;
import java.util.List;
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
    public List<Motion> getData() {
        return dataService.getData();
    }

}
