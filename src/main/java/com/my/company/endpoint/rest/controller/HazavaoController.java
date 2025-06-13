package com.my.company.endpoint.rest.controller;

import com.my.company.model.DefinitionResponse;
import com.my.company.service.OpenAiService;
import org.springframework.web.bind.annotation.*;

@RestController
public class HazavaoController {

  private final OpenAiService openAiService;

  public HazavaoController(OpenAiService openAiService) {
    this.openAiService = openAiService;
  }

  @GetMapping("/hazavao")
  public DefinitionResponse hazavao(@RequestParam String teny) {
    String fanazavana = openAiService.getMalagasyDefinition(teny);
    return new DefinitionResponse(teny, fanazavana);
  }
}

