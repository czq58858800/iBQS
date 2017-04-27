package com.bq.shuo.model.ext;

import com.bq.shuo.model.UserWeibo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/1/16.
 */
public class WeiboInvite implements Serializable {

    private String userId;

    private String thirdpartyId;

    private String openId;

    private String token;

    private List<UserWeibo> invites;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getThirdpartyId() {
        return thirdpartyId;
    }

    public void setThirdpartyId(String thirdpartyId) {
        this.thirdpartyId = thirdpartyId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<UserWeibo> getInvites() {
        return invites;
    }

    public void setInvites(List<UserWeibo> invites) {
        this.invites = invites;
    }
}
