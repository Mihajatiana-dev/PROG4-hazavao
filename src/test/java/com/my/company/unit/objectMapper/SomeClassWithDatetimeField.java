package com.my.company.unit.objectMapper;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SomeClassWithDatetimeField {
  @JsonProperty("datetimeField")
  private Instant datetimeField;

  public String toJsonString() {
    return "{\"datetimeField\":\"" + this.datetimeField + "\"}";
  }
}
