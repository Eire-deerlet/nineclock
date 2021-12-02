package com.nineclock.message.jpush;

import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class JPushManager {

    @Autowired
    private JPushClient jPushClient;

    /**
     * 推送全局通知
     *
     * @param title   标题
     * @param content 内容
     * @param extras  自定义参数
     * @throws Exception
     */
    public boolean sendNotification(String title, String content, Map<String, String> extras) throws Exception {
        PushPayload pushPayload = PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.all())
                .setNotification(Notification.newBuilder().addPlatformNotification(AndroidNotification.newBuilder().setTitle(title).setAlert(content).addExtras(extras).build()).build())
                .build();
        PushResult pushResult = jPushClient.sendPush(pushPayload);
        log.info("返回信息：" + pushResult.toString());
        return pushResult.statusCode == 0 ? true : false;
    }

    /**
     * 按别名推送通知
     *
     * @param title   标题
     * @param content 内容
     * @param alias   别名列表
     * @param extras  自定义参数
     * @throws Exception
     */
    public boolean sendNotificationByAlias(String title, String content, List<String> alias, Map<String, String> extras) throws Exception {
        PushPayload pushPayload = PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.alias(alias))
                .setNotification(Notification.newBuilder().addPlatformNotification(AndroidNotification.newBuilder().setTitle(title).setAlert(content).addExtras(extras).build()).build())
                .build();
        PushResult pushResult = jPushClient.sendPush(pushPayload);
        log.info("返回信息：" + pushResult.toString());
        return pushResult.statusCode == 0 ? true : false;
    }

    /**
     * 按标签推送通知
     *
     * @param title   标题
     * @param content 内容
     * @param tags    标签列表
     * @param extras  自定义参数
     * @throws Exception
     */
    public boolean sendNotificationByTags(String title, String content, List<String> tags, Map<String, String> extras) throws Exception {
        PushPayload pushPayload = PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.tag_and(tags))
                .setNotification(Notification.newBuilder().addPlatformNotification(AndroidNotification.newBuilder().setTitle(title).setAlert(content).addExtras(extras).build()).build())
                .build();
        PushResult pushResult = jPushClient.sendPush(pushPayload);
        log.info("返回信息：" + pushResult.toString());
        return pushResult.statusCode == 0 ? true : false;
    }
	
    /**
     * 推送自定义消息
     *
     * @param title   标题
     * @param content 内容
     * @param extras  自定义参数
     * @throws Exception
     */
    public boolean sendMessage(String title, String content, Map<String, String> extras) throws Exception {
        PushPayload pushPayload = PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.all())
                .setMessage(Message.newBuilder().setTitle(title).setMsgContent(content).addExtras(extras).build())
                .build();
        PushResult pushResult = jPushClient.sendPush(pushPayload);
        log.info("返回信息：" + pushResult.toString());
        return pushResult.statusCode == 0 ? true : false;
    }
	
    /**
     * 按注册唯一标识推送自定义消息
     * @param title
     * @param content
     * @param registrationIds
     * @param extras
     * @return
     * @throws Exception
     */
    public boolean sendMessageByRegistrationId(String title, String content, List<String> registrationIds, Map<String, String> extras) throws Exception {
        PushPayload pushPayload = PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.registrationId(registrationIds))
                .setMessage(Message.newBuilder().setTitle(title).setMsgContent(content).addExtras(extras).build())
                .build();
        PushResult pushResult = jPushClient.sendPush(pushPayload);
        log.info("返回信息：" + pushResult.toString());
        return pushResult.statusCode == 0 ? true : false;
    }
	
    /**
     * 按别名推送自定义消息
     *
     * @param title   标题
     * @param content 内容
     * @param alias   别名列表
     * @param extras  自定义参数
     * @throws Exception
     */
    public boolean sendMessageByAlias(String title, String content, List<String> alias, Map<String, String> extras) throws Exception {
        PushPayload pushPayload = PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.alias(alias))
                .setMessage(Message.newBuilder().setTitle(title).setMsgContent(content).addExtras(extras).build())
                .build();
        PushResult pushResult = jPushClient.sendPush(pushPayload);
        log.info("返回信息：" + pushResult.toString());
        return pushResult.statusCode == 0 ? true : false;
    }
	
    /**
     * 按标签推送自定义消息
     *
     * @param title   标题
     * @param content 内容
     * @param tags    标签列表
     * @param extras  自定义参数
     * @throws Exception
     */
    public boolean sendMessageByTags(String title, String content, List<String> tags, Map<String, String> extras) throws Exception {
        PushPayload pushPayload = PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.tag_and(tags))
                .setMessage(Message.newBuilder().setTitle(title).setMsgContent(content).addExtras(extras).build())
                .build();
        PushResult pushResult = jPushClient.sendPush(pushPayload);
        log.info("返回信息：" + pushResult.toString());
        return pushResult.statusCode == 0 ? true : false;
    }
}