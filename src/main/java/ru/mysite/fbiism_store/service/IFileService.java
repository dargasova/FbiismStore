package ru.mysite.fbiism_store.service;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

public interface IFileService {
    String saveFile(MultipartFile file, Long productId, String color, long imageCount) throws IOException;
}
