package com.example.AutoComplete.model;

import java.util.List;

public class CacheNode {
    private String key;
    private List<String> value;
    private CacheNode pre;
    private CacheNode next;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<String> getValue() {
        return value;
    }

    public void setValue(List<String> value) {
        this.value = value;
    }

    public CacheNode getPre() {
        return pre;
    }

    public void setPre(CacheNode pre) {
        this.pre = pre;
    }

    public CacheNode getNext() {
        return next;
    }

    public void setNext(CacheNode next) {
        this.next = next;
    }
}
