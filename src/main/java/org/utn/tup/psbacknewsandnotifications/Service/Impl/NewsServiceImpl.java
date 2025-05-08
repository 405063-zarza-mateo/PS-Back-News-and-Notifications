package org.utn.tup.psbacknewsandnotifications.Service.Impl;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.utn.tup.psbacknewsandnotifications.Dto.NewsDto;
import org.utn.tup.psbacknewsandnotifications.Dto.NewsResponseDto;
import org.utn.tup.psbacknewsandnotifications.Entity.NewsEntity;
import org.utn.tup.psbacknewsandnotifications.Repository.NewsRepository;
import org.utn.tup.psbacknewsandnotifications.Service.NewsService;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {
    private final NewsRepository repository;

    @Value("${app.upload-dir:uploads}")
    private String uploadDir;

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(Paths.get(uploadDir));
        } catch (IOException e) {
            throw new RuntimeException("No se pudo crear el directorio de subidas", e);
        }
    }

    @Override
    public List<NewsEntity> getAllNews() {
        return repository.findAll();
    }

    @Override
    public NewsEntity getOneNews(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Ha ocurrido un error al cargar la noticia"));
    }

    @Override
    public NewsResponseDto getNewsWithImageUrl(Long id) {
        NewsEntity news = getOneNews(id);
        return convertToResponseDto(news);
    }

    @Override
    public NewsEntity updateNews(NewsEntity entity) {
        NewsEntity existingNews = getOneNews(entity.getId());
        existingNews.setTitle(entity.getTitle());
        existingNews.setBody(entity.getBody());
        // La imagen se maneja en otro método si es necesario
        return repository.save(existingNews);
    }

    @Override
    public void deleteNews(Long id) {
        NewsEntity news = getOneNews(id);
        if (news.getImageRoute() != null) {
            try {
                Path imagePath = Paths.get(uploadDir).resolve(news.getImageRoute());
                Files.deleteIfExists(imagePath);
            } catch (IOException e) {
                System.err.println("Error al eliminar la imagen: " + e.getMessage());
            }
        }
        repository.deleteById(id);
    }

    @Override
    public NewsEntity createNews(NewsDto dto) throws IOException {
        NewsEntity news = new NewsEntity();
        news.setTitle(dto.getTitle());
        news.setBody(dto.getBody());

        if (dto.getImage() != null && !dto.getImage().isEmpty()) {
            String archiveName = UUID.randomUUID() + "_" + dto.getImage().getOriginalFilename();

            Path rutaCompleta = Paths.get(uploadDir, archiveName);

            Files.copy(dto.getImage().getInputStream(), rutaCompleta, StandardCopyOption.REPLACE_EXISTING);

            news.setImageRoute(archiveName);
        }

        news.setTime(LocalDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires")));

        return repository.save(news);
    }

    @Override
    public NewsResponseDto createNewsWithImageUrl(NewsDto dto) throws IOException {
        NewsEntity savedNews = createNews(dto);
        return convertToResponseDto(savedNews);
    }

    private NewsResponseDto convertToResponseDto(NewsEntity news) {
        NewsResponseDto responseDto = new NewsResponseDto();
        responseDto.setId(news.getId());
        responseDto.setTitle(news.getTitle());
        responseDto.setBody(news.getBody());
        responseDto.setTime(news.getTime());

        if (news.getImageRoute() != null) {
            responseDto.setImageUrl(baseUrl + "/api/news/image/" + news.getImageRoute());
        }

        return responseDto;
    }

    @Override
    public Resource loadImage(String nombreImagen) {
        try {
            Path rutaArchivo = Paths.get(uploadDir).resolve(nombreImagen);
            Resource recurso = new UrlResource(rutaArchivo.toUri());

            if (recurso.exists()) {
                return recurso;
            } else {
                throw new RuntimeException("No se encontró la imagen: " + nombreImagen);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error al cargar la imagen: " + nombreImagen, e);
        }
    }

    @Override
    public byte[] getImageBytes(String nombreImagen) {
        try {
            Path rutaArchivo = Paths.get(uploadDir).resolve(nombreImagen);
            return Files.readAllBytes(rutaArchivo);
        } catch (IOException e) {
            throw new RuntimeException("Error al leer la imagen: " + nombreImagen, e);
        }
    }
}
