package org.utn.tup.psbacknewsandnotifications.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.utn.tup.psbacknewsandnotifications.Dto.NewsDto;
import org.utn.tup.psbacknewsandnotifications.Dto.NewsResponseDto;
import org.utn.tup.psbacknewsandnotifications.Entity.NewsEntity;
import org.utn.tup.psbacknewsandnotifications.Service.NewsService;

import java.util.List;

@RestController
@RequestMapping("/api/news")
@RequiredArgsConstructor
public class NewsController {
    private final NewsService service;


    @GetMapping
    public ResponseEntity<List<NewsEntity>> getAllNews() {
        return new ResponseEntity<>(service.getAllNews(), HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<NewsResponseDto> crearNews(
            @RequestParam("title") String title,
            @RequestParam("body") String body,
            @RequestParam(value = "image", required = false) MultipartFile image) {
        NewsDto dto = new NewsDto(title, body, image);
        try {
            NewsResponseDto news = service.createNewsWithImageUrl(dto);
            return new ResponseEntity<>(news, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<NewsResponseDto> obtenerNews(@PathVariable Long id) {
        return new ResponseEntity<>(service.getNewsWithImageUrl(id), HttpStatus.OK);
    }

    // Endpoint para obtener la imagen como Resource
    @GetMapping("/image/{imageName}")
    public ResponseEntity<Resource> obtenerImagen(@PathVariable String imageName) {
        Resource recurso = service.loadImage(imageName);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG) // Ajusta según el tipo de imagen
                .body(recurso);
    }

    // Endpoint alternativo para obtener la imagen como bytes
    @GetMapping(value = "/image-bytes/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> obtenerImagenBytes(@PathVariable String imageName) {
        byte[] imagenBytes = service.getImageBytes(imageName);
        return ResponseEntity.ok().body(imagenBytes);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNews(@PathVariable Long id) {
        service.deleteNews(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
