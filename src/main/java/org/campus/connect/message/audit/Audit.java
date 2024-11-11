package org.campus.connect.message.audit;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.campus.connect.message.constants.SchemaConstants;
import org.campus.connect.message.constants.UtilsConstants;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Audit_tb", schema = SchemaConstants.MESSAGES)
public class Audit {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(columnDefinition = "UUID")
  private UUID id;

  @Column(name = "UPDATED_DATE", nullable = false)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonFormat(pattern = UtilsConstants.DATE_TIME_PATTERN)
  private LocalDateTime updated;

  @Column(name = "user_infos")
  private String user;

  @Column(name = "entity_name")
  private String entityName;

  @Column(name = "changed_data")
  private String changedData;

  @Column(name = "operation")
  private String operation;

  @Column(name = "entity_id")
  private UUID entityId;
}
