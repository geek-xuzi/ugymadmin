package com.ugym.admin.service;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import com.github.pagehelper.PageHelper;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import com.ugym.admin.bean.CacheKey;
import com.ugym.admin.bean.Motion;
import com.ugym.admin.bean.OrderData;
import com.ugym.admin.common.ResultTemplate;
import com.ugym.admin.dao.MotionDao;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import org.apache.commons.io.IOUtils;
import org.apache.tools.ant.util.ReaderInputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * Created by xuzi on 2017/5/10.
 */
@Service
public class DataServiceImpl implements DataService {

    private static final long EXPIRE = 30000;
    public static final String ALL_DATA = "XUEN";
    private static final int NUM = 500;
    private static final int THREAD_POOL_NUM = 30;


    @Autowired
    private MotionDao motionDao;

    @Resource(name = "jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

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

    String filePath = "tmp/test.csv";
    File file = new File(filePath);
    File dir = new File("tmp");

    @Override
    public List<OrderData> getAllData() throws InterruptedException {
        int count = motionDao.getCount();
        int taskNum = count % NUM == 0 ? (count / NUM) : (count / NUM + 1);
        List<OrderData> allData = Lists.newArrayList();
        CountDownLatch countDownLatch = new CountDownLatch(taskNum);
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(THREAD_POOL_NUM);
        for (int i = 0; i < taskNum; i++) {
            int j = i;
            fixedThreadPool.execute(() -> {
                List<OrderData> dataList = motionDao.getData(j * NUM, NUM);
                allData.addAll(dataList);
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        //        List<OrderData> datas = motionDao.getAllData();
        createCSVFile(allData);
        // 上传到hdfs

        // 导入到hive

        return allData;
    }


    private void createCSVFile(List<OrderData> orderDataList) {

        try {
            if (!dir.exists()) {
                dir.mkdirs();
            }
            file.createNewFile();
            // 创建CSV写对象
            CsvWriter csvWriter = new CsvWriter(filePath, ',', Charset.forName("UTF-8"));
            orderDataList.stream().forEach(item -> {
                String[] s = apdaterStr(item);
                try {
                    csvWriter.writeRecord(s);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String[] apdaterStr(OrderData item) {
        String[] s = new String[]{item.getOrder_id().toString(),
                "order-product:" + item.getOrder_product() + "," +
                        "main-order:" + item.getMain_order(),
                item.getCreate_date()};
        return s;
    }

    public static void main(String[] args) {
//        String filePath = "tmp/test.csv";
//        try {
//            File file = new File(filePath);
//            File dir = new File("tmp");
//
//            if (!dir.exists()) {
//                dir.mkdirs();
//            }
//
//            file.createNewFile();
//            // 创建CSV写对象
//            CsvWriter csvWriter = new CsvWriter(filePath, ',', Charset.forName("UTF-8"));
//            //CsvWriter csvWriter = new CsvWriter(filePath);
//            // 写表头
//            String[] headers = {"编号", "姓名", "年龄"};
//            String[] content = {"12365", "张", "35"};
//            csvWriter.writeRecord(headers);
//            csvWriter.writeRecord(content);
//            csvWriter.close();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
