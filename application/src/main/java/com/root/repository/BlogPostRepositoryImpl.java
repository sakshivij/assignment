package com.root.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.root.model.BlogPost;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
@Transactional
public class BlogPostRepositoryImpl implements BlogPostRepository {

    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public BlogPost savePost(BlogPost blog) {
            entityManager.persist(blog);
            return blog;
    }
    
}
