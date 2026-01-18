package br.com.jovirds.service;

import br.com.jovirds.config.FileStorageConfig;
import br.com.jovirds.exception.FileNotFoundException;
import br.com.jovirds.exception.FileStorageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileService {

    private static final Logger logger = LoggerFactory.getLogger(FileService.class);

    private final Path fileStorageLocation;

    @Autowired
    public FileService(FileStorageConfig fileStorageConfig) {
        Path path = Paths.get(fileStorageConfig.getUpload_dir())
                .toAbsolutePath().toAbsolutePath().normalize();
        this.fileStorageLocation = path;

        try {
            logger.error("Creating Directories");
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception e) {
            logger.error("Could not create the directory where files will be stored");
            throw new FileStorageException("Could not create the directory where files will be stored", e);
        }
    }

    public String storeFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry, Filename Contains a Invalid path Sequence " + fileName);
            }

            logger.error("Saving Files");

            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (Exception e) {
            logger.error("Could not store file. Please try Again");
            throw new FileStorageException("Could not store file. Please try Again");
        }

    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                return  resource;
            }

            logger.error("file not found");
            throw new FileNotFoundException("File not found" + fileName);
        } catch (Exception e) {
            logger.error("File not found");
            throw new FileNotFoundException("File not found" + fileName, e);
        }
    }

}
