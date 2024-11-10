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

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Audit_tb", schema = SchemaConstants.MESSAGES)
public class Audit {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "IDS_ENTITY_SEQ")
  @SequenceGenerator(name = "IDS_ENTITY_SEQ", sequenceName = "IDS_ENTITY_SEQ", allocationSize = 1)
  private Long id;

  @Column(name = "UPDATED_DATE", nullable = false)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonFormat(pattern = UtilsConstants.DATE_TIME_PATTERN)
  private LocalDateTime updated;

  private String user;

  @Column(name = "entity_name")
  private String entityName;

  @Column(name = "changed_data")
  private String changedData;

  @Column(name = "operation")
  private String operation;
}
