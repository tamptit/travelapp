package com.travel.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.nio.file.Path;
import java.util.stream.Stream;


public interface StorageService {

    void init();
    Path store (MultipartFile file);

    Stream<Path> loadAll();

    Path load(String fileName);

    Resource loadAsResource (String fileName);

    void deleteAll();
}
