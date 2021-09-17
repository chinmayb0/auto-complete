package com.example.AutoComplete.services.impl;

import com.example.AutoComplete.model.CacheNode;
import com.example.AutoComplete.services.CacheService;
import com.example.AutoComplete.services.NodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class CacheServiceImpl implements CacheService {

    private final NodeService nodeService;
    CacheNode head;
    CacheNode tail;
    ConcurrentHashMap<String, CacheNode> cache;
    int size;
    int count;

    CacheServiceImpl(NodeService nodeService) {
        this.initCache();
        this.nodeService = nodeService;
    }

    private void initCache() {
        this.head = new CacheNode();
        this.head.setPre(null);
        this.tail = new CacheNode();
        this.tail.setNext(null);
        this.head.setNext(tail);
        this.tail.setPre(head);
        this.size = 50;
        this.cache = new ConcurrentHashMap<>(50);
    }

    private void addNode(CacheNode node) {
        node.setPre(head);
        node.setNext(head.getNext());
        node.getNext().setPre(node);
        head.setNext(node);
    }

    private void removeNode(CacheNode node) {
        CacheNode pre = node.getPre();
        CacheNode next = node.getNext();
        pre.setNext(next);
        next.setPre(pre);
    }

    private void updateNodePosition(CacheNode node) {
        removeNode(node);
        addNode(node);
    }

    private CacheNode removeNodeFromTail() {
        CacheNode pre = tail.getPre();
        removeNode(pre);
        return pre;
    }

    @Override
    public List<String> getFromCache(String key, int limit) throws Exception {
        CacheNode node = cache.get(key);
        if (node == null) {
            log.debug("Cache MISS : " + key);
            List<String> result = nodeService.autocompleteSuggestions(key.toLowerCase(), limit);
            if (result.size() > 0) {
                updateCache(key, result);
            }
            return result;
        }
        updateNodePosition(node);
        return node.getValue();
    }

    @Override
    public void updateCache(String key, List<String> value) {
        CacheNode temp = cache.get(key);
        if (temp == null) {
            CacheNode node = new CacheNode();
            node.setKey(key);
            node.setValue(value);
            this.cache.put(key, node);
            addNode(node);
            count++;
            if (count > size) {
                CacheNode tail = removeNodeFromTail();
                this.cache.remove(tail.getKey());
                count--;
            }
        } else {
            temp.setValue(value);
            this.updateNodePosition(temp);
        }
    }

    @Override
    public void removeAllCache() {
        cache.clear();
    }

    @Override
    public void removeWordFromCache(String key) {
        if (cache.containsKey(key)) {
            CacheNode node = this.cache.remove(key);
            removeNode(node);
        }
    }
}
