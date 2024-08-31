package org.campus.connect.message.message;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.campus.connect.message.constants.Enums.Status;
import org.campus.connect.message.constants.SchemaConstants;
import org.campus.connect.message.utils.AbstractEntity;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Message_tb", schema = SchemaConstants.MESSAGES)
public class Message extends AbstractEntity {
  private String title;
  private String summary;
  @Column(name = "send_date")
  private LocalDateTime sendDate;
  private Status status;
  private String message;
  private UUID response;
}

