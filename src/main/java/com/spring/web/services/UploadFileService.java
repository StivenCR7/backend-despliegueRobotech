package com.spring.web.services;

import com.azure.storage.blob.*;
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
            try {
                System.out.println("Obteniendo contenedor: " + containerName);
                BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
                if (!containerClient.exists()) {
                    System.out.println("Contenedor no existe. Creando contenedor: " + containerName);
                    containerClient.create();
                } else {
                    System.out.println("Contenedor existe: " + containerName);
                }

                // Generar un nombre único para el archivo
                String fileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
                System.out.println("Nombre del archivo generado: " + fileName);
                BlobClient blobClient = containerClient.getBlobClient(fileName);

                // Subir el archivo al blob
                try (InputStream dataStream = file.getInputStream()) {
                    System.out.println("Subiendo archivo al blob...");
                    blobClient.upload(dataStream, file.getSize(), true);
                    System.out.println("Archivo subido con éxito.");
                }

                // Opción de prueba: retornar la URL sin SAS token (descomentar para testear)
                // String blobUrl = blobClient.getBlobUrl();
                // System.out.println("Blob URL sin SAS: " + blobUrl);
                // return blobUrl;

                // Generar y retornar la URL con SAS token válido por 2 semanas
                String sasUrl = generateSasToken(blobClient);
                System.out.println("Blob URL con SAS token: " + sasUrl);
                return sasUrl;
            } catch (Exception ex) {
                System.err.println("Error en saveImage:");
                ex.printStackTrace();
                throw ex;
            }
        }
        return "default.jpg";
    }

    public void deleteImage(String blobName) {
        try {
            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
            BlobClient blobClient = containerClient.getBlobClient(blobName);
            if (blobClient.exists()) {
                System.out.println("Eliminando blob: " + blobName);
                blobClient.delete();
                System.out.println("Blob eliminado.");
            } else {
                System.out.println("Blob no existe: " + blobName);
            }
        } catch (Exception ex) {
            System.err.println("Error en deleteImage:");
            ex.printStackTrace();
            throw ex;
        }
    }

    private String generateSasToken(BlobClient blobClient) {
        try {
            // Definir permisos para el SAS (solo lectura)
            BlobSasPermission sasPermission = new BlobSasPermission().setReadPermission(true);
            // Establecer tiempo de expiración a 2 semanas
            OffsetDateTime expiryTime = OffsetDateTime.now().plusWeeks(2);
            // Construir el SAS token
            BlobServiceSasSignatureValues sasValues = new BlobServiceSasSignatureValues(expiryTime, sasPermission)
                    .setProtocol(SasProtocol.HTTPS_ONLY);
            String sasToken = blobClient.generateSas(sasValues);
            System.out.println("SAS token generado: " + sasToken);
            return blobClient.getBlobUrl() + "?" + sasToken;
        } catch (Exception ex) {
            System.err.println("Error en generateSasToken:");
            ex.printStackTrace();
            throw ex;
        }
    }
}
