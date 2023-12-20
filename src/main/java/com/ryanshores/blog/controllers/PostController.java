package com.ryanshores.blog.controllers;

import com.ryanshores.blog.models.Account;
import com.ryanshores.blog.models.Post;
import com.ryanshores.blog.services.AccountService;
import com.ryanshores.blog.services.PostService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class PostController {

    private final PostService postService;

    private final AccountService accountService;

    public PostController(PostService postService, AccountService accountService) {
        this.postService = postService;
        this.accountService = accountService;
    }

    // Views

    @GetMapping("/posts/{id}")
    public String getPost(@PathVariable Long id, Model model) {
        Optional<Post> optionalPost = postService.getById(id);

        if (optionalPost.isPresent()) {
            var post = optionalPost.get();
            model.addAttribute("post", post);
            return "post";
        } else {
            return "404";
        }
    }

    @GetMapping("/posts/new")
    public String createNewPost(Model model){
        Optional<Account> optionalAccount = accountService.findByEmail("user@email.com");
        if (optionalAccount.isPresent()) {
            var post = new Post();
            post.setAccount(optionalAccount.get());
            model.addAttribute("post", post);
            return "post_new";
        } else return "404";
    }

    @PostMapping("/posts/new")
    public String saveNewPost(@ModelAttribute Post post){
        postService.save(post);

        return "redirect:/posts/" + post.getId();
    }

    @GetMapping("/posts/{id}/edit")
    @PreAuthorize("isAuthenticated()")
    public String getPostForEdit(@PathVariable Long id, Model model) {

        var optionalPost = postService.getById(id);

        if (optionalPost.isPresent()) {
            var post = optionalPost.get();
            model.addAttribute("post", post);
            return "post_edit";
        } else return "404";
    }

    @PostMapping("/posts/{id}")
    @PreAuthorize("isAuthenticated()")
    public String updatePost(@PathVariable Long id, Post post) {

        var optionalPost = postService.getById(id);

        if (optionalPost.isPresent()) {
            var existingPost = optionalPost.get();

            existingPost.setTitle(post.getTitle());
            existingPost.setBody(post.getBody());

            postService.save(existingPost);
        }

        return "redirect:/posts/" + post.getId();
    }

    @GetMapping("/posts/{id}/delete")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String deletePost(@PathVariable Long id) {

        var optionalPost = postService.getById(id);

        optionalPost.ifPresent(postService::delete);

        return optionalPost.isPresent() ? "redirect:/" : "404";
    }
}
