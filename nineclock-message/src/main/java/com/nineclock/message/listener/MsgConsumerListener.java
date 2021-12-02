package com.nineclock.message.listener;

import com.alibaba.fastjson.JSON;
import com.nineclock.message.pojo.NcMessage;
import com.nineclock.message.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 消息监听器
 */
@Component
@RocketMQMessageListener(topic = "messagePushTopic",  consumerGroup = "consumerGroup")
@Slf4j
public class MsgConsumerListener implements RocketMQListener<String> {

    @Autowired
    private MessageService messageService;

    @Override
    public void onMessage(String message) {
        log.info("接受到消息：" + message);

        // 消息json转为对象
        NcMessage ncMessage = JSON.parseObject(message, NcMessage.class);

        try {
            messageService.pushMessage(ncMessage);
        } catch (Exception e) {
            log.error("监听消息出错：" + message + "，错误信息：", e);
        }
    }
}