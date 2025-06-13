package com.my.company.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class DefinitionResponse {

  @JsonProperty("teny")
  private String teny;

  @JsonProperty("fanazavana")
  private String fanazavana;

  // Constructeur par défaut pour Jackson
  public DefinitionResponse() {}

  public DefinitionResponse(String teny, String fanazavana) {
    this.teny = teny;
    this.fanazavana = fanazavana;
  }

  public String getTeny() {
    return teny;
  }

  public void setTeny(String teny) {
    this.teny = teny;
  }

  public String getFanazavana() {
    return fanazavana;
  }

  public void setFanazavana(String fanazavana) {
    this.fanazavana = fanazavana;
  }

  @Override
  public String toString() {
    return String.format("DefinitionResponse{teny='%s', fanazavana='%s'}", teny, fanazavana);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;

    DefinitionResponse that = (DefinitionResponse) obj;
    return Objects.equals(teny, that.teny) && Objects.equals(fanazavana, that.fanazavana);
  }

  @Override
  public int hashCode() {
    return Objects.hash(teny, fanazavana);
  }
}