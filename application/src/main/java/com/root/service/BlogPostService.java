package com.root.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.root.model.BlogPost;
import com.root.repository.BlogPostRepository;

@Service
public class BlogPostService {

    @Autowired
    private BlogPostRepository blogPostRepository;

    public Long saveBlogPost(BlogPost blog){
        BlogPost savedBlogPost = blogPostRepository.save(blog);
        return savedBlogPost.getId();
    }
}
