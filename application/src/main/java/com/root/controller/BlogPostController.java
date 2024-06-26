package com.root.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.root.model.BlogPost;
import com.root.model.BlogResponse;
import com.root.service.BlogPostService;
import com.root.service.OutBoundApiCall;

@RestController
public class BlogPostController {

    @Autowired
    private BlogPostService blogPostService;

    @PostMapping("/createNewPost")
    public ResponseEntity<BlogResponse> saveBlogPost(@RequestBody BlogPost blogPost){
        try{
            Long savedBlogId = blogPostService.saveBlogPost(blogPost);
            var response = OutBoundApiCall.makeApiCall();
            System.out.println(new BlogResponse(savedBlogId, response));
            return new ResponseEntity<>(new BlogResponse(savedBlogId, response), HttpStatus.CREATED);
        }
        catch(Exception ex){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error: " 
            + ex.getMessage());
        }

        
    }
}
