package com.ugym.admin.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.ugym.admin.bean.Motion;
import com.ugym.admin.dao.MotionDao;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xuzi on 2017/5/10.
 */
@Service
public class DataServiceImpl implements DataService {

    private static final long EXPIRE = 30000;
    private static final String MOTION_KEY = "XUEN";


    @Autowired
    private MotionDao motionDao;

    private LoadingCache<String, List<Motion>> motionCache = CacheBuilder.newBuilder()
            .expireAfterAccess(EXPIRE, TimeUnit.MILLISECONDS).maximumSize(100)
            .build(new CacheLoader<String, List<Motion>>() {
                @Override
                public List<Motion> load(String s) throws Exception {
                    return motionDao.getMotionData();
                }
            });

    public List<Motion> getData() {
        long start = System.currentTimeMillis();
        try {
            return motionCache.get(MOTION_KEY);
        } catch (ExecutionException e) {
            return motionDao.getMotionData();
        } finally {
            System.out.println(System.currentTimeMillis() - start);

        }
    }
}
