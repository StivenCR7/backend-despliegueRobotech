package com.spring.web.controller;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.spring.web.model.Noticias;
import com.spring.web.services.NoticiaServices;
import com.spring.web.services.UploadFileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/noticias")
@CrossOrigin
public class NoticiaController {

    private final NoticiaServices noticiaService;
    private final UploadFileService img;
    private final BlobContainerClient containerClient;

   
    public NoticiaController(NoticiaServices noticiaService, UploadFileService img,BlobContainerClient blobClient) {
    	this.containerClient = blobClient;
        this.noticiaService = noticiaService;
        this.img = img;
    }

    @GetMapping("/listar")
    public List<Noticias> listarNoticias() {
        List<Noticias> noticias = noticiaService.listarNoticias();

        // Generar SAS Token para cada imagen de las noticias
        for (Noticias noticia : noticias) {
            if (noticia.getImagen() != null && !noticia.getImagen().isEmpty()) {
                // Crear BlobClient a partir del nombre del archivo o la URL
                String blobName = noticia.getImagen().substring(noticia.getImagen().lastIndexOf("/") + 1); // Extrae el nombre del archivo
                BlobClient blobClient = containerClient.getBlobClient(blobName);

                // Generar la URL con SAS Token para cada imagen
                String imagenUrlConSas = img.generateSasToken(blobClient);
                noticia.setImagen(imagenUrlConSas);
            }
        }

        return noticias;
    }

    @GetMapping("/obtener/{id}")
    public ResponseEntity<Noticias> obtenerNoticiaPorId(@PathVariable Long id) {
        return noticiaService.obtenerNoticiaPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/add")
    public ResponseEntity<Noticias> crearNoticia(
            @RequestParam("titulo") String titulo,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("imagen") MultipartFile file) {

        try {
            Noticias noticia = new Noticias();
            // Aquí se sube la imagen a Azure Blob Storage y se obtiene la URL completa con SAS token.
            String imagenUrl = img.saveImage(file);
            noticia.setImagen(imagenUrl);
            noticia.setTitulo(titulo);
            noticia.setDescripcion(descripcion);

            Noticias nuevaNoticia = noticiaService.guardarNoticia(noticia);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaNoticia);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> actualizarNoticia(
            @PathVariable Long id,
            @RequestParam("titulo") String titulo,
            @RequestParam("descripcion") String descripcion,
            @RequestParam(value = "imagen", required = false) MultipartFile file) {

        return noticiaService.obtenerNoticiaPorId(id).map(noticiaExistente -> {
            try {
                noticiaExistente.setTitulo(titulo);
                noticiaExistente.setDescripcion(descripcion);

                if (file != null && !file.isEmpty()) {
                    // 🔥 Eliminar la imagen anterior antes de subir la nueva
                    if (noticiaExistente.getImagen() != null && !noticiaExistente.getImagen().isEmpty()) {
                        String oldBlobUrl = noticiaExistente.getImagen();
                        String oldBlobName = oldBlobUrl.substring(oldBlobUrl.lastIndexOf("/") + 1);
                        if (oldBlobName.contains("?")) {
                            oldBlobName = oldBlobName.substring(0, oldBlobName.indexOf("?"));
                        }
                        img.deleteImage(oldBlobName);
                    }

                    // 🆕 Subimos la nueva imagen y actualizamos la URL en la noticia.
                    String nuevaImagenUrl = img.saveImage(file);
                    noticiaExistente.setImagen(nuevaImagenUrl);
                }

                Noticias noticiaGuardada = noticiaService.guardarNoticia(noticiaExistente);
                return ResponseEntity.ok(noticiaGuardada);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> eliminarNoticia(@PathVariable Long id) {
        return noticiaService.obtenerNoticiaPorId(id).map(noticia -> {
            try {
                if (noticia.getImagen() != null && !noticia.getImagen().isEmpty()) {
                    // Extraer el nombre del archivo desde la URL
                    String blobUrl = noticia.getImagen();
                    String blobName = blobUrl.substring(blobUrl.lastIndexOf("/") + 1);
                    if (blobName.contains("?")) {
                        blobName = blobName.substring(0, blobName.indexOf("?"));
                    }
                    img.deleteImage(blobName);
                }

                noticiaService.eliminarNoticia(id);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
