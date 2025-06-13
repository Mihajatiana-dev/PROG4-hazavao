package com.my.company.endpoint.rest.controller;

import com.my.company.model.DefinitionResponse;
import com.my.company.service.OpenAiService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

@RestController
public class HazavaoController {

  private final OpenAiService openAiService;

  public HazavaoController(OpenAiService openAiService) {
    this.openAiService = openAiService;
  }

  @GetMapping("/hazavao")
  public ResponseEntity<DefinitionResponse> hazavao(@RequestParam String teny) {
    try {
      // Validation du paramètre d'entrée
      if (teny == null || teny.trim().isEmpty()) {
        return ResponseEntity.badRequest()
                .body(new DefinitionResponse("", "Tsy misy teny napetraka"));
      }

      String fanazavana = openAiService.getMalagasyDefinition(teny.trim());
      DefinitionResponse response = new DefinitionResponse(teny, fanazavana);

      return ResponseEntity.ok(response);

    } catch (Exception e) {
      // Gestion des erreurs
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body(new DefinitionResponse(teny, "Nisy olana tamin'ny fanazavana ny teny"));
    }
  }
}