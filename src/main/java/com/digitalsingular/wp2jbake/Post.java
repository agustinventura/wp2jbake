package com.digitalsingular.wp2jbake;

import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

public class Post {
    private String title;

    private Date publishingDate;

    private Set<String> tags = new TreeSet<>();

    private String content;

    public Post () {

    }

    public Post withTitle(String title) {
        this.title = title;
        return this;
    }

    public Post withPublishingDate(Date publishingDate) {
        this.publishingDate = publishingDate;
        return this;
    }

    public Post withTag(String tag) {
        this.tags.add(tag);
        return this;
    }

    public Post withContent(String content) {
        this.content = content;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Date getPublishingDate() {
        return publishingDate;
    }

    public Set<String> getTags() {
        return tags;
    }

    public String getContent() {
        return content;
    }
}

