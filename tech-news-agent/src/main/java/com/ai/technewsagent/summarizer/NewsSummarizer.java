/*
 * NewsSummarizer.java
 *
 * created at 2026-01-09 by h.ravichandran <h.ravichandran@seeburger.com>
 *
 * Copyright (c) SEEBURGER AG, Germany. All Rights Reserved.
 */
package com.ai.technewsagent.summarizer;


public interface NewsSummarizer
{
    /**
     * Generates a short summary for a given news headline.
     *
     * @param title news headline
     * @return summarized text
     */
    String summarize(String title);
}



