package com.example.AutoComplete.model;

import java.util.HashMap;
import java.util.Map;

public class DataNode {
    private char data;
    private Map<Character, DataNode> children;
    private boolean isEnd;

    public DataNode() {
        children = new HashMap<>();
    }

    public DataNode(char letter) {
        this();
        data = letter;
    }

    public char getData() {
        return data;
    }

    public void setData(char data) {
        this.data = data;
    }

    public Map<Character, DataNode> getChildren() {
        return children;
    }

    public void setChildren(Map<Character, DataNode> children) {
        this.children = children;
    }

    public boolean isEnd() {
        return isEnd;
    }

    public void setEnd(boolean end) {
        isEnd = end;
    }
}
