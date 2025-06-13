package com.my.company.endpoint.rest.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.my.company.model.DefinitionResponse;
import com.my.company.service.OpenAiService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class HazavaoControllerTest {

  @Mock private OpenAiService openAiService;

  @InjectMocks private HazavaoController hazavaoController;

  @Test
  void hazavao_Success() {
    // Given
    String word = "fitiavana";
    String definition = "Fihetseham-po lalina sy mahery vaika";

    when(openAiService.getMalagasyDefinition(word)).thenReturn(definition);

    // When
    ResponseEntity<DefinitionResponse> response = hazavaoController.hazavao(word);

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(word, response.getBody().getTeny());
    assertEquals(definition, response.getBody().getFanazavana());

    verify(openAiService).getMalagasyDefinition(word);
  }

  @Test
  void hazavao_EmptyWord() {
    // When
    ResponseEntity<DefinitionResponse> response = hazavaoController.hazavao("");

    // Then
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("", response.getBody().getTeny());
    assertEquals("Tsy misy teny napetraka", response.getBody().getFanazavana());

    verifyNoInteractions(openAiService);
  }

  @Test
  void hazavao_NullWord() {
    // When
    ResponseEntity<DefinitionResponse> response = hazavaoController.hazavao(null);

    // Then
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("", response.getBody().getTeny());
    assertEquals("Tsy misy teny napetraka", response.getBody().getFanazavana());

    verifyNoInteractions(openAiService);
  }

  @Test
  void hazavao_WhitespaceOnly() {
    // When
    ResponseEntity<DefinitionResponse> response = hazavaoController.hazavao("   ");

    // Then
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("", response.getBody().getTeny());
    assertEquals("Tsy misy teny napetraka", response.getBody().getFanazavana());

    verifyNoInteractions(openAiService);
  }

  @Test
  void hazavao_ServiceException() {
    // Given
    String word = "fitiavana";

    when(openAiService.getMalagasyDefinition(word))
        .thenThrow(new RuntimeException("Service error"));

    // When
    ResponseEntity<DefinitionResponse> response = hazavaoController.hazavao(word);

    // Then
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(word, response.getBody().getTeny());
    assertEquals("Nisy olana tamin'ny fanazavana ny teny", response.getBody().getFanazavana());

    verify(openAiService).getMalagasyDefinition(word);
  }

  @Test
  void hazavao_WordWithWhitespace() {
    // Given
    String wordWithWhitespace = "  fitiavana  ";
    String trimmedWord = "fitiavana";
    String definition = "Fihetseham-po lalina sy mahery vaika";

    when(openAiService.getMalagasyDefinition(trimmedWord)).thenReturn(definition);

    // When
    ResponseEntity<DefinitionResponse> response = hazavaoController.hazavao(wordWithWhitespace);

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(wordWithWhitespace, response.getBody().getTeny()); // Original word returned
    assertEquals(definition, response.getBody().getFanazavana());

    verify(openAiService).getMalagasyDefinition(trimmedWord); // Trimmed word sent to service
  }
}
