package com.nineclock.attendance.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.util.CollectionUtils;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nineclock.attendance.dto.AttendGroupDTO;
import com.nineclock.attendance.dto.AttendPunchDTO;
import com.nineclock.attendance.dto.AttendPunchUserWholeDayDTO;
import com.nineclock.attendance.dto.AttendsDTO;
import com.nineclock.attendance.euums.AttendEnums;
import com.nineclock.attendance.excel.CustomHandler;
import com.nineclock.attendance.mapper.AttendPunchMapper;
import com.nineclock.attendance.pojo.AttendPunch;
import com.nineclock.attendance.service.AttendGroupService;
import com.nineclock.attendance.service.AttendPunchService;
import com.nineclock.common.enums.ResponseEnum;
import com.nineclock.common.exception.NcException;
import com.nineclock.common.filter.CurrentUserHolder;
import com.nineclock.common.utils.BeanHelper;
import com.nineclock.common.utils.DateTimeUtil;
import com.nineclock.common.utils.DistanceUtil;
import com.nineclock.system.dto.SysCompanyUserDTO;
import com.nineclock.system.feign.SysCompanyUserFeign;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class AttendPunchServiceImpl implements AttendPunchService {

    @Autowired
    private AttendPunchMapper attendPunchMapper;

    @Autowired
    private AttendGroupService attendGroupService;

    @Autowired
    private SysCompanyUserFeign companyUserFeign;

    /**
     * 考勤: 移动端打卡
     */
    @Override
    public void punch(AttendPunchDTO attendPunchDTO) {
        // 1.参数校验
        if (attendPunchDTO == null) {
            throw new NcException(ResponseEnum.INVALID_PARAM_ERROR);
        }
        // 2.封装打卡信息
        // 2.1 设置基本信息，员工id、公司id、创建时间
        AttendPunch attendPunch = BeanHelper.copyProperties(attendPunchDTO, AttendPunch.class);
        attendPunch.setCompanyUserId(CurrentUserHolder.get().getCompanyUserId());
        attendPunch.setCompanyId(CurrentUserHolder.get().getCompanyId());
        attendPunch.setCreateTime(new Date());

        // 2.2 设置打卡时间（以服务端为准，否则用户可以在客户端修改手机时间打卡）
        Date now = new Date();
        attendPunch.setPunchTime(now);
        attendPunch.setPunchDateStr(DateTimeUtil.dateToStr(now, DateTimeUtil.TIME_FORMAT_2));
        // 2.3 设置上午、下午打卡（1-上午，2-下午）
        attendPunch.setPunchOnOffWork(DateTimeUtil.noonType(now) + 1);
        // 2.4 查询员工所属的考勤组信息
        // 2.4 查询员工所属的考勤组信息
        AttendGroupDTO attendGroupDTO = attendGroupService.getAttendGroupByUserId();
        if (attendGroupDTO == null) {
            throw new NcException(ResponseEnum.USER_NOT_MATCH_ATTENDGROUP);
        }

        // 2.5 判断是否有效范围内打卡，非有效范围抛出异常
        // boolean punchAreaFlag = this.validatePunchArea(attendPunchDTO, attendGroupDTO);
        boolean punchAreaFlag = true;
        if (!punchAreaFlag) {
            throw new NcException(ResponseEnum.PUNCH_INVALID_AREA);
        }
        attendPunch.setAreaValid(punchAreaFlag);

        // 2.6 判断是否是工作日打卡，非工作日抛出异常
        boolean workDayFlag = this.validateWorkDay(attendGroupDTO);
        if (!workDayFlag) {
            throw new NcException(ResponseEnum.PUNCH_INVALID_DAY);
        }
        attendPunch.setDayType(AttendEnums.DAY_TYPE_WORKDAY.value());
        // 2.7 设置打卡类型（打卡类型 1正常；2迟到；3早退；4旷工）
        this.setPunchType(attendPunch, attendGroupDTO);

        // 2.8 设置是否为有效打卡（上午以最早一次为有效，下午以最晚一次为有效）
        this.validateEffective(attendPunch);

        // 2.9 打卡来源
        attendPunch.setPunchSource(AttendEnums.PUNCH_SOURCE_PUNCH.value());

        attendPunchMapper.insert(attendPunch);
    }

    /**
     * 考勤: 查询打卡数据
     */
    @Override
    public AttendPunchUserWholeDayDTO queryPunchRecord() {
        //创建一个大的集合来存储 上午， 下午的数据
        AttendPunchUserWholeDayDTO wholeDayDTO = new AttendPunchUserWholeDayDTO();
        // 1.获取企业员工ID、企业ID、当前日期字符串
        Long companyUserId = CurrentUserHolder.get().getCompanyUserId();
        Long companyId = CurrentUserHolder.get().getCompanyId();
        //将datetime转换成str
        String nowStr = DateTimeUtil.dateToStr(new Date(), DateTimeUtil.TIME_FORMAT_2);
        // 2.查询上午有效打卡情况
        LambdaQueryWrapper<AttendPunch> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AttendPunch::getCompanyUserId, companyUserId);    //用户ID
        wrapper.eq(AttendPunch::getPunchOnOffWork, AttendEnums.NOON_TYPE_MORNING.value());  //上午或者下午 1为上午 2为下午
        wrapper.eq(AttendPunch::getEffectiveValid, true); //上下午打卡是否为有效打卡
        wrapper.eq(AttendPunch::getPunchDateStr, nowStr);   //打卡日期

        AttendPunch attendPunch = attendPunchMapper.selectOne(wrapper);
        // 3.查询下午有效打卡情况
        LambdaQueryWrapper<AttendPunch> wrapper1 = new LambdaQueryWrapper<>();
        wrapper.eq(AttendPunch::getCompanyUserId, companyUserId);    //用户ID
        wrapper.eq(AttendPunch::getPunchOnOffWork, AttendEnums.NOON_TYPE_AFTERNOON.value());  //上午或者下午 1为上午 2为下午
        wrapper.eq(AttendPunch::getEffectiveValid, true); //上下午打卡是否为有效打卡
        wrapper.eq(AttendPunch::getPunchDateStr, nowStr);   //打卡日期
        AttendPunch attendPunch1 = attendPunchMapper.selectOne(wrapper1);
        // 3.组装数据
        //转成dto 加入wholeDayDTO
        AttendPunchDTO attendPunchDTO = BeanHelper.copyProperties(attendPunch, AttendPunchDTO.class);
        AttendPunchDTO attendPunchDTO2 = BeanHelper.copyProperties(attendPunch1, AttendPunchDTO.class);
        wholeDayDTO.setAttendPunchAfterNoonResponse(attendPunchDTO);
        wholeDayDTO.setAttendPunchAfterNoonResponse(attendPunchDTO2);

        return wholeDayDTO;

    }

    /**
     * 考勤: 查询考勤列表数据
     */
    @Override
    public List<AttendPunchDTO> queryMembers(String startTime, String endTime) {
        // 1.获取起止日期内(包含起止日期)，所有日期的集合
        List<String> spanDateList = DateTimeUtil.getSpanDateList(startTime, endTime);


        // 2.获取当前企业下所有的员工数据列表
        //查询全部的企业员工
        List<SysCompanyUserDTO> companyUserDTOS = companyUserFeign.queryAllCompanyUser().getData();


        if (!CollectionUtils.isEmpty(companyUserDTOS)) {
            //3). 查询每个员工每天的打卡情况
            //	A. 遍历企业员工列表:

            //attendPunchDTOList 包含所有的出勤信息
            List<AttendPunchDTO> attendPunchDTOList = companyUserDTOS.stream().map(companyUserDTO -> {


                //	B. 封装用户基本信息 companyUserDTO -> attendPunchDTO
                AttendPunchDTO attendPunchDTO = BeanHelper.copyProperties(companyUserDTO, AttendPunchDTO.class);

                //	C. 获取该用户的指定时间范围内的有效打卡数据
                LambdaQueryWrapper<AttendPunch> wrapper = new LambdaQueryWrapper<>();
                //企业员工id = //员工ID
                wrapper.eq(AttendPunch::getCompanyUserId, companyUserDTO.getId());
                //打卡日期
                wrapper.between(AttendPunch::getPunchDateStr, startTime, endTime);

                // 3.3.1 包含该员工上午及下午的有效打卡数据（每天的有效打卡数据最多两条）
                wrapper.eq(AttendPunch::getEffectiveValid, true);

                List<AttendPunch> attendPunchList = attendPunchMapper.selectList(wrapper);


                // 3.3.2 将List转为Map<日期，这日期对应的有效打卡数据>，其中key为punchDateStr

                Map<String, List<AttendPunch>> puchMap = attendPunchList.stream()
                        .collect(Collectors.groupingBy(AttendPunch::getPunchDateStr));
                // value打卡状态

                // 3.4 封装打卡数据，每天都生成一个AttendsDTO，返回一个集合
                List<AttendsDTO> attendsList = spanDateList.stream().map(everyDay -> {
                    AttendsDTO attendsDTO = new AttendsDTO();
                    attendsDTO.setAttendDate(everyDay);

                    // 根据日期从上面的map中获取到该天的打卡数据

                    List<AttendPunch> attendPunchListByDay = puchMap.get(everyDay);

                    // 如果不存在，说明当天上午、下午都没打卡，直接设置为缺卡/缺卡
                    if (CollectionUtils.isEmpty(attendPunchListByDay)) {

                        attendsDTO.setAttendStatus("缺卡/缺卡");
                    } else {
                        String attendStatus = this.handleAttendStatus(attendPunchListByDay);
                        attendsDTO.setAttendStatus(attendStatus);
                    }

                    //里层 stream流 返回attendsDTO
                    return attendsDTO;


                }).collect(Collectors.toList());

                //然后将每日打卡信息的集合添加给外层
                attendPunchDTO.setAttendsList(attendsList);

                //外层 stream流 返回 attendPunchDTO
                return attendPunchDTO;

            }).collect(Collectors.toList());


            return attendPunchDTOList;


        }
        return null;
    }

    /**
     * 考勤: 导出考勤列表数据创
     */
    @Override
    public void exportAttendData(String startTime, String endTime) throws IOException {
        // 1.获取考勤列表数据
        List<AttendPunchDTO> punchDTOS = this.queryMembers(startTime, endTime);
        // 2.获取响应对象response，设置文件下载的头信息
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = requestAttributes.getResponse();
        response.setHeader("Content-disposition", "attachment;filename=Attendance.xls");
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.setCharacterEncoding("utf-8");


        //3.通过response获取输出流, 并生成的Excel输出
        EasyExcel.write(response.getOutputStream())
                .excelType(ExcelTypeEnum.XLS)
                .head(this.handleHeaders(punchDTOS))
                .registerWriteHandler(this.horizontalCellStyleStrategy())
                .registerWriteHandler(new CustomHandler())
                .sheet()
                .doWrite(this.handleData(punchDTOS));
    }

    /**
     * 处理表头
     */
    private List<List<String>> handleHeaders(List<AttendPunchDTO> punchDTOS) {
        List<List<String>> headers = new ArrayList<>();

        headers.add(Arrays.asList("姓名"));
        headers.add(Arrays.asList("工号"));
        headers.add(Arrays.asList("部门"));
        headers.add(Arrays.asList("职位"));

        if (!CollectionUtils.isEmpty(punchDTOS)) {
            AttendPunchDTO attendPunchDTO = punchDTOS.get(0); // 取出第一条数据，每行数据都有考勤数据
            List<AttendsDTO> attendsList = attendPunchDTO.getAttendsList();

            if (!CollectionUtils.isEmpty(attendsList)) {
                for (AttendsDTO attendsDTO : attendsList) {
                    headers.add(Arrays.asList(attendsDTO.getAttendDate()));
                }
            }
        }
        return headers;
    }

    /**
     * 处理每行数据
     *
     * @param punchDTOS
     * @return
     */
    private List<List<String>> handleData(List<AttendPunchDTO> punchDTOS) {
        List<List<String>> dataList = new ArrayList<>();

        if (!CollectionUtils.isEmpty(punchDTOS)) {
            for (AttendPunchDTO punchDTO : punchDTOS) {
                // 每一行基本信息
                List<String> rowData = new ArrayList<>();
                rowData.add(punchDTO.getUserName());
                rowData.add(punchDTO.getWorkNumber()); // 工号
                rowData.add(punchDTO.getDepartmentName()); // 部门
                rowData.add(punchDTO.getPost()); // 职位

                // 每一行考勤数据
                List<AttendsDTO> attendsList = punchDTO.getAttendsList();
                if (!CollectionUtils.isEmpty(attendsList)) {
                    for (AttendsDTO attendsDTO : attendsList) {
                        rowData.add(attendsDTO.getAttendStatus());
                    }
                }

                // 将每行数据添加到data中
                dataList.add(rowData);
            }
            return dataList;
        }
        return null;
    }


    /**
     * 设置样式
     */
    public HorizontalCellStyleStrategy horizontalCellStyleStrategy() {

        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        //内容策略 - 水平居中
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);

        //设置字体
        WriteFont font = new WriteFont();
        font.setFontName("阿里巴巴普惠体");
        font.setFontHeightInPoints((short) 11);
        contentWriteCellStyle.setWriteFont(font);
        HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(contentWriteCellStyle, contentWriteCellStyle);

        return horizontalCellStyleStrategy;
    }


    /**
     * 处理打卡状态
     */
    private String handleAttendStatus(List<AttendPunch> punches) {
        String amAttendStatus = "缺卡"; //上午 - 考勤
        String pmAttendStatus = "缺卡"; //下午 - 考勤

        for (AttendPunch punch : punches) {
            if (punch.getPunchOnOffWork() == AttendEnums.NOON_TYPE_MORNING.value()) {
                amAttendStatus = this.getPuchTypeDesc(punch.getMorningPunchType());
            } else if (punch.getPunchOnOffWork() == AttendEnums.NOON_TYPE_AFTERNOON.value()) {
                pmAttendStatus = this.getPuchTypeDesc(punch.getAfternoonPunchType());
            }
        }
        return amAttendStatus + "/" + pmAttendStatus;
    }


    /**
     * 根据打卡类型，获取打卡类型描述
     */
    private String getPuchTypeDesc(Integer punchType) {
        String desc = "";
        switch (punchType) {
            case 1:
                desc = AttendEnums.PUNCH_TYPE_OK.toString();
                break;
            case 2:
                desc = AttendEnums.PUNCH_TYPE_LATE.toString();
                break;
            case 3:
                desc = AttendEnums.PUNCH_TYPE_EARLY.toString();
                break;
            case 4:
                desc = AttendEnums.PUNCH_TYPE_STAYALAY.toString();
                break;
            case 5:
                desc = AttendEnums.PUNCH_TYPE_NOT_WORK_DAY.toString();
                break;
            default:
                break;
        }
        return desc;
    }


    /**
     * 判断打卡类型 : 1正常；2迟到；3早退；4旷工
     */
    private void setPunchType(AttendPunch attendPunch, AttendGroupDTO attendGroupDTO) {
        //获取考勤组中的允许迟到早退分钟数
        Integer allowLateMinutes = attendGroupDTO.getAllowLateMinutes();

        //获取考勤组中的允许旷工分钟数
        Integer lateMinutes = attendGroupDTO.getLateMinutes();

        //获取实际打卡时间
        Date punchTime = attendPunch.getPunchTime();
        //获取实际打卡的时分秒
        String punchTimeStr = DateTimeUtil.dateToStr(punchTime, DateTimeUtil.TIME_FORMAT_1);

        //默认正常
        AttendEnums punchType = AttendEnums.PUNCH_TYPE_OK;

        //判定
        if (attendPunch.getPunchOnOffWork() == AttendEnums.NOON_TYPE_MORNING.value()) { //上午
            //获取考勤组的设置上班时间
            String startWorkTime = attendGroupDTO.getStartWorkTime();

            //判定上班打卡时间
            //返回1 , 说明 : punchTimeStr > startWorkTime + allowLateMinutes
            if (DateTimeUtil.compareTime(punchTimeStr, startWorkTime, allowLateMinutes) == 1) {
                punchType = AttendEnums.PUNCH_TYPE_LATE; // 迟到

                //返回1 , 说明 : punchTimeStr > startWorkTime + lateMinutes
                if (DateTimeUtil.compareTime(punchTimeStr, startWorkTime, lateMinutes) == 1) {
                    punchType = AttendEnums.PUNCH_TYPE_STAYALAY; // 旷工
                }
            }

            attendPunch.setMorningPunchType(punchType.value());

        } else { //下午
            String offWorkTime = attendGroupDTO.getOffWorkTime();

            //判定下班打卡时间
            //返回1 , 说明 : offWorkTime > punchTimeStr + allowLateMinutes
            if (DateTimeUtil.compareTime(offWorkTime, punchTimeStr, allowLateMinutes) == 1) {
                punchType = AttendEnums.PUNCH_TYPE_EARLY; // 早退

                //返回1 , 说明 : offWorkTime > punchTimeStr + lateMinutes
                if (DateTimeUtil.compareTime(offWorkTime, punchTimeStr, lateMinutes) == 1) {
                    punchType = AttendEnums.PUNCH_TYPE_STAYALAY;  // 旷工
                }
            }
            attendPunch.setAfternoonPunchType(punchType.value());
        }
    }

    /**
     * 判断上下午打卡是否有效打卡
     */
    private void validateEffective(AttendPunch attendPunch) {

        //根据条件查询数据库中当前用户的今日上午/下午 打卡记录
        LambdaQueryWrapper<AttendPunch> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AttendPunch::getCompanyUserId, attendPunch.getCompanyUserId()) //打卡用户ID
                .eq(AttendPunch::getPunchDateStr, attendPunch.getPunchDateStr()) //打卡时间
                .eq(AttendPunch::getPunchOnOffWork, attendPunch.getPunchOnOffWork()) //上午 , 下午
                .eq(AttendPunch::getAreaValid, true); //打卡范围有效


        Integer punchCount = attendPunchMapper.selectCount(queryWrapper);
        //如果未获取到, 说明没有打过卡, 此次打卡, 无论是上午还是下午都是有效的
        if (punchCount == 0) {
            attendPunch.setEffectiveValid(true);
        } else {
            //如果获取到记录, 说明已经打过卡
            //如果是上午, 第一条有效, 其余都是无效的
            if (attendPunch.getPunchOnOffWork() == AttendEnums.NOON_TYPE_MORNING.value()) {
                attendPunch.setEffectiveValid(false);

            } else { //如果是下午, 最后一条有效, 其余都是无效的
                attendPunch.setEffectiveValid(true);
                //更新之前的打卡记录为无效, 以最后一次打卡为准
                AttendPunch ap = new AttendPunch();
                ap.setEffectiveValid(false);
                attendPunchMapper.update(ap, queryWrapper);
            }
        }
    }

    /**
     * 校验工作日
     */
    private boolean validateWorkDay(AttendGroupDTO attendGroupDTO) {
        // 使用hutool的工具类，获取今天是周几
        int week = DateUtil.thisDayOfWeek(); //1: 表示周日, 2: 表示周一, 3:表示周二

        List<String> workdays = attendGroupDTO.getWorkdays(); // [1,1,1,1,1,0,0]
        //获取在集合中的下标
        if (week == 1) {
            week = 6;
        } else {
            week = week - 2;
        }
        Integer value = Integer.valueOf(workdays.get(week)); // 获取workdays集合元素
        return value == 1;
    }

    /**
     * 校验打卡范围
     *
     * @param attendPunchDTO
     * @param attendGroupDTO
     * @return
     */
    private boolean validatePunchArea(AttendPunchDTO attendPunchDTO, AttendGroupDTO attendGroupDTO) {
        //比较"打卡坐标" 和 "考勤组打卡坐标"的距离
        Integer meter = DistanceUtil.getDistanceMeter(
                attendPunchDTO.getLat().doubleValue(),
                attendPunchDTO.getLng().doubleValue(),
                attendGroupDTO.getLat().doubleValue(),
                attendGroupDTO.getLng().doubleValue());
        //判定是否小于等于考勤组中设置的范围
        return meter <= attendGroupDTO.getAddressRange();

    }

    /**
     * 生成企业的考勤报表
     */
    @Override
    public String generatePunchReport(Long companyId) {
        // 1.获取当月的第一天、最后一天
        String startTime = DateTimeUtil.getCurrentMonthFirstDay();
        String endTime = DateTimeUtil.getCurrentMonthLastDay();

        // 获取起止日期内(包含起止日期)，所有日期的集合
        List<String> spanDateList = DateTimeUtil.getSpanDateList(startTime, endTime);

        // 2.获取企业的员工列表
        List<SysCompanyUserDTO> companyUserDTOList = companyUserFeign.queryAllUserByCompanyId(companyId).getData();

        // 3.调用公共方法，获取考勤数据
        List<AttendPunchDTO> attendPunchDTOS = getAttendPunchDTOS(startTime, endTime, spanDateList, companyUserDTOList);
        // 4.生成Excel报表
        // 4.1 创建报表放置的服务端文件夹
        String foldername = "D:" + File.separator + "考勤数据" + File.separator + companyId;
        File folder = new File(foldername);

        // 4.2 如果文件夹不存在，创建文件夹
        if(!folder.exists()){
            folder.mkdirs();
        }

        // 4.3 指定文件名，D:/考勤数据/xxx公司-2021-11-考勤数据.xls
        String filename = foldername + File.separator + companyUserDTOList.get(0).getCompanyName() + "-" + startTime.substring(0, 7) + "-考勤数据.xls";

        // 4.4 写文件
        try {
            EasyExcel.write(new File(filename))
                    .excelType(ExcelTypeEnum.XLS)
                    .head(this.handleHeaders(attendPunchDTOS))
                    .registerWriteHandler(this.horizontalCellStyleStrategy())
                    .registerWriteHandler(new CustomHandler())
                    .sheet("考勤报表")
                    .doWrite(this.handleData(attendPunchDTOS));
        }catch (Exception e){
            e.printStackTrace();
        }

        return filename;

    }

    /**
     * 调用公共方法，获取考勤数据
     *
     * @param startTime          开始时间
     * @param endTime            结束时间
     * @param spanDateList       所有日期的集合
     * @param companyUserDTOList 企业的员工列表
     * @return 考勤数据
     */
    private List<AttendPunchDTO> getAttendPunchDTOS(String startTime, String endTime, List<String> spanDateList, List<SysCompanyUserDTO> companyUserDTOList) {


        if (!CollectionUtils.isEmpty(companyUserDTOList)) {
            // 3.查询每个员工每天的打卡情况
            // 3.1 遍历企业员工列表
            List<AttendPunchDTO> attendPunchDTOList = companyUserDTOList.stream().map(companyUserDTO -> {

                // 3.2 封装用户基本信息
                AttendPunchDTO attendPunchDTO = BeanHelper.copyProperties(companyUserDTO, AttendPunchDTO.class);
                // 3.3 获取该用户的指定时间范围内的有效打卡数据
                // 3.3.1 包含该员工上午及下午的有效打卡数据（每天的有效打卡数据最多两条）
                LambdaQueryWrapper<AttendPunch> wrapper = new LambdaQueryWrapper<>();
                //最奇特的一点 要考勤的用户id 等于企业员工表的  员工ID
                wrapper.eq(AttendPunch::getCompanyUserId, companyUserDTO.getId());
                //打卡日期
                wrapper.between(AttendPunch::getPunchDateStr, startTime, endTime);
                wrapper.eq(AttendPunch::getEffectiveValid, true);  //上下午打卡是否为有效打卡
                List<AttendPunch> attendPunchList = attendPunchMapper.selectList(wrapper);
                // 3.3.2 将List转为Map<日期，这日期对应的有效打卡数据>，其中key为punchDateStr
                Map<String, List<AttendPunch>> puchMap = attendPunchList.stream().collect(Collectors.groupingBy(AttendPunch::getPunchDateStr));

                // 3.4 封装打卡数据，遍历spanDateList，将每天都生成一个AttendsDTO，返回一个集合
                List<AttendsDTO> attendsList = spanDateList.stream().map(everyDay -> {


                    AttendsDTO attendsDTO = new AttendsDTO();
                    attendsDTO.setAttendDate(everyDay); //老师添加的
                    // 根据日期从上面的map中获取到该天的打卡数据
                    List<AttendPunch> attendPunchListByDay = puchMap.get(everyDay);
                    // 如果不存在，说明当天上午、下午都没打卡，直接设置为缺卡/缺卡
                    if (CollectionUtils.isEmpty(attendPunchListByDay)) {
                        attendsDTO.setAttendStatus("缺卡/缺卡");
                    } else {
                        String attendStatus = this.handleAttendStatus(attendPunchListByDay);
                        attendsDTO.setAttendStatus(attendStatus);
                    }

                    return attendsDTO;
                }).collect(Collectors.toList());

                attendPunchDTO.setAttendsList(attendsList);

                return attendPunchDTO;


            }).collect(Collectors.toList());
            //考勤数据
            return attendPunchDTOList;

        }
        return null;
    }

}
