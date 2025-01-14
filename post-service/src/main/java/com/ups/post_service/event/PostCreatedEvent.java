package com.ups.post_service.event;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostCreatedEvent {
    private Long userId;
    private String content;
    private Long postId;
}
