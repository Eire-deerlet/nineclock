package com.nineclock.attendance.task;

import com.nineclock.attendance.mapper.AttendGroupMapper;
import com.nineclock.attendance.pojo.AttendGroup;
import com.nineclock.attendance.service.AttendPunchService;
import com.nineclock.system.dto.SysCompanyUserDTO;
import com.nineclock.system.feign.SysCompanyUserFeign;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 发送考勤数据邮件定时任务
 */
@Component
@Slf4j
public class SendAttendMailTask {
    @Autowired
    private AttendGroupMapper attendGroupMapper;
    @Autowired
    private SysCompanyUserFeign companyUserFeign;

    @Autowired
    private AttendPunchService attendPunchService;


    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String from;


    @XxlJob("countAttendanceAndSendMail")
    public void countAttendanceAndSendMail() throws Exception {

        log.info("----开始执行定时任务，发送考勤数据邮件给企业管理员----");

        // 1.获取所有设置了考勤组的企业id集合
        Set<Long> companyIds = this.queryAllCompanyIds();

        // 2.遍历所有的企业id，依次生成考勤报表，并发送邮件
        if (!CollectionUtils.isEmpty(companyIds)) {

            for (Long companyId : companyIds) {

                // 2.1 生成企业的考勤报表
                String excelFile = attendPunchService.generatePunchReport(companyId);

                // 2.2 获取企业主管理员信息
                SysCompanyUserDTO mainAdmin = companyUserFeign.queryAdminByCompanyId(companyId).getData();

                if (mainAdmin != null && !StringUtils.isEmpty(mainAdmin.getEmail())) {
                    // 2.3 发送邮件
                    this.sendMail(mainAdmin, excelFile);
                }
            }
        }
    }


    /**
     * 发送附件邮件给企业的主管理员
     * @param userDTO   企业主管理员信息
     * @param excelFile 附件文件名（之前生成好放置在服务端的路径）
     */
    private void sendMail(SysCompanyUserDTO userDTO, String excelFile) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);

        messageHelper.setFrom(from);
        messageHelper.setTo(userDTO.getEmail());
        messageHelper.setSubject("考勤数据报表");
        messageHelper.setText("您好, 本月考勤数据报表已经生成, 请查收附件, 谢谢");

        messageHelper.addAttachment("本月考勤数据.xls", new File(excelFile));

        javaMailSender.send(mimeMessage);

    }

    /**
     * 获取所有设置了考勤组的企业id集合
     * @return
     */
    private Set<Long> queryAllCompanyIds() {
        // 查询考勤组，没有条件
        List<AttendGroup> attendGroupList = attendGroupMapper.selectList(null);

        if (!CollectionUtils.isEmpty(attendGroupList)) {

            // 之所以返回set集合，是因为一个公司可能有多个考勤组
            return attendGroupList.stream().map(attendGroup -> {
                return attendGroup.getCompanyId();
            }).collect(Collectors.toSet());

        }
        return null;
    }
}
