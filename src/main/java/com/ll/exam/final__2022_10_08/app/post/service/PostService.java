package com.ll.exam.final__2022_10_08.app.post.service;

import com.ll.exam.final__2022_10_08.app.member.entity.Member;
import com.ll.exam.final__2022_10_08.app.post.entity.Post;
import com.ll.exam.final__2022_10_08.app.post.repository.PostRepository;
import com.ll.exam.final__2022_10_08.app.postTag.entity.PostTag;
import com.ll.exam.final__2022_10_08.app.postTag.service.PostTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;
    private final PostTagService postTagService;

    @Transactional
    public Post write(Member author, String subject, String content, String contentHtml, String postTagContents) {
        Post post = Post.builder()
                .subject(subject)
                .content(content)
                .contentHtml(contentHtml)
                .author(author)
                .build();
        postRepository.save(post);

        applyPostTags(post, postTagContents);

        return post;
    }

    public Optional<Post> findById(long postId) {
        return postRepository.findById(postId);
    }

    @Transactional
    public void applyPostTags(Post post, String postTagContents) {
        postTagService.applyPostTags(post, postTagContents);
    }

    public List<PostTag> getPostTags(Post post) {
        return postTagService.getPostTags(post);
    }

    @Transactional
    public void modify(Post post, String subject, String content, String contentHtml, String postTagContents) {
        post.setSubject(subject);
        post.setContent(content);
        post.setContentHtml(contentHtml);
        applyPostTags(post, postTagContents);
    }

    public boolean actorCanModify(Member author, Post post) {
        return author.getId().equals(post.getAuthor().getId());
    }

    public boolean actorCanRemove(Member author, Post post) {
        return actorCanModify(author, post);
    }

    public List<PostTag> getPostTags(Member author, String postKeywordContent) {
        List<PostTag> postTags = postTagService.getPostTags(author, postKeywordContent);

        loadForPrintDataOnPostTagList(postTags);

        return postTags;
    }

    public Optional<Post> findForPrintById(long id) {
        Optional<Post> opPost = findById(id);

        if (opPost.isEmpty()) return opPost;

        List<PostTag> postTags = getPostTags(opPost.get());

        opPost.get().getExtra().put("postTags", postTags);

        return opPost;
    }

    private void loadForPrintDataOnPostTagList(List<PostTag> postTags) {
        List<Post> posts = postTags
                .stream()
                .map(PostTag::getPost)
                .collect(toList());

        loadForPrintData(posts);
    }

    public void loadForPrintData(List<Post> posts) {

    }

    public List<Post> findAllForPrintByAuthorIdOrderByIdDesc(long authorId) {
        List<Post> posts = postRepository.findAllByAuthorIdOrderByIdDesc(authorId);
        loadForPrintData(posts);

        return posts;
    }

    @Transactional
    public void remove(Post post) {
        postRepository.delete(post);
    }

    public boolean actorCanSee(Member actor, Post post) {
        if (actor == null) return false;
        if (post == null) return false;

        return post.getAuthor().getId().equals(actor.getId());
    }
}
