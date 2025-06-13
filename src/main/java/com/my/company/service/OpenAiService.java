package com.my.company.service;

import java.util.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OpenAiService {

  @Value("${openai.api.key}")
  private String apiKey;

  @Value("${openai.api.url}")
  private String apiUrl;

  private static final String SYSTEM_PROMPT =
      "Mamaly amin'ny teny malagasy fotsiny. Hazavao amin'ny teny malagasy ilay teny.";

  public String getMalagasyDefinition(String word) {
    RestTemplate restTemplate = new RestTemplate();

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(apiKey);
    headers.setContentType(MediaType.APPLICATION_JSON);

    // Préparation du message système et de l'utilisateur
    Map<String, Object> systemMessage = Map.of("role", "system", "content", SYSTEM_PROMPT);
    Map<String, Object> userMessage = Map.of("role", "user", "content", word);

    Map<String, Object> requestBody =
        Map.of(
            "model",
            "gpt-3.5-turbo",
            "messages",
            List.of(systemMessage, userMessage),
            "max_tokens",
            150,
            "temperature",
            0.5);

    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

    try {
      ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl, entity, Map.class);

      if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
        Map<String, Object> responseBody = response.getBody();
        List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
        if (choices != null && !choices.isEmpty()) {
          Map<String, Object> messageObj = (Map<String, Object>) choices.get(0).get("message");
          return messageObj.get("content").toString().trim();
        }
      }
    } catch (Exception e) {
      return "Nisy olana nahazo fanazavana tamin'ny ChatGPT: " + e.getMessage();
    }
    return "Tsy afaka nahazo fanazavana tamin'ny ChatGPT.";
  }
}
