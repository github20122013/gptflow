package com.concentrate.gpt.gptflow.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


/**
 * SESSION 常用方法类
 */
public class SessionUtil {

    public static final String DEBUG_KEY = "debug";
    public static final String DEBUG_ON = "true";
    public static HttpSession getSession() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attr.getRequest();
        return request.getSession();
    }

    public static void setSessionAttribute(String key, Object value) {
        HttpSession session = getSession();
        session.setAttribute(key, value);
    }

    public static Object getSessionAttribute(String key) {
        HttpSession session = getSession();
        return session.getAttribute(key);
    }

    public static boolean debugOn(){
        return DEBUG_ON.equals(getSessionAttribute(DEBUG_KEY));
    }

}
