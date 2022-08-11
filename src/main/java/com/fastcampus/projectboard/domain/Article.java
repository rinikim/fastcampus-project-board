package com.fastcampus.projectboard.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@ToString
@Table(indexes = {
        @Index(columnList = "title"),
        @Index(columnList = "hashtag"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy"),
})
@Entity
public class Article extends AuditingFields{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter @Column(nullable = false)
    private String title;       // 제목

    @Setter @Column(nullable = false, length = 10000)
    private String content;     // 본문

    @Setter
    private String hashtag;     // 해시테그

    /*
        실무에서는 양방향을 풀고 진행하는 경우도 많다.
        cascade 로 인해서 서로 결합되면 마이그레이션 하거나 편집할 때 어려움이 따를 수 있고,
        원치 않는 손실이 일어날 수 있기 때문이다.
        논리상으로는 게시글이 지워지면 거기에 딸린 댓글은 의미가 없어져 사라지는 것이 맞지만,
        운영은 다를 수 있다. 게시글이 사라져도, 댓글은 백업목적으로 남겨야 하는 경우가 있다.
        이러하면 cascade 는 운영입장에서는 불상사이다.
        그래서 일부러 연관관께 FK 를 걸지 않는 경우가 있다.
        퍼포먼스 이슈때문에도 FK 를 걸지 않는 경우가 있다.
        하지만 지금은 공부하는 것이기 때문에 적용을 해보자.
     */
    @OrderBy("id")  // 정렬기준
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL) // mappedBy 하지 않으면 article_articleComment 로 생김
    @ToString.Exclude   // 게시글을 보면서 댓글을 모두 다 볼 필요 없으므로 해당 ToString 을 제외한다.
    private final Set<ArticleComment> articleComments = new LinkedHashSet<>();  // 중복허용 x

    protected Article() {}

    private Article(String title, String content, String hashtag) {
        this.title = title;
        this.content = content;
        this.hashtag = hashtag;
    }

    // factory method pattern - domain article 을 생성하고자 할 때 어떤 값을 필요한지 쉽게 확인할 수 있다.
    public static Article of(UserAccount userAccount, String title, String content, String hashtag) {
        return new Article(title, content, hashtag);
    }

    /*
        list or collection 을 사용할 때 id 는 pk 이기 때문에 다른 값을 비교할 필요 없이 id 가 같으면,
        같은 object 라고 볼 수 있다.
        check accept subclasses as parameter to equals() method
        check select all non-null field
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Article article)) return false;  // java 14 pattern matching 참고
        return id != null && id.equals(article.id); // 새로 만든 article 이 영속화가 되지 않았으면 (id == null) 모든 값이 같아도 같다고 보지 않는다.
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
