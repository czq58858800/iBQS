package com.bq.shuo.model;

import com.bq.shuo.core.push.PushUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by Administrator on 2017/4/17 0017.
 */
public class Push {

    private String loginDevice;

    private String token;

    private String content;

    public Push(String loginDevice,String token,String content) {
        this.loginDevice = loginDevice;
        this.token = token;
        this.content = content;
    }
    public boolean sendMsg() {
        PushUtil push = new PushUtil();
        if (StringUtils.equals("iPhone",loginDevice)) {
            push.sendIOSUnicast(token,content,0,"default");
            return true;
        } else if (StringUtils.equals("Android",loginDevice)) {
            return false;
        }
        return false;
    }

    public String getLoginDevice() {
        return loginDevice;
    }

    public void setLoginDevice(String loginDevice) {
        this.loginDevice = loginDevice;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
