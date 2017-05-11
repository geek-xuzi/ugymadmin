package com.ugym.admin.bean;

import java.io.Serializable;

/**
 * @author zheng.xu
 * @since 2017-05-11
 */
public class Motion implements Serializable {

    private int uid;
    private long sum_duration;
    private long sum_distance;
    private String nickname;
    private String avatar;

    public int getUid() {
        return uid;
    }

    public long getSum_distance() {
        return sum_distance;
    }

    public long getSum_duration() {
        return sum_duration;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getNickname() {
        return nickname;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setSum_distance(long sum_distance) {
        this.sum_distance = sum_distance;
    }

    public void setSum_duration(long sum_duration) {
        this.sum_duration = sum_duration;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

}
