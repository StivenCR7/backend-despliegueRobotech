package com.spring.web.services;

import com.azure.storage.blob.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.azure.storage.blob.sas.*;
import com.azure.storage.common.sas.SasProtocol;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;
import java.io.IOException;


@Service
public class UploadFileService {

	private final BlobContainerClient containerClient;

	public UploadFileService(@Value("${azure.storage.connection-string}") String connectionString,
			@Value("${azure.storage.container-name}") String containerName) {
		BlobServiceClient blobServiceClient = new BlobServiceClientBuilder().connectionString(connectionString)
				.buildClient();
		this.containerClient = blobServiceClient.getBlobContainerClient(containerName);
	}

	public String saveImage(MultipartFile file) throws IOException {
		String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();
		BlobClient blobClient = containerClient.getBlobClient(fileName);
		blobClient.upload(file.getInputStream(), file.getSize(), true);

		// Generar URL con SAS Token
		return generateSasToken(blobClient);
	}

	// Método para eliminar archivos
    	public void deleteImage(String fileName) {
        	BlobClient blobClient = containerClient.getBlobClient(fileName);
        	if (blobClient.exists()) {
            	blobClient.delete();
        	} else {
            	throw new RuntimeException("El archivo no existe en el almacenamiento de Azure.");
        	}
    	}

	private String generateSasToken(BlobClient blobClient) {
		BlobSasPermission permission = new BlobSasPermission().setReadPermission(true);
		OffsetDateTime expiryTime = OffsetDateTime.now(ZoneOffset.UTC).plusHours(1); // Expira en 1 hora

		BlobServiceSasSignatureValues values = new BlobServiceSasSignatureValues(expiryTime, permission)
				.setStartTime(OffsetDateTime.now(ZoneOffset.UTC)).setProtocol(SasProtocol.HTTPS_ONLY);

		String sasToken = blobClient.generateSas(values);
		return blobClient.getBlobUrl() + "?" + sasToken;
	}
}
