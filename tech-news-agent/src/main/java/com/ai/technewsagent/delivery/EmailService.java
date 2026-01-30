/*
 * EmailService.java
 *
 * created at 2026-01-28 by h.ravichandran <h.ravichandran@seeburger.com>
 *
 * Copyright (c) SEEBURGER AG, Germany. All Rights Reserved.
 */
package com.ai.technewsagent.delivery;

import java.util.List;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.ai.technewsagent.model.Article;

@Service
public class EmailService
{
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
        }

    public void sendDailySummary(String to, List<Article> articles) {


        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("📰 Your Daily Tech News Summary");


        StringBuilder body = new StringBuilder();
        body.append("Hello 👋\n\n");
        body.append("Here are today’s relevant tech updates:\n\n");


        for (Article article : articles) {
        body.append("• ").append(article.getTitle()).append("\n");
        body.append(" ").append(article.getSummary()).append("\n");
        body.append(" ").append(article.getLink()).append("\n\n");
        }


        message.setText(body.toString());


        mailSender.send(message);
        }
}



