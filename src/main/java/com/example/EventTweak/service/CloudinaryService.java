package com.example.EventTweak.service;

import com.cloudinary.Cloudinary;
import jakarta.mail.Multipart;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CloudinaryService {

    private Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary){
        this.cloudinary = cloudinary;
    }

    public Map uploadSingleFile(MultipartFile file) throws IOException {
       return this.cloudinary.uploader().upload(file.getBytes(), Map.of());
    }

    public Map deleteSingleFile(String publicId) throws  IOException{
        return this.cloudinary.uploader().destroy(publicId, Map.of());
    }

    public List<Map> uploadMultipleFile(MultipartFile[] sampleWorks) throws IOException {
        List<Map> result = new ArrayList<>();
        for(MultipartFile file: sampleWorks){
            result.add(uploadSingleFile(file));
        }
        return result;
    }


}
