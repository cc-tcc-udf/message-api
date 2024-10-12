package org.campus.connect.message.message;

import jakarta.persistence.*;
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
  @Enumerated(EnumType.STRING)
  private Status status;
  private String message;
  @Column(name = "responsible_id")
  private UUID responsible;
}

