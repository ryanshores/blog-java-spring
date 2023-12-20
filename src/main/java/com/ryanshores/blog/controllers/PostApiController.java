package com.ryanshores.blog.controllers;

import com.ryanshores.blog.models.Post;
import com.ryanshores.blog.repositories.PostRepository;
import com.ryanshores.blog.services.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostApiController {

    private final PostService postService;
    private final PostRepository postRepo;

    public PostApiController(PostService postService, PostRepository postRepository) {
        this.postService = postService;
        this.postRepo = postRepository;
    }

    @GetMapping
    public List<Post> all() {
        return postRepo.findAll();
    }

    @GetMapping("{id}")
    public Post one(@PathVariable Long id) {
        return postService.getById(id).orElseThrow(() -> new PostNotFoundException(id));
    }

    @PostMapping
    public Post newPost(@RequestBody Post post) {
        return postService.save(post);
    }

    @PutMapping("{id}")
    public Post replacePost(@RequestBody Post post, @PathVariable Long id) {
        return postService.getById(id)
            .map(foundPost -> {
                foundPost.setTitle(post.getTitle());
                foundPost.setBody(post.getBody());
                return postService.save(foundPost);
            })
            .orElseGet(() -> {
                post.setId(id);
                return postService.save(post);
            });
    }

    @DeleteMapping("{id}")
    public void deletePost(@PathVariable Long id) {
        postService.deleteById(id);
    }
}

class PostNotFoundException extends RuntimeException {

    PostNotFoundException(Long id) {
        super("Could not find post " + id);
    }
}

@ControllerAdvice
class PostNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(PostNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String postNotFoundHandler(PostNotFoundException exception) {
        return exception.getMessage();
    }
}