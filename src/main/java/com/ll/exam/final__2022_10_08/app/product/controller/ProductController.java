package com.ll.exam.final__2022_10_08.app.product.controller;

import com.ll.exam.final__2022_10_08.app.base.exception.ActorCanNotModifyException;
import com.ll.exam.final__2022_10_08.app.base.exception.ActorCanNotRemoveException;
import com.ll.exam.final__2022_10_08.app.base.rq.Rq;
import com.ll.exam.final__2022_10_08.app.member.entity.Member;
import com.ll.exam.final__2022_10_08.app.post.entity.Post;
import com.ll.exam.final__2022_10_08.app.postkeyword.entity.PostKeyword;
import com.ll.exam.final__2022_10_08.app.postkeyword.service.PostKeywordService;
import com.ll.exam.final__2022_10_08.app.product.entity.Product;
import com.ll.exam.final__2022_10_08.app.product.form.ProductForm;
import com.ll.exam.final__2022_10_08.app.product.form.ProductModifyForm;
import com.ll.exam.final__2022_10_08.app.product.service.ProductService;
import com.ll.exam.final__2022_10_08.app.productTag.entity.ProductTag;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;
    private final PostKeywordService postKeywordService;
    private final Rq rq;

    @PreAuthorize("isAuthenticated() and hasAuthority('AUTHOR')")
    @GetMapping("/create")
    public String showCreate(Model model) {
        List<PostKeyword> postKeywords = postKeywordService.findByMemberId(rq.getId());
        model.addAttribute("postKeywords", postKeywords);
        return "product/create";
    }

    @PreAuthorize("isAuthenticated() and hasAuthority('AUTHOR')")
    @PostMapping("/create")
    public String create(@Valid ProductForm productForm) {
        Member author = rq.getMember();
        Product product = productService.create(author, productForm.getSubject(), productForm.getPrice(), productForm.getPostKeywordId(), productForm.getProductTagContents());
        return "redirect:/product/" + product.getId();
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Product product = productService.findForPrintById(id).get();
        List<Post> posts = productService.findPostsByProduct(product);

        model.addAttribute("product", product);
        model.addAttribute("posts", posts);

        return "product/detail";
    }

    @GetMapping("/list")
    public String list(Model model) {
        List<Product> products = productService.findAllForPrintByOrderByIdDesc(rq.getMember());

        model.addAttribute("products", products);

        return "product/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/modify")
    public String showModify(@PathVariable long id, Model model) {
        Product product = productService.findForPrintById(id).get();

        Member actor = rq.getMember();

        if (productService.actorCanModify(actor, product) == false) {
            throw new ActorCanNotModifyException();
        }

        model.addAttribute("product", product);

        return "product/modify";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/modify")
    public String modify(@Valid ProductModifyForm productForm, @PathVariable long id) {
        Product product = productService.findById(id).get();
        Member actor = rq.getMember();

        if (productService.actorCanModify(actor, product) == false) {
            throw new ActorCanNotModifyException();
        }

        productService.modify(product, productForm.getSubject(), productForm.getPrice(), productForm.getProductTagContents());
        return Rq.redirectWithMsg("/product/" + product.getId(), "%d번 도서 상품이 수정되었습니다.".formatted(product.getId()));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/remove")
    public String remove(@PathVariable long id) {
        Product post = productService.findById(id).get();
        Member actor = rq.getMember();

        if (productService.actorCanRemove(actor, post) == false) {
            throw new ActorCanNotRemoveException();
        }

        productService.remove(post);

        return Rq.redirectWithMsg("/post/list", "%d번 글이 삭제되었습니다.".formatted(post.getId()));
    }

    @GetMapping("/tag/{tagContent}")
    public String tagList(Model model, @PathVariable String tagContent) {
        List<ProductTag> productTags = productService.getProductTags(tagContent, rq.getMember());

        model.addAttribute("productTags", productTags);
        return "product/tagList";
    }
}
