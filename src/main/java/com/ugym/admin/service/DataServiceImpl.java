package com.ugym.admin.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.ugym.admin.bean.CacheKey;
import com.ugym.admin.bean.Motion;
import com.ugym.admin.common.ResultTemplate;
import com.ugym.admin.dao.MotionDao;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by xuzi on 2017/5/10.
 */
@Service
public class DataServiceImpl implements DataService {

    private static final long EXPIRE = 30000;
    public static final String ALL_DATA = "XUEN";


    @Autowired
    private MotionDao motionDao;

    private LoadingCache<String, List<Motion>> allMotionCache = CacheBuilder.newBuilder()
            .expireAfterAccess(EXPIRE, TimeUnit.MILLISECONDS).maximumSize(100)
            .build(new CacheLoader<String, List<Motion>>() {
                @Override
                public List<Motion> load(String s) throws Exception {
                    return motionDao.getAllMotionData();
                }
            });
    private LoadingCache<String, List<Motion>> monthMotionCache = CacheBuilder.newBuilder()
            .expireAfterAccess(EXPIRE, TimeUnit.MILLISECONDS).maximumSize(100)
            .build(new CacheLoader<String, List<Motion>>() {
                       @Override
                       public List<Motion> load(String key) throws Exception {

                           return motionDao.getMotionDataByMonth(5);
                       }
                   }
            );

    private LoadingCache<CacheKey, List<Motion>> weekDayMotionCache = CacheBuilder.newBuilder()
            .expireAfterAccess(EXPIRE, TimeUnit.MILLISECONDS).maximumSize(100)
            .build(new CacheLoader<CacheKey, List<Motion>>() {
                @Override
                public List<Motion> load(CacheKey key) throws Exception {
                    return motionDao.getMotionDataByWeekDay(key.getWeekDay(), key.getMonth());
                }
            });

    private LoadingCache<CacheKey, List<Motion>> dayMotionCache = CacheBuilder.newBuilder()
            .expireAfterAccess(EXPIRE, TimeUnit.MILLISECONDS).maximumSize(100)
            .build(new CacheLoader<CacheKey, List<Motion>>() {
                @Override
                public List<Motion> load(CacheKey key) throws Exception {
                    return motionDao.getMotionDataByDay(key.getDay(), key.getMonth());
                }
            });

    // ok
    @Override
    public List<Motion> getMotionDataByMonth(int month) {
        return new ResultTemplate<Motion>() {

            @Override
            protected String getTag() {
                return "Month_data";
            }

            @Override
            protected List getDataByDao() {

                return motionDao.getMotionDataByMonth(month);
            }

            @Override
            protected List getDataByCache() throws ExecutionException {

                return monthMotionCache.get("");
            }
        }.executor();
    }

    // ok
    @Override
    public List<Motion> getMotionDataByWeekDay(int weekDay, int month) {
        return new ResultTemplate<Motion>() {

            @Override
            protected String getTag() {
                return "WeekDay_data";
            }

            @Override
            protected List getDataByDao() {
                return motionDao.getMotionDataByWeekDay(weekDay, month);
            }

            @Override
            protected List getDataByCache() throws ExecutionException {
                return weekDayMotionCache.get(new CacheKey(month, -1, weekDay));
            }
        }.executor();
    }

    // ok
    @Override
    public List<Motion> getMotionDataByDay(int day, int month) {
        return new ResultTemplate<Motion>() {

            @Override
            protected String getTag() {
                return "Day_data";
            }

            @Override
            protected List getDataByDao() {
                return motionDao.getMotionDataByDay(day, month);
            }

            @Override
            protected List getDataByCache() throws ExecutionException {
                return dayMotionCache.get(new CacheKey(month, day, -1));
            }
        }.executor();
    }

    // ok
    @Override
    public List<Motion> getAllMotionData() {
        return new ResultTemplate<Motion>() {


            @Override
            protected String getTag() {
                return "All_data";
            }

            @Override
            protected List getDataByDao() {
                return motionDao.getAllMotionData();
            }

            @Override
            protected List getDataByCache() throws ExecutionException {
                return allMotionCache.get(ALL_DATA);
            }
        }.executor();
    }
}
