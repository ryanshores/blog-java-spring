package com.ryanshores.blog.config;

import com.ryanshores.blog.models.Account;
import com.ryanshores.blog.models.Authority;
import com.ryanshores.blog.models.Post;
import com.ryanshores.blog.repositories.AuthorityRepository;
import com.ryanshores.blog.services.AccountService;
import com.ryanshores.blog.services.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
@Component
public class SpringData implements CommandLineRunner {

    @Autowired
    private PostService postService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AuthorityRepository authorityRepository;

    private static final Logger log = LoggerFactory.getLogger(SpringData.class);

    @Override
    public void run(String... args) throws Exception {
        List<Post> posts = postService.getAll();

        if (posts.isEmpty()) {
            var userAuthority = new Authority();
            userAuthority.setName("ROLE_USER");
            log.info("Preloading " + authorityRepository.save(userAuthority));

            var adminAuthority = new Authority();
            adminAuthority.setName("ROLE_ADMIN");
            log.info("Preloading " + authorityRepository.save(adminAuthority));

            var account1 = new Account();
            account1.setFirstName("user");
            account1.setLastName("user");
            account1.setEmail("user@email.com");
            account1.setPassword("password");
            var authorities1 = new HashSet<Authority>();
            authorityRepository.findById("ROLE_USER").ifPresent(authorities1::add);
            account1.setAuthorities(authorities1);
            log.info("Preloading " + accountService.save(account1));

            var account2 = new Account();
            account2.setFirstName("admin");
            account2.setLastName("admin");
            account2.setEmail("admin@email.com");
            account2.setPassword("password");
            var authorities2 = new HashSet<Authority>();
            authorityRepository.findById("ROLE_ADMIN").ifPresent(authorities2::add);
            account2.setAuthorities(authorities2);
            log.info("Preloading " + accountService.save(account2));

            Post post1 = new Post();
            post1.setTitle("Title of Post 1");
            post1.setBody("This is the body of post 1");
            post1.setAccount(account1);
            log.info("Preloading " + postService.save(post1));

            Post post2 = new Post();
            post2.setTitle("Title of Post 2");
            post2.setBody("This is the body of post 2");
            post2.setAccount(account2);
            log.info("Preloading " + postService.save(post2));
        }
    }
}
