package com.nineclock.message.service;


import com.nineclock.message.dto.NcMessageDto;
import com.nineclock.message.dto.NcMessageQueryDTO;
import com.nineclock.message.pojo.NcMessage;

import java.util.List;

public interface MessageService {
    /**
     * 推送消息
     * @param message
     */
    void pushMessage(NcMessage message) throws Exception;
    /**
     * 获取推送历史记录
     * @param messageDTO
     * @return
     */
    List<NcMessageDto> findMessageRecord(NcMessageQueryDTO messageDTO);
    /**
     * 更新消息为已读, 审核通过
     * @param msgId
     */
    void updateMessageWithApproved(String msgId);
}
