package com.nineclock.message.service.impl;



import com.nineclock.common.utils.BeanHelper;
import com.nineclock.message.dto.NcMessageDto;
import com.nineclock.message.dto.NcMessageQueryDTO;
import com.nineclock.message.jpush.JPushManager;
import com.nineclock.message.pojo.NcMessage;
import com.nineclock.message.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


import java.util.Date;
import java.util.List;

@Service
@Transactional
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MongoTemplate mongoTemplate;
    //极光推送
    @Autowired
    private JPushManager jPushManager;

    @Autowired
    private MessageService messageService;
    /**
     * 推送消息
     * @param message
     */
    @Override
    public void pushMessage(NcMessage message) throws Exception {
        // 消息保存到mongoDB
        message.setCreateTime(new Date());
        //MongoDB  的启动类
        mongoTemplate.save(message);

        // 调用极光推送API
        jPushManager.sendNotificationByAlias(message.getTitle(), message.getContent(),
                message.getTargets(), null);
    }
    /**
     * 获取推送历史记录
     * @param messageDTO
     * @return
     */
    @Override
    public List<NcMessageDto> findMessageRecord(NcMessageQueryDTO messageDTO) {
        // 组装查询条件
        Query query = new Query();
        if (messageDTO != null && !StringUtils.isEmpty(messageDTO.getId())) {
            query.addCriteria(Criteria.where("id").is(messageDTO.getId()));
        }
        if (messageDTO != null && !StringUtils.isEmpty(messageDTO.getType())) {
            query.addCriteria(Criteria.where("messageType").is(messageDTO.getType()));
        }
        if (messageDTO != null && messageDTO.getUserId() != null) {
            query.addCriteria(Criteria.where("applyUserId").is(messageDTO.getUserId()));
        }
        if (messageDTO != null && messageDTO.getTargetId() != null) {
            query.addCriteria(Criteria.where("approveUserId").is(messageDTO.getTargetId()));
        }

        // 根据审核状态升序排序
        query.with(Sort.by(Sort.Order.asc("approveStatue")));

        List<NcMessage> ncMessageList = mongoTemplate.find(query, NcMessage.class);

        return BeanHelper.copyWithCollection(ncMessageList, NcMessageDto.class);
    }
    /**
     * 更新消息为已读, 审核通过
     * @param msgId
     */
    @Override
    public void updateMessageWithApproved(String msgId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(msgId));

        Update update = new Update();
        update.set("useStatus",1);
        update.set("approveStatue",1);

        mongoTemplate.updateFirst(query, update, NcMessage.class);
    }
}
