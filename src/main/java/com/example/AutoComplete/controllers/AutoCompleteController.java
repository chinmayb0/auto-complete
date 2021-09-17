package com.example.AutoComplete.controllers;

import com.example.AutoComplete.services.CacheService;
import com.example.AutoComplete.services.DataLoadingService;
import com.example.AutoComplete.services.NodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Slf4j
@RestController
public class AutoCompleteController {
    private final CacheService cacheService;
    private final NodeService nodeService;
    private final DataLoadingService dataLoadingService;

    @Autowired
    AutoCompleteController(CacheService cacheService, NodeService nodeService, DataLoadingService dataLoadingService) {
        this.cacheService = cacheService;
        this.nodeService = nodeService;
        this.dataLoadingService = dataLoadingService;
    }

    @GetMapping("/suggestions/{inputWord}")
    public List<String> processInput(HttpServletRequest request, HttpServletResponse response,
                                     @PathVariable String inputWord,
                                     @RequestParam(value = "limit", required = false, defaultValue = "50") int limit)
            throws IOException {
        if (!validateInputLimit(limit)) {
            log.error("invalid limit value : " + limit);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "invalid limit value");
        }
        List<String> result = null;
        try {
            result = cacheService.getFromCache(inputWord.toLowerCase(), limit);
            response.setStatus(200);
        } catch (Exception ex) {
            log.error("Failed to generate Suggestions for input : " + inputWord, ex);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to generate Suggestions for input : " + inputWord);
        }
        return result;
    }

    @GetMapping("/insertWord/{word}")
    public void insertWord(HttpServletRequest request, HttpServletResponse response, @PathVariable String word)
            throws IOException {
        try {
            nodeService.insertWord(word);
        } catch (Exception ex) {
            log.error("Unable to insert Word" + word, ex);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to insert Word");
        }
    }

    @PostMapping(path = "/uploadFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void processFile(HttpServletRequest request, HttpServletResponse response,
                            @RequestPart("file") MultipartFile file) throws IOException {
        try {
            InputStream is = file.getInputStream();
            dataLoadingService.processResource(is);
        } catch (Exception ex) {
            log.error("Failed to process input File", ex);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to process input File");
        }
    }

    @PutMapping("/purgeCache")
    public void removeAllCache(HttpServletRequest request, HttpServletResponse response,
                               @RequestHeader(value = "Authorization", required = true) String token,
                               @RequestParam(value = "word", required = false, defaultValue = "") String word)
            throws IOException {
        //TODO: validate token to Authenticate access
        try {
            if (!StringUtils.hasText(word)) {
                cacheService.removeAllCache();
            } else {
                cacheService.removeWordFromCache(word);
            }
        } catch (Exception ex) {
            log.error("Unable to remove from cache", ex);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to remove from cache");
        }
    }

    private boolean validateInputLimit(int limit) {
        return limit > 0;
    }
}
