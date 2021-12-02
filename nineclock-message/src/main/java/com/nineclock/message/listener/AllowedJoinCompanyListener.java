package com.nineclock.message.listener;

import com.nineclock.message.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RocketMQMessageListener(topic = "allowedJoinCompanyTopic", consumerGroup = "allowedJoinCompanyConsumer")
public class AllowedJoinCompanyListener implements RocketMQListener<String> {

    @Autowired
    private MessageService messageService;

    @Override
    public void onMessage(String msgId) {

        try {
            log.info("接收到加入团队审核通过的消息，消息id = " + msgId);
            messageService.updateMessageWithApproved(msgId);

        } catch (Exception e) {
            log.error("接收到加入团队审核通过的消息，发生异常：", e);
        }
    }
}