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
  @Column(name = "responsible_id")
  private UUID responsible;

//  @ManyToMany
//  @JoinTable(
//    name = "message_links",
//    schema = SchemaConstants.MESSAGES,
//    joinColumns = @JoinColumn(name = "message_id"),
//    inverseJoinColumns = @JoinColumn(name = "link_id")
//  )
//  private List<Links> links;
//
//  @ManyToMany
//  @JoinTable(
//    name = "message_attachments",
//    schema = SchemaConstants.MESSAGES,
//    joinColumns = @JoinColumn(name = "message_id"),
//    inverseJoinColumns = @JoinColumn(name = "file_id")
//  )
//  private List<File> attachments;
}

