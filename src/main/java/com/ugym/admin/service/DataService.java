package com.ugym.admin.service;

import com.ugym.admin.bean.Motion;
import com.ugym.admin.bean.OrderData;
import java.util.List;

/**
 * Created by xuzi on 2017/5/10.
 */
public interface DataService {

    List<Motion> getMotionDataByMonth(int month);

    List<Motion> getMotionDataByWeekDay(int weekDay, int month);

    List<Motion> getMotionDataByDay(int day, int month);

    List<Motion> getAllMotionData();

    List<OrderData> getAllData() throws InterruptedException;

}
