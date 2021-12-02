package com.nineclock.system.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.nineclock.system.service.SysCompanyUserService;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;


/**
 * 解析员工excel监听器
 */
@Slf4j
public class ExcelMemberListener extends AnalysisEventListener<ExcelMember> {

    private List<ExcelMember> memberList = new ArrayList<>();
    private SysCompanyUserService companyUserService;

    public ExcelMemberListener(SysCompanyUserService companyUserService) {
        this.companyUserService = companyUserService;
    }


    @Override
    public void invoke(ExcelMember excelMember, AnalysisContext analysisContext) {
        memberList.add(excelMember);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        log.info("解析员工excel完毕，员工数据 = " + memberList);

        /**
         * 处理解析完毕之后的员工数据, 进行数据组装及持久化操作
         * @param memberList
         */
        companyUserService.handleParsedData(memberList);
    }
}