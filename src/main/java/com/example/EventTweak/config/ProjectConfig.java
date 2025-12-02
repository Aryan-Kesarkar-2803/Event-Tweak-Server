package com.example.EventTweak.config;

import com.cloudinary.Cloudinary;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ProjectConfig {

//    Dotenv dotenv = Dotenv.load();
    @Bean
    public Cloudinary getCloudinary(){
        Map<String, Object> config = new HashMap<>();
        config.put("cloud_name",System.getenv("CLOUD_NAME"));
        config.put("api_key",System.getenv("CLOUD_API_KEY"));
        config.put("api_secret",System.getenv("CLOUD_API_SECRET"));


//        config.put("cloud_name",dotenv.get("CLOUD_NAME"));
//        config.put("api_key",dotenv.get("CLOUD_API_KEY"));
//        config.put("api_secret",dotenv.get("CLOUD_API_SECRET"));


        config.put("secret",true);
        return new Cloudinary(config);
    }
}
