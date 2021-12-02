package com.nineclock.common.exception;

import com.nineclock.common.enums.ResponseEnum;
import lombok.Data;

@Data
//NcException 继承 RuntimeException
public class NcException extends RuntimeException {
    //创建一个错误代码值
    private Integer code;

    public NcException(ResponseEnum responseEnum) {
        //responseEnum得到的错误代码信息，给到父类
        super(responseEnum.getMessage());

        //responseEnum的错误代码，赋值给变量code
        this.code = responseEnum.getCode();
    }

}
