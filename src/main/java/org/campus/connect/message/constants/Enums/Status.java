package org.campus.connect.message.constants.Enums;

import lombok.Getter;

@Getter
public enum Status {
  ENVIADO("Enviado"),
  NAO_ENVIADO("Não enviado"),
  RECEBIDO("Recebido"),
  VISUALIZADO("Visualizado"),
  REMOVIDA("Removida");
  private final String description;
  Status(final String description) {
    this.description = description;
  }

}
