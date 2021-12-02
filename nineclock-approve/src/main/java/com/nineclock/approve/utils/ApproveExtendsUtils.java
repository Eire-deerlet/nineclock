package com.nineclock.approve.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nineclock.approve.dto.ApproveDefinitionDto;
import com.nineclock.approve.dto.ApproveDefinitionTableDataDto;
import com.nineclock.approve.dto.ColumnObjDto;
import com.nineclock.common.utils.UUIDUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ApproveExtendsUtils {

    /**
     * 扩充参数 : fieldKey
     * @param approveDefinitionDto
     * @return
     */
    public static List<ColumnObjDto> getClolumnListWithFieldKey(ApproveDefinitionDto approveDefinitionDto) {
        List<ColumnObjDto> columns = new ArrayList<>();

        for (ApproveDefinitionTableDataDto tableData : approveDefinitionDto.getTableData()) {
            if(StringUtils.isEmpty(tableData.getFieldKey())){
                tableData.setFieldKey(UUIDUtils.getUUID());
            }

            columns.add(new ColumnObjDto(tableData.getFieldKey(), tableData.getLab(), tableData.getTitle(), "1"));
        }
        return columns;
    }


    /**
     * 扩充流程定义中每一个节点的参数 nodeKey
     * @param jsonArray
     */
    public static void expandParamWithNodeKey(JSONArray jsonArray) {

        for(int i = 0; i < jsonArray.size(); i++){
            JSONObject jsonNode = (JSONObject) jsonArray.get(i);

            String nodeKey = jsonNode.getString("nodeKey");
            if(StringUtils.isEmpty(nodeKey)){
                jsonNode.put("nodeKey", UUIDUtils.getUUID());
            }

            // 处理网关节点的内部节点 nodeKey
            String type = jsonNode.getString("type");
            if("condition".equals(type)){

                JSONArray batchArray = (JSONArray) jsonNode.get("node");
                if(batchArray!=null && batchArray.size()>0){

                    for (int j = 0; j < batchArray.size(); j++) {
                        JSONArray innerArray = (JSONArray) batchArray.get(j);

                        if(innerArray!=null && innerArray.size()>0){
                            for (int k = 0; k < innerArray.size(); k++) {
                                JSONObject node = (JSONObject) innerArray.get(k);
                                if(!node.containsKey("nodeKey")){
                                    node.put("nodeKey", UUIDUtils.getUUID());
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}
