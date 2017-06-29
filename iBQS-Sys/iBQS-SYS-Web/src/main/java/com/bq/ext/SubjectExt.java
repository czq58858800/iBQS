package com.bq.ext;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/29 0029.
 */
public class SubjectExt implements Serializable {
    private String id;
    private Boolean isHot;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getIsHot() {
        return isHot;
    }

    public void setIsHot(Boolean isHot) {
        this.isHot = isHot;
    }
}
