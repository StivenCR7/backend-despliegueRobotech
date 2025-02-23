
package com.spring.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.spring.web.model.Noticias;
import com.spring.web.services.NoticiaServices;
import com.spring.web.services.UploadFileService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/noticias")
@CrossOrigin
public class NoticiaController {
	private final NoticiaServices noticiaService;

	@Autowired
	private UploadFileService img;

	public NoticiaController(NoticiaServices noticiaService) {
		this.noticiaService = noticiaService;
	}

	@GetMapping("/listar")
	public List<Noticias> listarNoticias() {
		return noticiaService.listarNoticias();
	}

	@GetMapping("/obtener/{id}")
	public ResponseEntity<Noticias> obtenerNoticiaPorId(@PathVariable Long id) {
		return noticiaService.obtenerNoticiaPorId(id).map(ResponseEntity::ok)
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}

	@PostMapping("/add")
	public ResponseEntity<Noticias> crearNoticia(@RequestParam("titulo") String titulo,
			@RequestParam("descripcion") String descripcion, @RequestParam("imagen") MultipartFile file) {

		try {
			Noticias noticia = new Noticias();
			String nombreImagen = img.saveImage(file);
			noticia.setImagen(nombreImagen);
			noticia.setTitulo(titulo);
			noticia.setDescripcion(descripcion);

			Noticias nuevaNoticia = noticiaService.guardarNoticia(noticia);
			return ResponseEntity.status(HttpStatus.CREATED).body(nuevaNoticia);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<?> actualizarNoticia(@PathVariable Long id,
	        @RequestParam("titulo") String titulo,
	        @RequestParam("descripcion") String descripcion,
	        @RequestParam(value = "imagen", required = false) MultipartFile file) {
	    return noticiaService.obtenerNoticiaPorId(id).map(noticiaExistente -> {
	        try {
	            noticiaExistente.setTitulo(titulo);
	            noticiaExistente.setDescripcion(descripcion);

	            if (file != null && !file.isEmpty()) {
	                String nombreImagen = img.saveImage(file);
	                noticiaExistente.setImagen(nombreImagen);
	            }

	            Noticias noticiaGuardada = noticiaService.guardarNoticia(noticiaExistente);
	            return ResponseEntity.ok(noticiaGuardada); // Siempre retorna ResponseEntity<?>
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // También ResponseEntity<?>
	        }
	    }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build()); // Igualmente ResponseEntity<?>
	}


	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Object> eliminarNoticia(@PathVariable Long id) {
		return noticiaService.obtenerNoticiaPorId(id).map(noticia -> {
			try {

				if (noticia.getImagen() != null && !noticia.getImagen().isEmpty()) {
					eliminarArchivo(noticia.getImagen());
				}

				noticiaService.eliminarNoticia(id);
				return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
			} catch (Exception e) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			}
		}).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}

	private void eliminarArchivo(String nombreArchivo) {
		try {
			java.nio.file.Path rutaArchivo = Paths.get("ruta/donde/guardar/imagenes").resolve(nombreArchivo);
			Files.deleteIfExists(rutaArchivo);
		} catch (IOException e) {
			System.err.println("No se pudo eliminar el archivo: " + nombreArchivo);
			e.printStackTrace();
		}
	}
}
