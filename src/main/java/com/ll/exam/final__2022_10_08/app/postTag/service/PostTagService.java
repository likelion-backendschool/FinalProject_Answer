package com.ll.exam.final__2022_10_08.app.postTag.service;

import com.ll.exam.final__2022_10_08.app.member.entity.Member;
import com.ll.exam.final__2022_10_08.app.post.entity.Post;
import com.ll.exam.final__2022_10_08.app.postKeyword.entity.PostKeyword;
import com.ll.exam.final__2022_10_08.app.postKeyword.service.PostKeywordService;
import com.ll.exam.final__2022_10_08.app.postTag.entity.PostTag;
import com.ll.exam.final__2022_10_08.app.postTag.repository.PostTagRepository;
import com.ll.exam.final__2022_10_08.app.productTag.entity.ProductTag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostTagService {
    private final PostKeywordService postKeywordService;
    private final PostTagRepository postTagRepository;

    public void applyPostTags(Post post, String postTagContents) {
        List<String> postKeywordContents = Arrays.stream(postTagContents.split("#"))
                .map(String::trim)
                .filter(s -> s.length() > 0)
                .collect(Collectors.toList());

        Set<PostTag> newPostTags = postKeywordContents
                .stream()
                .map(postKeywordContent -> savePostTag(post, postKeywordContent))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        post.updatePostTags(newPostTags);
    }

    private PostTag savePostTag(Post post, String postKeywordContent) {
        PostKeyword postKeyword = postKeywordService.save(postKeywordContent);

        Optional<PostTag> opPostTag = postTagRepository.findByPostIdAndPostKeywordId(post.getId(), postKeyword.getId());

        if (opPostTag.isPresent()) {
            return opPostTag.get();
        }

        PostTag postTag = PostTag.builder()
                .post(post)
                .member(post.getAuthor())
                .postKeyword(postKeyword)
                .build();

        postTagRepository.save(postTag);

        return postTag;
    }

    public List<PostTag> getPostTags(Post post) {
        return postTagRepository.findAllByPostId(post.getId());
    }

    public List<PostTag> getPostTagsByPostIdIn(long[] ids) {
        return postTagRepository.findAllByPostIdIn(ids);
    }

    public List<PostTag> getPostTags(Member member, String postKeywordContent) {
        return postTagRepository.findAllByMemberIdAndPostKeyword_contentOrderByPost_idDesc(member.getId(), postKeywordContent);
    }

    public List<PostTag> getPostTags(long authorId, long postKeywordId) {
        return postTagRepository.findAllByMemberIdAndPostKeywordIdOrderByPost_idDesc(authorId, postKeywordId);
    }

    public List<ProductTag> getPostTags(String productTagContent) {
        return postTagRepository.findAllByPostKeyword_contentOrderByPost_idDesc(productTagContent);
    }
}
