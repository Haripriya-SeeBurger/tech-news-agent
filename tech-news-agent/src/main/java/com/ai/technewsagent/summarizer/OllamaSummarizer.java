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

import com.ai.technewsagent.model.OllamaRequest;
import com.ai.technewsagent.model.OllamaResponse;


@Component
public class OllamaSummarizer implements NewsSummarizer
{
    private static final String OLLAMA_URL = "http://127.0.0.1:11434/api/generate";
    private static final String MODEL_NAME = "phi3";
    private final RestTemplate restTemplate;

    public OllamaSummarizer(RestTemplate restTemplate)
    {
        this.restTemplate = restTemplate;
    }

    @Override
    public String summarize(String title)
    {
        try
        {
            String prompt = "Summarize this tech news headline in one sentence:\n" + title;

            OllamaRequest requestBody = new OllamaRequest(MODEL_NAME, prompt, false);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<OllamaRequest> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<OllamaResponse> response = restTemplate.postForEntity(OLLAMA_URL, request, OllamaResponse.class);

            return response.getBody() != null
                            ? response.getBody().getResponse().trim()
                            : "No response from LLM";

           // return extractSummary(response.getBody());

        }
        catch (Exception e)
        {
            e.printStackTrace(); // TEMP
            return "AI summary unavailable (Ollama error)";
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
