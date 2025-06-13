package com.my.company.service;

import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class OpenAiService {

  private static final Logger logger = LoggerFactory.getLogger(OpenAiService.class);

  @Value("${openai.api.key:}")
  private String apiKey;

  @Value("${openai.api.url:https://api.openai.com/v1/chat/completions}")
  private String apiUrl;

  private final RestTemplate restTemplate;

  // Constructeur avec injection de RestTemplate pour faciliter les tests
  public OpenAiService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  // Constructeur par défaut
  public OpenAiService() {
    this.restTemplate = new RestTemplate();
  }

  public String getMalagasyDefinition(String word) {
    // Vérification de la configuration
    if (apiKey == null || apiKey.trim().isEmpty()) {
      logger.warn("OpenAI API key tsy napetraka");
      return "Tsy afaka nifandray tamin'ny serivisy fanazavana (API key tsy hita).";
    }

    try {
      // Configuration des headers
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(apiKey);
      headers.setContentType(MediaType.APPLICATION_JSON);

      // Amélioration du prompt pour des définitions plus précises
      String prompt =
          String.format(
              "Hazavao amin'ny teny malagasy fotsiny ny teny '%s'. "
                  + "Omeo fanazavana fohy sy mazava. Aza mamerina ny teny anglisy.",
              word);

      Map<String, Object> message = Map.of("role", "user", "content", prompt);

      Map<String, Object> requestBody =
          Map.of(
              "model", "gpt-3.5-turbo",
              "messages", List.of(message),
              "max_tokens", 150,
              "temperature", 0.3,
              "top_p", 1.0,
              "frequency_penalty", 0.0,
              "presence_penalty", 0.0);

      HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

      logger.info("Fangatahana fanazavana ho an'ny teny: {}", word);

      ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl, entity, Map.class);

      if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
        Map<String, Object> responseBody = response.getBody();

        // Vérification de la présence des données
        if (responseBody.containsKey("choices")) {
          List<Map<String, Object>> choices =
              (List<Map<String, Object>>) responseBody.get("choices");

          if (choices != null && !choices.isEmpty()) {
            Map<String, Object> choice = choices.get(0);

            if (choice.containsKey("message")) {
              Map<String, Object> messageObj = (Map<String, Object>) choice.get("message");

              if (messageObj.containsKey("content")) {
                String definition = messageObj.get("content").toString().trim();

                // Nettoyage de la réponse
                if (!definition.isEmpty()) {
                  logger.info("Nahavita fanazavana ho an'ny teny: {}", word);
                  return definition;
                }
              }
            }
          }
        }

        // Si la structure de réponse est inattendue
        logger.warn("Vokatra tsy voafehy avy amin'ny OpenAI ho an'ny teny: {}", word);
        return "Tsy afaka nahazo fanazavana marina.";
      }

      logger.error("Tsy nahavita ny fangatahana OpenAI. Status: {}", response.getStatusCode());
      return "Nisy olana tamin'ny serivisy fanazavana.";

    } catch (RestClientException e) {
      logger.error(
          "Olana tamin'ny fifandraisana amin'ny OpenAI ho an'ny teny '{}': {}",
          word,
          e.getMessage());
      return "Tsy afaka nifandray tamin'ny serivisy fanazavana.";

    } catch (Exception e) {
      logger.error(
          "Olana tsy nampoizina tamin'ny fanazavana ny teny '{}': {}", word, e.getMessage());
      return "Nisy olana tamin'ny fanazavana ny teny.";
    }
  }
}
