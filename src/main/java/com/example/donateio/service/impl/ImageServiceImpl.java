package com.example.donateio.service.impl;


import com.example.donateio.service.ImageService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;

import java.awt.font.ImageGraphicAttribute;
import java.io.*;

@Service
public class ImageServiceImpl implements ImageService {

    @Override
    public String uploadImage(String image, String filePath) {
        byte[] imageByte= Base64.decodeBase64(image);

        String directory1=System.getProperty("user.dir");


        String directory = directory1 + "/photos/" + filePath;

        try {
            new FileOutputStream(directory).write(imageByte);
        } catch (IOException e) {
            System.out.println("IMAGE WRITE EXCEPTION2");
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }

        return filePath;

    }

    @Override
    public byte[] getImage(String imageId) throws IOException {
        String dirPath = System.getProperty("user.dir");

        System.out.println("DIR PATH");
        System.out.println(dirPath);

        System.out.println("IMAGE ID");
        System.out.println(imageId);

        String fullPath = dirPath + "/photos/" + imageId;
        File file = new File(fullPath);
        FileInputStream fis = new FileInputStream(file);
        byte[] data = new byte[(int) file.length()];
        fis.read(data);
        fis.close();

        System.out.println("DATA IS READ");
        System.out.println(data);

        return data;
    }
}
