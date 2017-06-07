package com.ugym.admin.contorller;

import com.google.common.base.Preconditions;
import com.ugym.admin.bean.Motion;
import com.ugym.admin.bean.OrderData;
import com.ugym.admin.common.Result;
import com.ugym.admin.service.DataService;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Resource;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
    public Result getData() throws InterruptedException {
//        List<Motion> motions = dataService.getAllMotionData();
        long start = System.currentTimeMillis();
        List<OrderData> datas = dataService.getAllData();
        System.out.println("getdata:time" + (System.currentTimeMillis() - start));
        return Result.ok(datas);
    }

    @RequestMapping("/getmonth.action")
    @ResponseBody
    public Result getMotionData(int month) {
        Preconditions.checkArgument(!(month < 0 || month > 12), "月份不合法");
        List<Motion> motions = dataService.getMotionDataByMonth(month);
        return Result.ok(motions);
    }

    @RequestMapping("/getweek.action")
    @ResponseBody
    public Result getWeekDayData(int weekDay, int month) {
        Preconditions.checkArgument(!(month < 0 || month > 12), "月份不合法");
        Preconditions.checkArgument(!(month < 0 || month > 7), "周不合法");
        List<Motion> motions = dataService.getMotionDataByWeekDay(weekDay, month);
        return Result.ok(motions);
    }


    static void fun3(ScriptObjectMirror mirror) {
        System.out.println(mirror.getClassName() + ";" + Arrays.toString(mirror.getOwnKeys(true)));
    }


}
