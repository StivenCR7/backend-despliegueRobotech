package com.spring.web.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service 
public class UploadFileService {
    // Utiliza la variable de entorno HOME para definir la ruta
    private final String folder = System.getenv("HOME") + "/images/";
    
    public String saveImage(MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            // Asegurarse de que el directorio existe
            Path folderPath = Paths.get(folder);
            if (!Files.exists(folderPath)) {
                Files.createDirectories(folderPath);
            }
            
            byte[] bytes = file.getBytes();
            Path path = folderPath.resolve(file.getOriginalFilename());
            Files.write(path, bytes);
            return file.getOriginalFilename();
        }
        return "default.jpg";
    }
    
    public void deleteImage(String nombre) {
        String ruta = System.getenv("HOME") + "/images/";
        File file = new File(ruta + nombre);
        file.delete();
    }
}

