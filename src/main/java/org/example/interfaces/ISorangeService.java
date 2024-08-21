package org.example.interfaces;

import org.springframework.web.multipart.MultipartFile;

public interface ISorangeService {
    String saveFile(MultipartFile file);
    void deleteFile(String fileName);
}
