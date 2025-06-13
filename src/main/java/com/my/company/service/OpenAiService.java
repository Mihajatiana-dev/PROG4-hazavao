package com.my.company.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class OpenAiService {

  @Value("${openai.api.key}")
  private String apiKey;

  @Value("${openai.api.url}")
  private String apiUrl;

  public String getMalagasyDefinition(String word) {
    RestTemplate restTemplate = new RestTemplate();

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(apiKey);
    headers.setContentType(MediaType.APPLICATION_JSON);

    Map<String, Object> message = Map.of(
            "role", "user",
            "content", "Hazavao amin'ny teny malagasy ilay teny: " + word
    );

    Map<String, Object> requestBody = Map.of(
            "model", "gpt-3.5-turbo",
            "messages", List.of(message),
            "max_tokens", 100,
            "temperature", 0.7
    );

    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

    ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl, entity, Map.class);

    if (response.getStatusCode() == HttpStatus.OK) {
      List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
      if (choices != null && !choices.isEmpty()) {
        Map<String, Object> messageObj = (Map<String, Object>) choices.get(0).get("message");
        return messageObj.get("content").toString().trim();
      }
    }
    return "Tsy afaka nahazo fanazavana tamin'ny ChatGPT.";
  }
}
