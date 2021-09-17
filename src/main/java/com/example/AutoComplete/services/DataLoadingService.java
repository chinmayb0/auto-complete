package com.example.AutoComplete.services;

import java.io.InputStream;

public interface DataLoadingService {
    void processResource(InputStream inputStream) throws Exception;
}
