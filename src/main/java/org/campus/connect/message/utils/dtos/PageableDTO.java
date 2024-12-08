package org.campus.connect.message.utils.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class PageableDTO {
  private int first;
  private int rows;
  private String sortField;
  private int total;
  private int sortOrder;
  private String flag;
  private Object filters;
  private String globalFilter;
  private UUID objectId;
}