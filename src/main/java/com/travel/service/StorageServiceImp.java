package com.travel.service;

import com.travel.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

@Service
public class StorageServiceImp implements StorageService{
    private final Path rootLocation;

    @Autowired
    public StorageServiceImp ()
    {
        this.rootLocation = Paths.get("upload-dir");
    }

    @Override
    public void init() {

    }

    @Override
    public Path store(MultipartFile file) {
         String fileName = StringUtils.cleanPath(file.getOriginalFilename());
         try {
             if (file.isEmpty())
             {
                 throw new BadRequestException(fileName);
             }
             if (fileName.contains(".."))
             {
                 throw new BadRequestException(fileName);
             }
             Files.copy(file.getInputStream(),this.rootLocation.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
         }
           catch (IOException e) {
             throw new BadRequestException(fileName,e);
         }
         return this.rootLocation.resolve(fileName);
    }

    @Override
    public Stream<Path> loadAll() {
        return null;
    }

    @Override
    public Path load(String fileName) {
        return null;
    }

    @Override
    public Resource loadAsResource(String fileName) {
        return null;
    }

    @Override
    public void deleteAll() {

    }
}
