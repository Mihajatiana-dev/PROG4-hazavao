package com.my.company.unit.objectMapper;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.my.company.conf.FacadeIT;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ObjectMapperTest extends FacadeIT {
  @Autowired ObjectMapper injectedBean;

  // Créez une nouvelle instance SANS le module Java Time pour le test
  ObjectMapper newInstanceWithoutJavaTimeModule = new ObjectMapper();

  SomeClassWithDatetimeField someClassWithDatetimeField =
      new SomeClassWithDatetimeField(Instant.now());

  @Test
  void new_instance_throws_on_java_datetime_module() {
    // Modifiez le format de la chaîne JSON pour correspondre au format attendu
    String jsonString =
        "{\"datetimeField\":\"" + someClassWithDatetimeField.getDatetimeField() + "\"}";

    assertThrows(
        InvalidDefinitionException.class,
        () ->
            newInstanceWithoutJavaTimeModule.readValue(
                jsonString, SomeClassWithDatetimeField.class));
  }

  @Test
  void injected_bean_handles_java_datetime_module() {
    // Utilisez le même format JSON cohérent
    String jsonString =
        "{\"datetimeField\":\"" + someClassWithDatetimeField.getDatetimeField() + "\"}";

    assertDoesNotThrow(() -> injectedBean.readValue(jsonString, SomeClassWithDatetimeField.class));
  }
}
