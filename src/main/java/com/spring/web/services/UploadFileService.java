package com.spring.web.services;

import com.azure.storage.blob.*;
import com.azure.storage.blob.models.*;
import com.azure.storage.blob.sas.*;
import com.azure.storage.common.sas.SasProtocol;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.time.OffsetDateTime;
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
                blobClient.upload(dataStream, file.getSize(), true);
            }

            // Generar y devolver la URL con un SAS token válido por 2 semanas
            return generateSasToken(blobClient);
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

    private String generateSasToken(BlobClient blobClient) {
        // Definir permisos para el SAS (solo lectura)
        BlobSasPermission sasPermission = new BlobSasPermission().setReadPermission(true);

        // Establecer tiempo de expiración (2 semanas desde ahora)
        OffsetDateTime expiryTime = OffsetDateTime.now().plusWeeks(2);

        // Construir el SAS token
        BlobServiceSasSignatureValues sasValues = new BlobServiceSasSignatureValues(expiryTime, sasPermission)
                .setProtocol(SasProtocol.HTTPS_ONLY);

        // Generar el SAS token
        String sasToken = blobClient.generateSas(sasValues);

        // Devolver la URL con el SAS token
        return blobClient.getBlobUrl() + "?" + sasToken;
    }
}
