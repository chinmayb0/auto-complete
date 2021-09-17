package com.example.AutoComplete.services;

import java.util.List;

public interface CacheService {

    List<String> getFromCache(String key, int limit) throws Exception;

    void updateCache(String key, List<String> value);

    void removeAllCache();

    void removeWordFromCache(String key);
}
