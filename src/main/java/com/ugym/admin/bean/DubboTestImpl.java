package com.ugym.admin.bean;

import org.springframework.stereotype.Service;

/**
 * @author zheng.xu
 * @since 2017-06-08
 */
@Service("dubbotest")
public class DubboTestImpl implements DubboTest {

    @Override
    public String run() {
        System.out.println("aaaaaaaaaaaaa");
        return "ok";
    }
}
