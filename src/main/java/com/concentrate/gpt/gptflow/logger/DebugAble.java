package com.concentrate.gpt.gptflow.logger;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class DebugAble {

    /**
     * 通过MAP透传的日志，用__debug作为key
     */
    public static final String DEBUG_KEY_IN_MAP = "__debug";

    /**
     * 通过对象透传的日志，用__debug作为key
     */
    public CopyOnWriteArrayList __debug = new CopyOnWriteArrayList();

}
