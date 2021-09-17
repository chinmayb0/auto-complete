package com.example.AutoComplete.services.impl;

import com.example.AutoComplete.services.DataLoadingService;
import com.example.AutoComplete.services.NodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
public class DataLoadingServiceImpl implements DataLoadingService {

    private final NodeService nodeService;

    @Autowired
    DataLoadingServiceImpl(NodeService nodeService) {
        this.nodeService = nodeService;
    }

    @PostConstruct
    public void init() {
        ClassPathResource resource = new ClassPathResource("words.txt");
        InputStream is;
        try {
            is = resource.getInputStream();
            processResource(is);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public void processResource(InputStream is) throws Exception {
        InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(isr);
        for (String line; (line = br.readLine()) != null; ) {
            nodeService.insertWord(line.toLowerCase());
        }
    }
}
