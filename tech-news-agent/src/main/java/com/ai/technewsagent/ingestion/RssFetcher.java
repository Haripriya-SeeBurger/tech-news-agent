/*
 * RssFetcher.java
 *
 * created at 2026-01-09 by h.ravichandran <h.ravichandran@seeburger.com>
 *
 * Copyright (c) SEEBURGER AG, Germany. All Rights Reserved.
 */
package com.ai.technewsagent.ingestion;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.ai.technewsagent.model.Article;

@Component
public class RssFetcher
{
    public List<Article> fetch(String rssUrl, String sourceName) {
        List<Article> articles = new ArrayList<>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new URL(rssUrl).openStream());

            NodeList items = doc.getElementsByTagName("item");

            for (int i = 0; i < items.getLength(); i++) {
                Element item = (Element) items.item(i);

                String title = getText(item, "title");
                String link = getText(item, "link");

                if (title != null && link != null) {
                    articles.add(new Article(title, link, sourceName));
                }
            }

        } catch (Exception e) {
            System.err.println("Failed to fetch RSS from " + sourceName + ": " + e.getMessage());
        }

        return articles;
    }

    private String getText(Element parent, String tag) {
        NodeList list = parent.getElementsByTagName(tag);
        if (list.getLength() > 0) {
            return list.item(0).getTextContent();
        }
        return null;
    }
}



