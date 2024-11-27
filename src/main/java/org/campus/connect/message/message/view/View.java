package org.campus.connect.message.message.view;

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
import org.campus.connect.message.message.Message;
import org.campus.connect.message.users.Users;
import org.campus.connect.message.utils.AbstractEntity;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Message_view_tb", schema = SchemaConstants.MESSAGES)
public class View extends AbstractEntity {
  @ManyToOne
  @JoinColumn(name = "message_id", nullable = false)
  private Message message;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private Users user;

  @Column(name = "view_date")
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonFormat(pattern = UtilsConstants.DATE_TIME_PATTERN)
  private LocalDateTime viewDate;

  @Column(name = "viewed")
  private boolean viewed;

  @Column(name = "received_date")
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonFormat(pattern = UtilsConstants.DATE_TIME_PATTERN)
  private LocalDateTime receivedDate;

  @Column(name = "received")
  private boolean received;

  @Column(name = "favorite")
  private boolean favorite;
}
