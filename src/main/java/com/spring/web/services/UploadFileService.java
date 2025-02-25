package com.spring.web.services;

import com.azure.storage.blob.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service 
public class UploadFileService {
	
	
	private final BlobServiceClient blobServiceClient;
    private final String containerName;

    public UploadFileService(@Value("${azure.storage.connection-string}") String connectionString,
                             @Value("${azure.storage.container-name}") String containerName) {
        this.blobServiceClient = new BlobServiceClientBuilder()
                                    .connectionString(connectionString)
                                    .buildClient();
        this.containerName = containerName;
    }

    public String saveImage(MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            // Obtener o crear el contenedor
            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
            if (!containerClient.exists()) {
                containerClient.create();
            }

            // Generar un nombre único para evitar colisiones
            String fileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
            BlobClient blobClient = containerClient.getBlobClient(fileName);

            try (InputStream dataStream = file.getInputStream()) {
                // El tercer parámetro 'true' indica que se sobrescribe si existe
                blobClient.upload(dataStream, file.getSize(), true);
            }

            // Retorna la URL del blob subido, que puedes usar en el front
            return blobClient.getBlobUrl();
        }
        return "default.jpg";
    }

    public void deleteImage(String blobName) {
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
        BlobClient blobClient = containerClient.getBlobClient(blobName);
        if (blobClient.exists()) {
            blobClient.delete();
        }
    }
	
}
