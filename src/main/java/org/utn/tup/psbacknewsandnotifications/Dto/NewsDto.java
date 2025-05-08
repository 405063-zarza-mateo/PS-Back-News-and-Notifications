package org.utn.tup.psbacknewsandnotifications.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsDto {
    private String title;
    private String body;
    private MultipartFile image;
}
