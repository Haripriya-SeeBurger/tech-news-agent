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

import com.ai.technewsagent.delivery.EmailService;
import com.ai.technewsagent.filter.InterestFilter;
import com.ai.technewsagent.summarizer.NewsSummarizer;

@Component
public class NewsIngestionRunner implements CommandLineRunner
{
    private final RssFetcher rssFetcher;
    private final InterestFilter interestFilter;
    private final NewsSummarizer newsSummarizer;
    private final EmailService emailService;

    public NewsIngestionRunner(RssFetcher rssFetcher, InterestFilter interestFilter, NewsSummarizer newsSummarizer, EmailService emailService) {
        this.rssFetcher = rssFetcher;
        this.interestFilter = interestFilter;
        this.newsSummarizer = newsSummarizer;
        this.emailService = emailService;
    }

    @Override
    public void run(String... args) {
        var articles = rssFetcher.fetch("https://feeds.feedburner.com/TechCrunch/", "TechCrunch");

                                var relevantArticles = interestFilter.filter(articles);

                                System.out.println("=== Relevant Tech News ===");

                                relevantArticles.stream()
                                .limit(5)
                                .forEach(article -> {
                                    String summary = newsSummarizer.summarize(article.getTitle());
                                    article.setSummary(summary);

                                    System.out.println("📰 " + article.getTitle());
                                    System.out.println("🤖 " + article.getSummary());
                                    System.out.println();
                                });

                                emailService.sendDailySummary(
                                "test@example.com", // any email, Mailtrap will capture it
                                relevantArticles
                                );
    }
}



