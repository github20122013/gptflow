/*
 * Copyright (C), 2002-2014, 苏宁易购电子商务有限公司
 * FileName: CommonUtil.java
 * Author:   13071494
 * Date:     2014-6-7 下午2:57:30
 * Description: //模块目的、功能描述
 * History: //修改记录
 * <author>      <time>      <version>    <desc>
 * 修改人姓名             修改时间            版本号                  描述
 */
package com.concentrate.gpt.gptflow.util;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * CSV 处理工具
 */
public class CommonUtil {
    private CommonUtil() {
    }
    // CSV处理字符
    /**
     * '\n'
     */
    public static final char LF = '\n';
    /**
     * '\r'
     */
    public static final char CR = '\r';
    /**
     * '"'
     */
    public static final char QUOTE = '"';
    /**
     * ','
     */
    public static final char COMMA = ',';
    /**
     * '\t'
     */
    public static final char TAB = '\t';
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonUtil.class);


    /**
     * 功能描述: <br>
     * 〈CSV字段符号处理〉
     *
     * @param s 待处理的字符串
     * @return 处理后的字符串
     * @author 13071494
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public static String processCsvValue(String s) {
        if (StringUtils.isBlank(s)) {
            return "";
        }
        String value = s.trim();
        // 包含有逗号、双引号、空格、换行符、回车符、空字符则将此字段用双引号引起来，
        // 且原来的双引号变为双双引号
        if (value.indexOf(QUOTE) > -1 || value.indexOf(COMMA) > -1 || value.indexOf(LF) > -1 || value.indexOf(CR) > -1) {
            StringBuilder csvValue = new StringBuilder();
            value = value.replace("\"", "\"\"");
            return csvValue.append(QUOTE).append(value).append(QUOTE).toString();
        } else {
            if (value.matches("^[0-9\\.]{16,}$")) {
                return value + TAB;
            } else {
                return value;
            }
        }
    }


}