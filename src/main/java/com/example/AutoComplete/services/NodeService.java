package com.example.AutoComplete.services;

import java.util.List;

public interface NodeService {

    List<String> autocompleteSuggestions(String input, int limit) throws Exception;

    void insertWord(String word) throws Exception;

}
