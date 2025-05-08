package org.utn.tup.psbacknewsandnotifications.Service;

import org.springframework.core.io.Resource;
import org.utn.tup.psbacknewsandnotifications.Dto.NewsDto;
import org.utn.tup.psbacknewsandnotifications.Dto.NewsResponseDto;
import org.utn.tup.psbacknewsandnotifications.Entity.NewsEntity;

import java.io.IOException;
import java.util.List;

public interface NewsService {
    List<NewsEntity> getAllNews();
    NewsEntity getOneNews(Long id);
    NewsResponseDto getNewsWithImageUrl(Long id);
    NewsEntity updateNews(NewsEntity entity);
    void deleteNews(Long id);
    NewsEntity createNews(NewsDto dto) throws IOException;
    NewsResponseDto createNewsWithImageUrl(NewsDto dto) throws IOException;
    Resource loadImage(String nombreImagen);
    byte[] getImageBytes(String nombreImagen);

}
