package com.nineclock.message.controller;

import com.nineclock.common.entity.Result;
import com.nineclock.message.dto.NcMessageDto;
import com.nineclock.message.dto.NcMessageQueryDTO;
import com.nineclock.message.service.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/message")
@Api(value = "消息管理接口", tags = "消息服务")
public class MessageController {

    @Autowired
    private MessageService messageService;

    /**
     * 获取推送历史记录
     * @param messageDTO
     * @return
     */
    @ApiOperation(value = "获取推送历史记录")
    @GetMapping
    public Result<List<NcMessageDto>> findMessageRecord(NcMessageQueryDTO messageDTO) {
        List<NcMessageDto> messageRecord = messageService.findMessageRecord(messageDTO);
        return Result.success(messageRecord);
    }
}