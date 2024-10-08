package org.example.interfaces;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public interface ISorangeService {
    String saveFile(MultipartFile file);
    void deleteFile(String fileName);
    File getFile(String fileName) throws IOException;
}
