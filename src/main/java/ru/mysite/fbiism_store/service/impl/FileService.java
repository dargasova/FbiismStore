package ru.mysite.fbiism_store.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.mysite.fbiism_store.service.IFileService;

import java.io.File;
import java.io.IOException;

@Service
public class FileService implements IFileService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public String saveFile(MultipartFile file, Long productId, String color, long imageCount) throws IOException {
        String fileName = String.format("product%d_color%s_image%d.png", productId, color.toLowerCase(), imageCount);
        File destinationFile = new File(uploadDir + fileName);
        file.transferTo(destinationFile);
        return "/uploads/images/" + fileName;
    }
}
