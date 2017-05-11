package com.ugym.admin.contorller;

import com.google.common.base.Preconditions;
import com.ugym.admin.bean.Motion;
import com.ugym.admin.common.Result;
import com.ugym.admin.service.DataService;
import java.util.List;
import javax.annotation.Resource;
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
    public Result getData() {
        List<Motion> motions = dataService.getAllMotionData();
        return Result.ok(motions);
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

}
