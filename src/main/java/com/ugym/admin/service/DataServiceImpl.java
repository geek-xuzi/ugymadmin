package com.ugym.admin.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xuzi on 2017/5/10.
 */
@Service
public class DataServiceImpl implements DataService {


    public Map getData() {
        Map<String, String> hashMap = new HashMap();
        hashMap.put("username", "xuzheng");
        hashMap.put("pass", "aaaa");
        return hashMap;
    }
}
