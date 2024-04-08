package com.concentrate.gpt.gptflow.logger;

import com.concentrate.gpt.gptflow.util.SessionUtil;
import org.slf4j.Logger;
import org.slf4j.Marker;

import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class DebugAbleLogger implements Logger {

    protected Logger inner;

    public DebugAbleLogger(Logger outer){
        this.inner = outer;
    }

    @Override
    public String getName() {
        return inner.getName();
    }

    @Override
    public boolean isTraceEnabled() {
        return inner.isTraceEnabled();
    }

    @Override
    public void trace(String s) {
        inner.trace(s);
    }

    @Override
    public void trace(String s, Object o) {
        inner.trace(s,o);
    }

    @Override
    public void trace(String s, Object o, Object o1) {
        inner.trace(s,o,o1);
    }

    @Override
    public void trace(String s, Object... objects) {
        inner.trace(s,objects);
    }

    @Override
    public void trace(String s, Throwable throwable) {
        inner.trace(s,throwable);
    }

    @Override
    public boolean isTraceEnabled(Marker marker) {
        return inner.isTraceEnabled(marker);
    }

    @Override
    public void trace(Marker marker, String s) {
        inner.trace(marker,s);
    }

    @Override
    public void trace(Marker marker, String s, Object o) {
        inner.trace(marker,s,o);
    }

    @Override
    public void trace(Marker marker, String s, Object o, Object o1) {
        inner.trace(marker,s,o,o1);
    }

    @Override
    public void trace(Marker marker, String s, Object... objects) {
        inner.trace(marker,s,objects);
    }

    @Override
    public void trace(Marker marker, String s, Throwable throwable) {
        inner.trace(marker,s,throwable);
    }

    @Override
    public boolean isDebugEnabled() {
        return inner.isDebugEnabled();
    }

    @Override
    public void debug(String s) {
        inner.debug(s);
    }

    @Override
    public void debug(String s, Object o) {
        inner.debug(s,o);
    }

    @Override
    public void debug(String s, Object o, Object o1) {
        inner.debug(s,o,o1);
    }

    @Override
    public void debug(String s, Object... objects) {
        inner.debug(s,objects);
    }

    @Override
    public void debug(String s, Throwable throwable) {
        inner.debug(s,throwable);
    }

    @Override
    public boolean isDebugEnabled(Marker marker) {
        return inner.isDebugEnabled(marker);
    }

    @Override
    public void debug(Marker marker, String s) {
        inner.debug(marker,s);
    }

    @Override
    public void debug(Marker marker, String s, Object o) {
        inner.debug(marker,s,o);
    }

    @Override
    public void debug(Marker marker, String s, Object o, Object o1) {
        inner.debug(marker,s,o,o1);
    }

    @Override
    public void debug(Marker marker, String s, Object... objects) {
        inner.debug(marker,s,objects);
    }

    @Override
    public void debug(Marker marker, String s, Throwable throwable) {
        inner.debug(marker,s,throwable);
    }

    @Override
    public boolean isInfoEnabled() {
        return inner.isInfoEnabled();
    }

    @Override
    public void info(String s) {
        inner.info(s);
    }

    @Override
    public void info(String s, Object o) {
        inner.info(s,o);
    }

    @Override
    public void info(String s, Object o, Object o1) {
        inner.info(s,o,o1);
    }

    @Override
    public void info(String s, Object... objects) {
        inner.info(s,objects);
    }

    @Override
    public void info(String s, Throwable throwable) {
        inner.info(s,throwable);
    }

    @Override
    public boolean isInfoEnabled(Marker marker) {
        return inner.isInfoEnabled(marker);
    }

    @Override
    public void info(Marker marker, String s) {
        inner.info(marker,s);
    }

    @Override
    public void info(Marker marker, String s, Object o) {
        inner.info(marker,s,o);
    }

    @Override
    public void info(Marker marker, String s, Object o, Object o1) {
        inner.info(marker,s,o,o1);
    }

    @Override
    public void info(Marker marker, String s, Object... objects) {
        inner.info(marker,s,objects);
    }

    @Override
    public void info(Marker marker, String s, Throwable throwable) {
        inner.info(marker,s,throwable);
    }

    @Override
    public boolean isWarnEnabled() {
        return inner.isWarnEnabled();
    }

    @Override
    public void warn(String s) {
        inner.warn(s);
    }

    @Override
    public void warn(String s, Object o) {
        inner.warn(s,o);
    }

    @Override
    public void warn(String s, Object... objects) {
        inner.warn(s,objects);
    }

    @Override
    public void warn(String s, Object o, Object o1) {
        inner.warn(s,o,o1);
    }

    @Override
    public void warn(String s, Throwable throwable) {
        inner.warn(s,throwable);
    }

    @Override
    public boolean isWarnEnabled(Marker marker) {
        return inner.isWarnEnabled(marker);
    }

    @Override
    public void warn(Marker marker, String s) {
        inner.warn(marker,s);
    }

    @Override
    public void warn(Marker marker, String s, Object o) {
        inner.warn(marker,s,o);
    }

    @Override
    public void warn(Marker marker, String s, Object o, Object o1) {
        inner.warn(marker,s,o,o1);
    }

    @Override
    public void warn(Marker marker, String s, Object... objects) {
        inner.warn(marker,s,objects);
    }

    @Override
    public void warn(Marker marker, String s, Throwable throwable) {
        inner.warn(marker,s,throwable);
    }

    @Override
    public boolean isErrorEnabled() {
        return inner.isErrorEnabled();
    }

    @Override
    public void error(String s) {
        inner.error(s);
    }

    @Override
    public void error(String s, Object o) {
        inner.error(s,o);
    }

    @Override
    public void error(String s, Object o, Object o1) {
        inner.error(s,o,o1);
    }

    @Override
    public void error(String s, Object... objects) {
        inner.error(s,objects);
    }

    @Override
    public void error(String s, Throwable throwable) {
        inner.error(s,throwable);
    }

    @Override
    public boolean isErrorEnabled(Marker marker) {
        return inner.isErrorEnabled(marker);
    }

    @Override
    public void error(Marker marker, String s) {
        inner.error(marker,s);
    }

    @Override
    public void error(Marker marker, String s, Object o) {
        inner.error(marker,s,o);
    }

    @Override
    public void error(Marker marker, String s, Object o, Object o1) {
        inner.error(marker,s,o,o1);
    }

    @Override
    public void error(Marker marker, String s, Object... objects) {
        inner.error(marker,s,objects);
    }

    @Override
    public void error(Marker marker, String s, Throwable throwable) {
        inner.error(marker,s,throwable);
    }

    /**
     * 解析日志内容,put到map的__debug key中
     *
     * @param map
     * @param info
     * @param o
     */
    public void info(Map map, String info,Object o){
        if(SessionUtil.debugOn()){
            // 将解析后的日志内容放入Map中的__debug key中
            ensureDebugField(map);
            ((CopyOnWriteArrayList)map.get(DebugAble.DEBUG_KEY_IN_MAP)).add(String.format(info,o));
        }
    }

    public void ensureDebugField(Map map) {
        if(!map.containsKey(DebugAble.DEBUG_KEY_IN_MAP)){
            map.put(DebugAble.DEBUG_KEY_IN_MAP,new CopyOnWriteArrayList<Object>());
        }
    }

    /**
     * 解析日志内容,put到map的__debug key中，如何通过info 和 o 解析logger的内容
     *
     * @param debugAble
     * @param info
     * @param o
     */
    public void info(DebugAble debugAble, String info,Object o){
        if(SessionUtil.debugOn()){
            // 将解析后的日志内容放入Map中的__debug key中
            debugAble.__debug.add(String.format("[INFO]:"+info,o));
        }
        inner.info(info,o);
    }


    /**
     * 解析日志内容,put到map的__debug key中
     *
     * @param map
     * @param info
     * @param o
     */
    public void error(Map map, String info,Object o){
        if(SessionUtil.debugOn()){
            // 将解析后的日志内容放入Map中的__debug key中
            ensureDebugField(map);
            ((CopyOnWriteArrayList)map.get(DebugAble.DEBUG_KEY_IN_MAP)).add(String.format(info,o));
        }
        inner.error(info,o);
    }


    /**
     * 解析日志内容,put到map的__debug key中，如何通过info 和 o 解析logger的内容
     *
     * @param debugAble
     * @param info
     * @param o
     */
    public void error(DebugAble debugAble, String info,Object o){
        if(SessionUtil.debugOn()){
            // 将解析后的日志内容放入Map中的__debug key中
            debugAble.__debug.add(String.format("[ERROR]:"+info,o));
        }
    }

}
