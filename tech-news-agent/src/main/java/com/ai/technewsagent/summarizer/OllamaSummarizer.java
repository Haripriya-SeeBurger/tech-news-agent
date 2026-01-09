/*
 * AiSummarizer.java
 *
 * created at 2026-01-09 by h.ravichandran <h.ravichandran@seeburger.com>
 *
 * Copyright (c) SEEBURGER AG, Germany. All Rights Reserved.
 */
package com.ai.technewsagent.summarizer;


import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Component
public class OllamaSummarizer implements NewsSummarizer
{
    private static final String OLLAMA_URL = "http://127.0.0.1:11434/api/generate";
    private static final String MODEL_NAME = "phi3";
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public String summarize(String title)
    {
        try
        {
            String prompt = "Summarize this tech news headline in one sentence:\n" + title;

            String requestBody = """
                            {
                              "model": "%s",
                              "prompt": "%s",
                              "stream": false
                            }
                            """.formatted(
                                          MODEL_NAME,
                                          escapeJson(prompt));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(OLLAMA_URL, request, String.class);

            return extractSummary(response.getBody());

        }
        catch (Exception e)
        {
            return "AI summary unavailable (Ollama not reachable)";
        }
    }


    /**
     * Extracts the "response" field from Ollama JSON output
     * without using external JSON libraries.
     */
    private String extractSummary(String json)
    {
        if (json == null || json.isEmpty())
        {
            return "No response from LLM";
        }

        int startIndex = json.indexOf("\"response\":\"");
        if (startIndex == -1)
        {
            return "Unable to parse AI response";
        }

        String content = json.substring(startIndex + 12);
        content = content.substring(0, content.indexOf("\""));

        return content.replace("\\n", " ").trim();
    }


    /**
     * Escapes double quotes for safe JSON embedding.
     */
    private String escapeJson(String text)
    {
        return text.replace("\"", "\\\"");
    }
}
