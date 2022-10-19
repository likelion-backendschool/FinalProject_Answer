package com.ll.exam.final__2022_10_08.app.post.controller;

import com.ll.exam.final__2022_10_08.app.base.exception.ActorCanNotModifyException;
import com.ll.exam.final__2022_10_08.app.base.exception.ActorCanNotRemoveException;
import com.ll.exam.final__2022_10_08.app.base.rq.Rq;
import com.ll.exam.final__2022_10_08.app.member.entity.Member;
import com.ll.exam.final__2022_10_08.app.post.entity.Post;
import com.ll.exam.final__2022_10_08.app.post.form.PostForm;
import com.ll.exam.final__2022_10_08.app.post.service.PostService;
import com.ll.exam.final__2022_10_08.app.postTag.entity.PostTag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
@Slf4j
public class PostController {
    private final PostService postService;
    private final Rq rq;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/write")
    public String showWrite() {
        return "post/write";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/write")
    public String write(@Valid PostForm postForm) {
        Member author = rq.getMember();
        Post post = postService.write(author, postForm.getSubject(), postForm.getContent(), postForm.getContentHtml(), postForm.getPostTagContents());
        return Rq.redirectWithMsg("/post/" + post.getId(), "%d번 글이 생성되었습니다.".formatted(post.getId()));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/modify")
    public String showModify(@PathVariable long id, Model model) {
        Post post = postService.findForPrintById(id).get();

        Member actor = rq.getMember();

        if (postService.actorCanModify(actor, post) == false) {
            throw new ActorCanNotModifyException();
        }

        model.addAttribute("post", post);

        return "post/modify";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/modify")
    public String modify(@Valid PostForm postForm, @PathVariable long id) {
        Post post = postService.findById(id).get();
        Member actor = rq.getMember();

        if (postService.actorCanModify(actor, post) == false) {
            throw new ActorCanNotModifyException();
        }

        postService.modify(post, postForm.getSubject(), postForm.getContent(), postForm.getContentHtml(), postForm.getPostTagContents());
        return Rq.redirectWithMsg("/post/" + post.getId(), "%d번 글이 수정되었습니다.".formatted(post.getId()));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Post post = postService.findForPrintById(id).get();

        Member actor = rq.getMember();

        if (postService.actorCanModify(actor, post) == false) {
            throw new ActorCanNotModifyException();
        }

        model.addAttribute("post", post);

        return "post/detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/list")
    public String list(Model model) {
        List<Post> posts = postService.findAllForPrintByAuthorIdOrderByIdDesc(rq.getId());

        model.addAttribute("posts", posts);

        return "post/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/tag/{tagContent}")
    public String tagList(Model model, @PathVariable String tagContent) {
        List<PostTag> postTags = postService.getPostTags(rq.getMember(), tagContent);

        model.addAttribute("postTags", postTags);
        return "post/tagList";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/remove")
    public String remove(@PathVariable long id) {
        Post post = postService.findById(id).get();
        Member actor = rq.getMember();

        if (postService.actorCanRemove(actor, post) == false) {
            throw new ActorCanNotRemoveException();
        }

        postService.remove(post);

        return Rq.redirectWithMsg("/post/list", "%d번 글이 삭제되었습니다.".formatted(post.getId()));
    }
}
