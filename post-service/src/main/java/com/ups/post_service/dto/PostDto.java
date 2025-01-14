package com.ups.post_service.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PostDto {

    private Long id;
    private String content;
    private Long userId;
    private LocalDateTime createdAt;
}
