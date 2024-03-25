package com.concentrate.gpt.gptflow.define;

public enum FeatureEnum {
    //不允许为空
    NOT_NULL,
    //唯一性校验
    UNIQ,
    //开始时间
    START_TIME,
    //结束时间
    END_TIME,
    //日期类型
    TIME,
    //只读
    READONLY,
    //多值查询
    MULTIVAL
}
