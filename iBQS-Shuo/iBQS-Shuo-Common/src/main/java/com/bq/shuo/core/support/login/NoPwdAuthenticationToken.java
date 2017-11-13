package com.bq.shuo.core.support.login;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.HostAuthenticationToken;
import org.apache.shiro.authc.RememberMeAuthenticationToken;

/**
 * @author chern.zq
 * @version 2016/8/23
 */
public class NoPwdAuthenticationToken implements HostAuthenticationToken, RememberMeAuthenticationToken {

    private String username;
    private boolean rememberMe;
    private String host;

    public NoPwdAuthenticationToken(){}

    public NoPwdAuthenticationToken(String username){
        this.username = username;
    }

    public NoPwdAuthenticationToken(String username, String host) {
        this(username, false, host);
    }

    public NoPwdAuthenticationToken(String username, boolean rememberMe) {
        this(username, rememberMe, (String)null);
    }

    public NoPwdAuthenticationToken(String username, boolean rememberMe, String host) {
        this.rememberMe = false;
        this.username = username;
        this.rememberMe = rememberMe;
        this.host = host;
    }

    @Override
    public Object getPrincipal() {
        return this.username;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void clear(){
        this.username = null;
        this.host = null;
        this.rememberMe = false;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(this.getClass().getName());
        sb.append(" - ");
        sb.append(this.username);
        sb.append(", rememberMe=").append(this.rememberMe);
        if(this.host != null) {
            sb.append(" (").append(this.host).append(")");
        }

        return sb.toString();
    }

    public boolean isRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
