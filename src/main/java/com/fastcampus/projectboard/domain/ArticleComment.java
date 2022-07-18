package com.fastcampus.projectboard.domain;

import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@ToString
@Table(indexes = {
        @Index(columnList = "content"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy"),
})
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne(optional = false)    // 필수값 명시, 댓글을 지웠을 경우 게시글에 영향을 받지 말아야 하므로 cascade x
    private Article article;    // 게시글 (ID)

    @Setter @Column(nullable = false, length = 500)
    private String content;     // 본문

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;    // 생성일시

    @CreatedBy
    @Column(nullable = false, length = 100)
    private String createdBy;           // 생성자

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime modifiedAt;   // 수정일시

    @LastModifiedBy
    @Column(nullable = false, length = 100)
    private String modifiedBy;          // 수정자

    private ArticleComment(Article article, String content) {
        this.article = article;
        this.content = content;
    }

    public static ArticleComment of(Article article, String content) {
        return new ArticleComment(article, content);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArticleComment that)) return false;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
