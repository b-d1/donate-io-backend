package com.example.donateio.service;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface ImageService {

    String uploadImage(String image, String fileName);

    byte[] getImage(String imageId) throws IOException;

}
