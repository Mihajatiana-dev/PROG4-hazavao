package com.my.company.model;

public class DefinitionResponse {
  private String teny;
  private String fanazavana;

  public DefinitionResponse(String teny, String fanazavana) {
    this.teny = teny;
    this.fanazavana = fanazavana;
  }

  public String getTeny() {
    return teny;
  }

  public String getFanazavana() {
    return fanazavana;
  }
}


