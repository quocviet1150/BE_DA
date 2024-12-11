package com.coverstar.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public class ShopUtil {

    private static String imageDirectory = "C://Images//";

    public static String handleFileUpload(MultipartFile file, String type, Long id) throws Exception {
        String filePath = imageDirectory + type + File.separator + id;
        File directory = new File(filePath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        String fullPath = filePath + File.separator + file.getOriginalFilename();
        file.transferTo(new File(fullPath));
        return fullPath;
    }
}
