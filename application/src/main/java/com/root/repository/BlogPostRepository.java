package com.root.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.root.model.BlogPost;

public interface BlogPostRepository extends JpaRepository<BlogPost, Long>{

}
