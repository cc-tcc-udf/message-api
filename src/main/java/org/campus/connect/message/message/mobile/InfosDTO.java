package org.campus.connect.message.message.mobile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class InfosDTO {
  private UUID id;
  private String email;
  private Number total;
  private Number favorites;
  private Number reads;
  private Number notReads;
}
