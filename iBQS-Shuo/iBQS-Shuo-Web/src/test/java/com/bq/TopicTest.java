package com.bq;

import com.bq.core.util.InstanceUtil;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/5/18 0018.
 */
public class TopicTest {
    public static List<String> findTopic(String str) {
        List<String> topic = InstanceUtil.newArrayList();
        if (StringUtils.isNotBlank(str)) {
            String regex = "#(.*?)#";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(str);//匹配类
            while (matcher.find()) {
                topic.add(matcher.group(1));
            }
        }
        return topic;
    }

    @Test
    public void test() {
        for (String topic:findTopic("#ccjlk# kalsjdf #akd#")) {
            System.out.println(topic);
        }
    }
}
