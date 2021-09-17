package com.example.AutoComplete.services.impl;

import com.example.AutoComplete.model.DataNode;
import com.example.AutoComplete.services.NodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class NodeServiceImpl implements NodeService {
    final DataNode root;
    int limit = 50;

    public NodeServiceImpl() {
        root = new DataNode();
    }

    @Override
    public List<String> autocompleteSuggestions(String input, int limit) {
        this.limit = limit;
        List<String> result = new ArrayList<>();
        DataNode current = root;
        StringBuffer sb = new StringBuffer();
        for (char c : input.toCharArray()) {
            current = current.getChildren().get(c);
            if (current == null) {
                return result;
            }
            sb.append(c);
        }
        generateSuggestions(current, result, sb, limit);
        return result;
    }

    private void generateSuggestions(DataNode current, List<String> result, StringBuffer sb, int limit) {
        if (current.isEnd()) {
            result.add(sb.toString());
        }
        if (result.size() == limit) {
            return;
        }
        if (current.getChildren().isEmpty()) {
            return;
        }
        for (DataNode child : current.getChildren().values()) {
            if (result.size() ==  limit) {
                break;
            }
            generateSuggestions(child, result, sb.append(child.getData()), limit);
            sb.setLength(sb.length() - 1);
        }
    }

    @Override
    public void insertWord(String word) {
        if (!StringUtils.hasText(word)) {
            return;
        }

        DataNode current = root;
        for (char c : word.toCharArray()) {
            if (current.getChildren().get(c) == null) {
                current.getChildren().put(c, new DataNode(c));
            }
            current = current.getChildren().get(c);
        }
        current.setEnd(true);
    }
}
