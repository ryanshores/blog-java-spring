package com.ryanshores.blog.config;

import com.ryanshores.blog.models.Post;
import com.ryanshores.blog.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SpringData implements CommandLineRunner {

    @Autowired
    private PostService postService;

    @Override
    public void run(String... args) throws Exception {
        List<Post> posts = postService.getAll();

        if (posts.isEmpty()) {
            Post post1 = new Post();
            post1.setTitle("Title of Post 1");
            post1.setBody("This is the body of post 1");

            Post post2 = new Post();
            post2.setTitle("Title of Post 2");
            post2.setBody("This is the body of post 2");

            postService.save(post1);
            postService.save(post2);
        }
    }
}
