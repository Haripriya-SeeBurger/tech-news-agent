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

@Component
public class NewsIngestionRunner implements CommandLineRunner
{
    private final RssFetcher rssFetcher;

    public NewsIngestionRunner(RssFetcher rssFetcher) {
        this.rssFetcher = rssFetcher;
    }

    @Override
    public void run(String... args) {
        rssFetcher.fetch(
                "https://feeds.feedburner.com/TechCrunch/",
                "TechCrunch"
        ).stream()
         .limit(5)
         .forEach(a -> System.out.println("📰 " + a.getTitle()));
    }
}



