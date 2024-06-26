package com.root.model;

import lombok.Data;

@Data
public class BlogResponse {

    private Long db_post;
    private String http_outbound;

    public BlogResponse(Long db_post, String http_outbound){
        this.db_post = db_post;
        this.http_outbound = http_outbound;
    }
}
