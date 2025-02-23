package com.spring.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.spring.web.controller.CategoriaController;
import com.spring.web.model.Categorias;
import com.spring.web.services.CategoriaServices;

@WebMvcTest(CategoriaController.class) // Asegúrate de especificar el controlador aquí
class CategoriaControllerTest {

    @Autowired
    private MockMvc mockMvc;  // MockMvc para simular solicitudes HTTP

    // Mockear el servicio de categoría
    @MockBean
    private CategoriaServices categoriaService;

    @Test
    public void testCrearCategoria() throws Exception {
        // Crear un archivo de prueba
        MockMultipartFile file = new MockMultipartFile("banner", "banner.jpg", MediaType.IMAGE_JPEG_VALUE, "contenido del archivo".getBytes());

        // Crear un objeto de la categoría esperada
        Categorias categoriaEsperada = new Categorias();
        categoriaEsperada.setNombre("Real Steel");
        categoriaEsperada.setFormato("Eliminación Directa");
        categoriaEsperada.setCantidad(8);
        categoriaEsperada.setBanner("banner.jpg");

        // Configurar el comportamiento del servicio mockeado
        when(categoriaService.crearCategoria(any(Integer.class), any(Categorias.class))).thenReturn(categoriaEsperada);

        // Realizar la solicitud POST
        mockMvc.perform(multipart("/torneos/56/categorias")
                        .file(file)
                        .param("nombre", "Real Steel")
                        .param("cantidad", "8"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Real Steel"))
                .andExpect(jsonPath("$.formato").value("Eliminación Directa"))
                .andExpect(jsonPath("$.cantidad").value(8))
                .andExpect(jsonPath("$.banner").value("banner.jpg"));

        // Verificar que el método del servicio fue llamado correctamente
        verify(categoriaService, times(1)).crearCategoria(any(Integer.class), any(Categorias.class));
    }

    @Test
    public void testCrearCategoria_Error() throws Exception {
        // Crear un archivo de prueba
        MockMultipartFile file = new MockMultipartFile("banner", "banner.jpg", MediaType.IMAGE_JPEG_VALUE, "contenido del archivo".getBytes());

        // Configurar el comportamiento del servicio mockeado para lanzar una excepción
        when(categoriaService.crearCategoria(any(Integer.class), any(Categorias.class)))
            .thenThrow(new RuntimeException("Error al crear la categoría"));

        // Realizar la solicitud POST
        mockMvc.perform(multipart("/torneos/56/categorias")
                        .file(file)
                        .param("nombre", "Real Steel")
                        .param("cantidad", "8"))
                .andExpect(status().isInternalServerError());  // Verificar que la respuesta tenga un código 500
    }
}