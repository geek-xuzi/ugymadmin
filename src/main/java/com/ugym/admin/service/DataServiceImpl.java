package com.ugym.admin.service;

import com.ugym.admin.bean.Motion;
import com.ugym.admin.dao.MotionDao;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xuzi on 2017/5/10.
 */
@Service
public class DataServiceImpl implements DataService {

    @Autowired
    private MotionDao motionDao;

    public List<Motion> getData() {
        long start = System.currentTimeMillis();
        List<Motion> motionData = motionDao.getMotionData();
        System.out.println(System.currentTimeMillis() - start);
        return motionData;
    }
}
