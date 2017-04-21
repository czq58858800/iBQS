package com.bq.shuo.core.push;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bq.core.util.PropertiesUtil;
import push.AndroidNotification;
import push.PushClient;
import push.android.*;
import push.ios.*;

/**
 * PushUtil
 *
 * @author Harvey.Wei
 * @date 2016/12/1 0001
 */
public class PushUtil {

    private String appkey = null;
    private String appMasterSecret = null;
    private String timestamp = null;
    private PushClient client = new PushClient();

    public PushUtil() {
        try {
            appkey = PropertiesUtil.getString("push.appkey");
            appMasterSecret = PropertiesUtil.getString("push.appMasterSecret");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Android 广播-向安装该App的所有设备发送消息。
     * @param ticker 通知栏提示文字
     * @param title 通知标题
     * @param text 通知文字描述
     * @throws Exception
     */
    public void sendAndroidBroadcast(String ticker,String title,String text){
        try {
            AndroidBroadcast broadcast = new AndroidBroadcast(appkey,appMasterSecret);
            broadcast.setTicker(ticker);
            broadcast.setTitle(title);
            broadcast.setText(text);
            broadcast.goAppAfterOpen();
            broadcast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
            // TODO Set 'production_mode' to 'false' if it's a test device.
            // For how to register a test device, please see the developer doc.
            broadcast.setProductionMode();
            // Set customized fields
            broadcast.setExtraField("test", "helloworld");
            client.send(broadcast);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Android 单播(unicast): 向指定的设备发送消息
     * @param deviceToken 设备的唯一标识
     * @param ticker 通知栏提示文字
     * @param title 通知标题
     * @param text 通知文字描述
     * @throws Exception
     */
    public void sendAndroidUnicast(String deviceToken,String ticker,String title,String text) {
        try {
            AndroidUnicast unicast = new AndroidUnicast(appkey,appMasterSecret);
            // TODO Set your device token
            unicast.setDeviceToken(deviceToken);
            unicast.setTicker(ticker);
            unicast.setTitle(title);
            unicast.setText(text);
            unicast.goAppAfterOpen();
            unicast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
            // TODO Set 'production_mode' to 'false' if it's a test device.
            // For how to register a test device, please see the developer doc.
            unicast.setProductionMode();
            // Set customized fields
//            unicast.setExtraField("test", "helloworld");
            client.send(unicast);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * Android 自定义播(customizedcast): 开发者通过自有的alias进行推送, 可以针对单个或者一批alias进行推送，也可以将alias存放到文件进行发送。
     * @param ticker 通知栏提示文字
     * @param title 通知标题
     * @param text 通知文字描述
     * @throws Exception
     */
    public void sendAndroidCustomizedcast(String ticker,String title,String text) {
        try {
            AndroidCustomizedcast customizedcast = new AndroidCustomizedcast(appkey,appMasterSecret);
            // TODO Set your alias here, and use comma to split them if there are multiple alias.
            // And if you have many alias, you can also upload a file containing these alias, then
            // use file_id to send customized notification.
            customizedcast.setAlias("alias", "alias_type");
            customizedcast.setTicker(ticker);
            customizedcast.setTitle(title);
            customizedcast.setText(text);
            customizedcast.goAppAfterOpen();
            customizedcast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
            // TODO Set 'production_mode' to 'false' if it's a test device.
            // For how to register a test device, please see the developer doc.
            customizedcast.setProductionMode();
            client.send(customizedcast);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 自定义播(customizedcast): 开发者通过自有的alias进行推送, 可以针对单个或者一批alias进行推送，也可以将alias存放到文件进行发送。
     * @param content 用户自定义内容, 可以为字符串或者JSON格式。
     * @param aliasType 当type=customizedcast时，必填，alias的类型,
     * @param ticker 通知栏提示文字
     * @param title 通知标题
     * @param text 通知文字描述
     */
    public void sendAndroidCustomizedcastFile(String content,String aliasType,String ticker,String title,String text) {
        try {
            AndroidCustomizedcast customizedcast = new AndroidCustomizedcast(appkey,appMasterSecret);
            // TODO Set your alias here, and use comma to split them if there are multiple alias.
            // And if you have many alias, you can also upload a file containing these alias, then
            // use file_id to send customized notification.
            String fileId = client.uploadContents(appkey,appMasterSecret,content);
            customizedcast.setFileId(fileId, aliasType);
            customizedcast.setTicker(ticker);
            customizedcast.setTitle(title);
            customizedcast.setText(text);
            customizedcast.goAppAfterOpen();
            customizedcast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
            // TODO Set 'production_mode' to 'false' if it's a test device.
            // For how to register a test device, please see the developer doc.
            customizedcast.setProductionMode();
            client.send(customizedcast);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Android 文件播(filecast): 开发者将批量的device_token或者alias存放到文件, 通过文件ID进行消息发送。
     * @param content 用户自定义内容, 可以为字符串或者JSON格式。
     * @param ticker 通知栏提示文字
     * @param title 通知标题
     * @param text 通知文字描述
     */
    public void sendAndroidFilecast(String content,String ticker,String title,String text) throws Exception {
        try {
            AndroidFilecast filecast = new AndroidFilecast(appkey,appMasterSecret);
            // TODO upload your device tokens, and use '\n' to split them if there are multiple tokens
            String fileId = client.uploadContents(appkey,appMasterSecret,content);
            filecast.setFileId( fileId);
            filecast.setTicker(ticker);
            filecast.setTitle(title);
            filecast.setText(text);
            filecast.goAppAfterOpen();
            filecast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
            client.send(filecast);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 广播(broadcast): 向安装该App的所有设备发送消息。
     * @param alert
     * @param badge
     * @param sound
     * @throws Exception
     */
    public void sendIOSBroadcast(String alert,int badge,String sound) {
        try {
            IOSBroadcast broadcast = new IOSBroadcast(appkey,appMasterSecret);

            broadcast.setAlert(alert);
            broadcast.setBadge(badge);
            broadcast.setSound(sound);
            // TODO set 'production_mode' to 'true' if your app is under production mode
            broadcast.setTestMode();
            // Set customized fields
            //        broadcast.setCustomizedField("test", "helloworld");
            client.send(broadcast);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * IOS 单播(unicast): 向指定的设备发送消息
     * @param deviceToken 设备的唯一标识
     * @param alert 标题
     * @param badge
     * @param sound 提示音
     * @throws Exception
     */
    public void sendIOSUnicast(String deviceToken,String alert,int badge,String sound) {
        try {
            IOSUnicast unicast = new IOSUnicast(appkey,appMasterSecret);
            // TODO Set your device token
            unicast.setDeviceToken(deviceToken);
            unicast.setAlert(alert);
            unicast.setBadge(badge);
            unicast.setSound(sound);
            // TODO set 'production_mode' to 'true' if your app is under production mode
            unicast.setTestMode();
            // Set customized fields
            //        unicast.setCustomizedField("test", "helloworld");
            client.send(unicast);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void sendIOSCustomizedcast() {
        try {
            IOSCustomizedcast customizedcast = new IOSCustomizedcast(appkey,appMasterSecret);
            // TODO Set your alias and alias_type here, and use comma to split them if there are multiple alias.
            // And if you have many alias, you can also upload a file containing these alias, then
            // use file_id to send customized notification.
            customizedcast.setAlias("alias", "alias_type");
            customizedcast.setAlert("IOS 个性化测试");
            customizedcast.setBadge( 0);
            customizedcast.setSound( "default");
            // TODO set 'production_mode' to 'true' if your app is under production mode
            customizedcast.setTestMode();
            client.send(customizedcast);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendIOSFilecast() {
        try {
            IOSFilecast filecast = new IOSFilecast(appkey,appMasterSecret);
            // TODO upload your device tokens, and use '\n' to split them if there are multiple tokens
            String fileId = client.uploadContents(appkey,appMasterSecret,"aa"+"\n"+"bb");
            filecast.setFileId( fileId);
            filecast.setAlert("IOS 文件播测试");
            filecast.setBadge( 0);
            filecast.setSound( "default");
            // TODO set 'production_mode' to 'true' if your app is under production mode
            filecast.setTestMode();
            client.send(filecast);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
