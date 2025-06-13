package com.my.company.unit.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.my.company.service.OpenAiService;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
class OpenAiServiceTest {

  @Mock private RestTemplate restTemplate;

  @InjectMocks private OpenAiService openAiService;

  @BeforeEach
  void setUp() {
    // Injection des valeurs de configuration via reflection
    ReflectionTestUtils.setField(openAiService, "apiKey", "test-api-key");
    ReflectionTestUtils.setField(
        openAiService, "apiUrl", "https://api.openai.com/v1/chat/completions");
  }

  @Test
  void getMalagasyDefinition_Success() {
    // Given
    String word = "fitiavana";
    String expectedDefinition =
        "Fihetseham-po lalina sy mahery vaika izay mampiseho fiankohofana sy firaisan-kina amin'ny"
            + " olona iray na zavatra iray.";

    Map<String, Object> mockResponse = createMockOpenAiResponse(expectedDefinition);
    ResponseEntity<Map> responseEntity = new ResponseEntity<>(mockResponse, HttpStatus.OK);

    when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(Map.class)))
        .thenReturn(responseEntity);

    // When
    String result = openAiService.getMalagasyDefinition(word);

    // Then
    assertEquals(expectedDefinition, result);
    verify(restTemplate).postForEntity(anyString(), any(HttpEntity.class), eq(Map.class));
  }

  @Test
  void getMalagasyDefinition_EmptyResponse() {
    // Given
    String word = "teny";
    Map<String, Object> mockResponse = Map.of("choices", Collections.emptyList());
    ResponseEntity<Map> responseEntity = new ResponseEntity<>(mockResponse, HttpStatus.OK);

    when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(Map.class)))
        .thenReturn(responseEntity);

    // When
    String result = openAiService.getMalagasyDefinition(word);

    // Then
    assertEquals("Tsy afaka nahazo fanazavana marina.", result);
  }

  @Test
  void getMalagasyDefinition_RestClientException() {
    // Given
    String word = "teny";

    when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(Map.class)))
        .thenThrow(new RestClientException("Connection failed"));

    // When
    String result = openAiService.getMalagasyDefinition(word);

    // Then
    assertEquals("Tsy afaka nifandray tamin'ny serivisy fanazavana.", result);
  }

  @Test
  void getMalagasyDefinition_HttpError() {
    // Given
    String word = "teny";
    ResponseEntity<Map> responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

    when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(Map.class)))
        .thenReturn(responseEntity);

    // When
    String result = openAiService.getMalagasyDefinition(word);

    // Then
    assertEquals("Nisy olana tamin'ny serivisy fanazavana.", result);
  }

  private Map<String, Object> createMockOpenAiResponse(String content) {
    Map<String, Object> message = Map.of("content", content);
    Map<String, Object> choice = Map.of("message", message);
    List<Map<String, Object>> choices = List.of(choice);
    return Map.of("choices", choices);
  }
}
