package com.fastcampus.projectboard.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public record ArticleUpdateDto(
        String title,
        String content,
        String hashtag
) implements Serializable {

    public static ArticleUpdateDto of(String title,
                            String content,
                            String hashtag) {
        return new ArticleUpdateDto(title, content, hashtag);
    }
}



