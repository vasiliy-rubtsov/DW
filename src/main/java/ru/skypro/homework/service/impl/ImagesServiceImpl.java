package ru.skypro.homework.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.FileTypeEnum;
import ru.skypro.homework.service.ImagesService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
public class ImagesServiceImpl implements ImagesService {
    @Value("${ads.image-file-dir}")
    private String imageFileDir;

    private final Logger logger = LoggerFactory.getLogger(ImagesServiceImpl.class);

    /**
     *
     * @param file MultipartFile
     * @param targetFileName String
     * @throws IOException
     */
    @Override
    public void saveFileToDisk(MultipartFile file, String targetFileName) throws IOException {
        Path outputFilePath = Path.of(imageFileDir, targetFileName);
        Files.createDirectories(outputFilePath.getParent());
        Files.deleteIfExists(outputFilePath);
        try (
                OutputStream os = Files.newOutputStream(outputFilePath, CREATE_NEW);
                BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
        ) {
            bos.write(file.getBytes());
        }
    }

    /**
     *
     * @param fileName String
     * @return byte[]
     * @throws IOException
     */
    @Override
    public byte[] loadFileByName(String fileName) throws IOException {
        Path filePath = Path.of(imageFileDir, fileName);
        return Files.readAllBytes(filePath);
    }

    /**
     *
     * @param originalFilename String
     * @param pk long
     * @param fileType FileTypeEnum
     * @return String
     */
    @Override
    public String makeTargetFileName(String originalFilename, long pk, FileTypeEnum fileType) {
        String fileNameExtension = "";
        int index = originalFilename.lastIndexOf(".");
        if (index >= 0) {
            fileNameExtension = "." + originalFilename.substring(index + 1);
        }

        return (fileType == FileTypeEnum.PRODUCT ? "product-" : "avatar-") + pk + fileNameExtension;
    }
}
