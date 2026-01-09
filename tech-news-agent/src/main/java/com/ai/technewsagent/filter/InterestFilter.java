/*
 * InterestFilter.java
 *
 * created at 2026-01-09 by h.ravichandran <h.ravichandran@seeburger.com>
 *
 * Copyright (c) SEEBURGER AG, Germany. All Rights Reserved.
 */
package com.ai.technewsagent.filter;


import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.ai.technewsagent.model.Article;


@Component
public class InterestFilter
{
    private static final Set<String> INTEREST_KEYWORDS = Set.of(
                                                                "java",
                                                                "spring",
                                                                "backend",
                                                                "integration",
                                                                "api",
                                                                "ai",
                                                                "developer");

    public List<Article> filter(List<Article> articles)
    {
        return articles.stream()
                       .filter(this::matchesInterest)
                       .collect(Collectors.toList());
    }


    private boolean matchesInterest(Article article)
    {
        String title = article.getTitle().toLowerCase();

        return INTEREST_KEYWORDS.stream()
                                .anyMatch(title::contains);
    }
}
