package com.ugym.admin.dao;

import com.ugym.admin.bean.Motion;
import java.util.List;

/**
 * @author zheng.xu
 * @since 2017-05-11
 */
public interface MotionDao {

    List<Motion> getMotionData();
}
