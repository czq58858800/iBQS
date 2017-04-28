package com.bq.shuo.core.util;

import com.baomidou.mybatisplus.plugins.Page;
import com.bq.core.util.InstanceUtil;
import com.bq.shuo.core.base.BaseProvider;
import com.bq.shuo.core.base.Parameter;
import com.bq.shuo.model.Notify;
import com.bq.shuo.model.User;
import com.bq.shuo.provider.IShuoProvider;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.context.ContextLoader;
import push.PushUtil;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/4/17 0017.
 */
public class Push {

    protected final Logger logger = LogManager.getLogger(this.getClass());

    // 线程池
    private ExecutorService executorService = Executors.newCachedThreadPool();


    private String sendUserId;
    private String recevieUserId;
    private String subjectId;
    private String content;
    private String type;    // 1:评论主题 2:发布主题 3:转发主题 4:主题点赞

    public Push(String type, String sendUserId, String recevieUserId) {
        this.type = type;
        this.sendUserId = sendUserId;
        this.recevieUserId = recevieUserId;
        sendMsg();
    }

    public Push(String type, String sendUserId, String recevieUserId, String subjectId, String content) {
        this.type = type;
        this.sendUserId = sendUserId;
        this.recevieUserId = recevieUserId;
        this.subjectId = subjectId;
        this.content = content;
        sendMsg();
    }


    private void sendMsg() {
        IShuoProvider provider = ContextLoader.getCurrentWebApplicationContext().getBean(IShuoProvider.class);
        executorService.submit(new Runnable() {
            public void run() {

                Parameter parameter = new Parameter("userService","queryById").setId(sendUserId);
                User sendUser = (User) provider.execute(parameter).getModel();
                parameter = new Parameter("userService","queryById").setId(recevieUserId);
                User recevieUser = (User) provider.execute(parameter).getModel();

                String pushContent = null;

                // 内容@推送
                if (StringUtils.isNotBlank(recevieUserId) && !StringUtils.equals(sendUserId,recevieUserId)) {
                    if (StringUtils.equals(type,PushType.COMMENTS)) {
                        pushContent = sendUser.getName()+",评论了你的主题。";
                        push(pushContent,recevieUser,PushType.COMMENTS,subjectId);
                    } else if (StringUtils.equals(type,PushType.FORWARD)) {
                        pushContent = sendUser.getName()+",转发了你的主题。";
                        push(pushContent,recevieUser,PushType.FORWARD,recevieUser.getId());
                    } else if (StringUtils.equals(type,PushType.LIKED)) {
                        pushContent = sendUser.getName()+",赞了你的主题。";
                        push(pushContent,recevieUser,PushType.LIKED,subjectId);
                    }
                    Notify notify = new Notify(sendUserId,recevieUserId,subjectId,pushContent,type);
                    parameter = new Parameter("notifyService","update").setModel(notify);
                    provider.execute(parameter);
                }


                if (StringUtils.isNotBlank(content)) {
                    List<String> atUsers = findAtUser(content);
                    for (String userName : atUsers) {
                        Map<String,Object> params = InstanceUtil.newHashMap();
                        params.put("name",userName);
                        parameter = new Parameter("userService","query").setMap(params);
                        Page<User> pageInfo = (Page<User>) provider.execute(parameter).getPage();
                        if (!pageInfo.getRecords().isEmpty()) {
                            User user = pageInfo.getRecords().get(0);
                            //推送@好友
                            if (user != null && sendUserId != user.getId()) {
                                if (StringUtils.equals(type,PushType.COMMENTS)) {
                                    push(sendUser.getName()+",评论中提到了你。",user,PushType.COMMENTS,subjectId);
                                } else if (StringUtils.equals(type,PushType.FORWARD)) {
                                    push(sendUser.getName()+",转发中提到了你。",user,PushType.FORWARD,recevieUser.getId());
                                } else if (StringUtils.equals(type,PushType.SUBJECT)) {
                                    push(sendUser.getName()+",主题中提到了你。",user,PushType.SUBJECT,subjectId);
                                }
                                Notify notify = new Notify(sendUserId,user.getId(),subjectId,content,type);
                                parameter = new Parameter("notifyService","update").setModel(notify);
                                provider.execute(parameter);
                            }
                        }

                    }
                }
            }
        });
    }

    private boolean push(String content,User user,String type,String identify) {
        if (StringUtils.isNotBlank(user.getPushDeviceToken())){
            PushUtil push = new PushUtil("582be60445297d42ad000e71", "dbd5ojrzw01chiouaezolsnpkff5ufva");
            if (StringUtils.equals("iPhone",user.getLoginDevice())) {
                push.sendIOSUnicast(user.getPushDeviceToken(),content,type,identify);
                return true;
            } else if (StringUtils.equals("Android",user.getLoginDevice())) {
                return false;
            }
        }
        return false;
    }

    private static List<String> findAtUser(String str) {
        List<String> arrayList = InstanceUtil.newArrayList();
        if (StringUtils.isNotBlank(str)) {
            String regex = "@(.*?)((?=@)|\\s|$)";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(str);//匹配类
            while (matcher.find()) {
                String val = matcher.group(1).trim();
                if (StringUtils.isNotBlank(val)) {
                    arrayList.add(val);
                }
            }
        }
        return arrayList;
    }

    public String getSendUserId() {
        return sendUserId;
    }

    public void setSendUserId(String sendUserId) {
        this.sendUserId = sendUserId;
    }

    public String getRecevieUserId() {
        return recevieUserId;
    }

    public void setRecevieUserId(String recevieUserId) {
        this.recevieUserId = recevieUserId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
