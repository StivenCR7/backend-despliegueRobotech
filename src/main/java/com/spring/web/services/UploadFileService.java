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
        System.out.println("Inicializando BlobServiceClient con container: " + containerName);
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
                    System.out.println("Contenedor creado exitosamente.");
                } else {
                    System.out.println("Contenedor existe: " + containerName);
                }

                // Generar un nombre único para el archivo
                String fileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
                System.out.println("Nombre del archivo generado: " + fileName);
                System.out.println("Tamaño del archivo: " + file.getSize() + " bytes");
                
                BlobClient blobClient = containerClient.getBlobClient(fileName);
                System.out.println("Blob URL antes de la subida: " + blobClient.getBlobUrl());

                // Subir el archivo al blob
                try (InputStream dataStream = file.getInputStream()) {
                    System.out.println("Iniciando subida del archivo al blob...");
                    blobClient.upload(dataStream, file.getSize(), true);
                    System.out.println("Archivo subido con éxito.");
                } catch (Exception uploadEx) {
                    System.err.println("Error durante la subida del archivo:");
                    uploadEx.printStackTrace();
                    throw uploadEx;
                }

                // Verificar si el blob existe después de la subida
                if (blobClient.exists()) {
                    System.out.println("Verificación: El blob existe en el contenedor.");
                } else {
                    System.err.println("Verificación: El blob NO existe después de la subida.");
                }

                // Generar y retornar la URL con SAS token válido por 2 semanas
                String sasUrl = generateSasToken(blobClient);
                System.out.println("Blob URL con SAS token: " + sasUrl);
                return sasUrl;
            } catch (Exception ex) {
                System.err.println("Error en saveImage:");
                ex.printStackTrace();
                throw ex;
            }
        } else {
            System.out.println("El archivo recibido está vacío. Retornando 'default.jpg'.");
        }
        return "default.jpg";
    }

    public void deleteImage(String blobName) {
        try {
            System.out.println("Iniciando eliminación del blob: " + blobName);
            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
            BlobClient blobClient = containerClient.getBlobClient(blobName);
            if (blobClient.exists()) {
                System.out.println("Blob encontrado. Procediendo a eliminarlo...");
                blobClient.delete();
                System.out.println("Blob eliminado exitosamente.");
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
            System.out.println("Iniciando generación de SAS token...");
            // Definir permisos para el SAS (solo lectura)
            BlobSasPermission sasPermission = new BlobSasPermission().setReadPermission(true);
            System.out.println("Permiso de lectura establecido en SAS token.");

            // Establecer tiempo de expiración a 2 semanas desde ahora
            OffsetDateTime expiryTime = OffsetDateTime.now().plusWeeks(2);
            System.out.println("Tiempo de expiración establecido a: " + expiryTime);

            // Construir el SAS token
            BlobServiceSasSignatureValues sasValues = new BlobServiceSasSignatureValues(expiryTime, sasPermission)
                    .setProtocol(SasProtocol.HTTPS_ONLY);
            String sasToken = blobClient.generateSas(sasValues);
            System.out.println("SAS token generado: " + sasToken);

            String finalUrl = blobClient.getBlobUrl() + "?" + sasToken;
            System.out.println("URL final del blob con SAS token: " + finalUrl);
            return finalUrl;
        } catch (Exception ex) {
            System.err.println("Error en generateSasToken:");
            ex.printStackTrace();
            throw ex;
        }
    }
}
