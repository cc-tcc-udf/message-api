package org.campus.connect.message.message;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.campus.connect.message.constants.Enums.Status;
import org.campus.connect.message.constants.SchemaConstants;
import org.campus.connect.message.files.File;
import org.campus.connect.message.links.Links;
import org.campus.connect.message.utils.AbstractEntity;

import java.time.LocalDateTime;
import java.util.List;
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
  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
    name = "message_links",
    schema = SchemaConstants.MESSAGES,
    joinColumns = @JoinColumn(name = "message_id"),
    inverseJoinColumns = @JoinColumn(name = "link_id")
  )
  private List<Links> links;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
    name = "message_attachments",
    schema = SchemaConstants.MESSAGES,
    joinColumns = @JoinColumn(name = "message_id"),
    inverseJoinColumns = @JoinColumn(name = "file_id")
  )
  private List<File> attachments;

}

