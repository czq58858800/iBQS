package com.bq.core.support.login;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author Harvey.Wei
 * @version 2016/8/23
 */
public class NoPwdAuthenticationToken implements AuthenticationToken {

    private String username;

    public NoPwdAuthenticationToken(){}

    public NoPwdAuthenticationToken(String username){
        this.username = username;
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
    }

    public String toString(){
        return "username="+this.username;
    }
}
