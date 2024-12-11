package org.campus.connect.message.utils.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class PageableDTO {
  private int first;
  private int rows;
  private String sortField;
  private int sortOrder;
  private String globalFilter;
  private Map<String, FilterDTO> filters;
  private int total;
  private String flag;
  private UUID objectId;
}