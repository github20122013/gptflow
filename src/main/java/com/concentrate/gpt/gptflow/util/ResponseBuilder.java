package com.concentrate.gpt.gptflow.util;

import com.alibaba.fastjson.JSON;
import com.concentrate.gpt.gptflow.vo.ResponseResult;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResponseBuilder {

	public final static String OK = "ok";
	
	public final static String ERROR = "error";
	
	public static ResponseResult ok(String msg) {
		return new ResponseResult(OK, msg);
	}
	
	public static ResponseResult error(String msg) {
		return new ResponseResult(ERROR, msg);
	}
	
	public static ResponseResult ok(Object obj) {
		return new ResponseResult(OK, obj);
	}
	
	public static ResponseResult error(Object obj) {
		return new ResponseResult(ERROR, obj);
	}

	/*
	 * 返回json 结果
	 */
	public static void resultObjectJson(HttpServletResponse response, Object object, HttpServletRequest request) {
		response.setHeader("Content-Type", "application/javascript;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		StringBuilder sb = new StringBuilder();
		sb.append(JSON.toJSONString(object));
		String callback = request.getParameter("callback");
		if (StringUtils.isNotEmpty(callback)) {
			sb.insert(0, callback + "(");
			sb.append(");");
		}
		try {
			response.getWriter().write(sb.toString());
		} catch (IOException e) {
			throw new RuntimeException("HelpUtil resultObjectJson throw exception", e);
		}
	}
}
