package com.jme.lsgoldtrade.domain;

import java.io.Serializable;

public class OnlineTimeVo implements Serializable {

    /**
     *  "id": "1",
     *  "timeName": "10分钟",
     *  "timeSeconds": 600
     */

    private String id;

    private String timeName;

    private long timeSeconds;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTimeName() {
        return timeName;
    }

    public void setTimeName(String timeName) {
        this.timeName = timeName;
    }

    public long getTimeSeconds() {
        return timeSeconds;
    }

    public void setTimeSeconds(long timeSeconds) {
        this.timeSeconds = timeSeconds;
    }
}
