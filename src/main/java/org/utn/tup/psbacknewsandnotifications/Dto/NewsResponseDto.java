package org.utn.tup.psbacknewsandnotifications.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsResponseDto {
    private Long id;
    private String title;
    private String body;
    private String imageUrl;
    private LocalDateTime time;
}
