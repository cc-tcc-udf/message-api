package org.campus.connect.message.utils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;
import org.campus.connect.message.constants.UtilsConstants;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@ToString
@MappedSuperclass
public class AbstractEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(columnDefinition = "UUID")
  private UUID id;

  @NotNull
  @Column(name = "CREATED_BY", nullable = false)
  private String createdBy;

  @Column(name = "UPDATED_BY", nullable = false)
  private String updatedBy;

  @Column(name = "CREATED_DATE", nullable = false)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonFormat(pattern = UtilsConstants.DATE_TIME_PATTERN)
  private LocalDateTime created;

  @Column(name = "UPDATED_DATE", nullable = false)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonFormat(pattern = UtilsConstants.DATE_TIME_PATTERN)
  private LocalDateTime updated;

  @NotNull
  @Column(name = "EXCLUDED")
  private Boolean excluded = false;

}
