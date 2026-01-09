/*
 * NewsIngestionRunner.java
 *
 * created at 2026-01-09 by h.ravichandran <h.ravichandran@seeburger.com>
 *
 * Copyright (c) SEEBURGER AG, Germany. All Rights Reserved.
 */
package com.ai.technewsagent.ingestion;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.ai.technewsagent.filter.InterestFilter;
import com.ai.technewsagent.summarizer.NewsSummarizer;

@Component
public class NewsIngestionRunner implements CommandLineRunner
{
    private final RssFetcher rssFetcher;
    private final InterestFilter interestFilter;
    private final NewsSummarizer newsSummarizer;

    public NewsIngestionRunner(RssFetcher rssFetcher, InterestFilter interestFilter, NewsSummarizer newsSummarizer) {
        this.rssFetcher = rssFetcher;
        this.interestFilter = interestFilter;
        this.newsSummarizer = newsSummarizer;
    }

    @Override
    public void run(String... args) {
        var articles = rssFetcher.fetch(
                                        "https://feeds.feedburner.com/TechCrunch/",
                                        "TechCrunch"
                                );

                                var relevantArticles = interestFilter.filter(articles);

                                System.out.println("=== Relevant Tech News ===");

                                relevantArticles.stream()
                                .limit(3)
                                .forEach(article -> {
                                    String summary = newsSummarizer.summarize(article.getTitle());
                                    article.setSummary(summary);

                                    System.out.println("📰 " + article.getTitle());
                                    System.out.println("🤖 " + article.getSummary());
                                    System.out.println();
                                });
    }
}



