package com.ll.exam.final__2022_10_08.app.post.entity;

import com.ll.exam.final__2022_10_08.app.base.entity.BaseEntity;
import com.ll.exam.final__2022_10_08.app.member.entity.Member;
import com.ll.exam.final__2022_10_08.app.postTag.entity.PostTag;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Setter
@SuperBuilder
@ToString(callSuper = true)
@NoArgsConstructor(access = PROTECTED)
public class Post extends BaseEntity {

    private String subject;

    @Column(columnDefinition = "LONGTEXT")
    private String content;
    @Column(columnDefinition = "LONGTEXT")
    private String contentHtml;

    @ManyToOne(fetch = LAZY)
    private Member author;

    @Builder.Default
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.EXTRA)
    Set<PostTag> postTags = new LinkedHashSet<>();

    public void updatePostTags(Set<PostTag> newPostTags) {
        // 지울거 모으고
        Set<PostTag> needToDelete = postTags
                .stream()
                .filter(Predicate.not(newPostTags::contains))
                .collect(Collectors.toSet());

        // 모아진걸 지우고
        needToDelete
                .stream()
                .forEach(postTags::remove);

        // 넣을거 넣는다.
        // SET 이기 때문에 중복 신경쓰지 말고 넣는다.
        newPostTags
                .stream()
                .forEach(postTags::add);
    }

    public String getForPrintContentHtml() {
        return contentHtml.replaceAll("toastui-editor-ww-code-block-highlighting", "");
    }

    public String getExtra_inputValue_hashTagContents() {
        return postTags
                .stream()
                .map(postTag -> "#" + postTag.getPostKeyword().getContent())
                .sorted()
                .collect(Collectors.joining(" "));
    }

    public String getExtra_postTagLinks() {
        return postTags
                .stream()
                .map(postTag -> {
                    String text = "#" + postTag.getPostKeyword().getContent();

                    return """
                            <a href="%s" class="text-link">%s</a>
                            """
                            .stripIndent()
                            .formatted(postTag.getPostKeyword().getListUrl(), text);
                })
                .sorted()
                .collect(Collectors.joining(" "));
    }

    public String getJdenticon() {
        return "post__" + getId();
    }
}
