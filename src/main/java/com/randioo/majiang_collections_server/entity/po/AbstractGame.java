package com.randioo.majiang_collections_server.entity.po;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.slf4j.Logger;

import com.randioo.randioo_server_base.processor.ICommandStoreable;

public class AbstractGame implements ICommandStoreable {
    public Logger logger;
    private Stack<String> cmdStack = new Stack<>();
    /** 动态数据结构 */
    public Map<String, Object> dataStructure = new HashMap<>();

    @Override
    public Stack<String> getCmdStack() {
        return cmdStack;
    }

    @Override
    public Logger logger() {
        return logger;
    }

}
