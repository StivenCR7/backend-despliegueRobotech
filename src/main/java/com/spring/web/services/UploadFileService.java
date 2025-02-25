package com.spring.web.services;

import com.azure.storage.blob.*;
import com.azure.storage.blob.sas.*;
import com.azure.storage.common.sas.SasProtocol;
import reactor.core.publisher.Mono;

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

    public Mono<String> saveImageAsync(MultipartFile file) {
        if (file.isEmpty()) {
            return Mono.just("default.jpg");
        }

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

            BlobAsyncClient blobAsyncClient = containerClient.getBlobAsyncClient(fileName);
            System.out.println("Blob URL antes de subir (async): " + blobAsyncClient.getBlobUrl());

            // Subir el archivo de forma asíncrona
            return Mono.using(
                () -> file.getInputStream(),
                inputStream -> blobAsyncClient.upload(inputStream, file.getSize(), true)
                            .doOnSuccess(response -> System.out.println("Archivo subido con éxito."))
                            .then(Mono.fromCallable(() -> generateSasToken(blobAsyncClient))),
                InputStream::close
            );
        } catch (IOException ex) {
            ex.printStackTrace();
            return Mono.error(ex);
        }
    }

    // Método para eliminar de forma sincrónica (puedes adaptarlo a async si lo prefieres)
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

    // Método sincrónico para generar el SAS token a partir de BlobAsyncClient
    private String generateSasToken(BlobAsyncClient blobAsyncClient) {
        try {
            System.out.println("Iniciando generación de SAS token (async)...");
            BlobSasPermission sasPermission = new BlobSasPermission().setReadPermission(true);
            OffsetDateTime expiryTime = OffsetDateTime.now().plusWeeks(2);
            System.out.println("Tiempo de expiración: " + expiryTime);
            BlobServiceSasSignatureValues sasValues = new BlobServiceSasSignatureValues(expiryTime, sasPermission)
                    .setProtocol(SasProtocol.HTTPS_ONLY);
            // Usamos el BlobClient sincrónico para generar el SAS token (ya que el método generateSas es sincrónico)
            BlobClient blobClient = blobAsyncClient.getBlockBlobClient();
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
