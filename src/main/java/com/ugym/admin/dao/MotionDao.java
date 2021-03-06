package com.ugym.admin.dao;

import com.ugym.admin.bean.Motion;
import com.ugym.admin.bean.OrderData;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * @author zheng.xu
 * @since 2017-05-11
 */
public interface MotionDao {

    List<Motion> getMotionDataByMonth(@Param("month") Integer month);

    List<Motion> getMotionDataByWeekDay(@Param("weekDay") Integer weekDay,
            @Param("month") int month);

    List<Motion> getMotionDataByDay(@Param("day") Integer day, @Param("month") Integer month);

    List<Motion> getAllMotionData();

    List<OrderData> getAllData();

    List<OrderData> getData(@Param("start") int start, @Param("end") int end);

    int getCount();

}
