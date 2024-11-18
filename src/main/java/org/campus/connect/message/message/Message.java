package org.campus.connect.message.message;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.campus.connect.message.constants.Enums.Status;
import org.campus.connect.message.constants.SchemaConstants;
import org.campus.connect.message.constants.UtilsConstants;
import org.campus.connect.message.course.Course;
import org.campus.connect.message.files.File;
import org.campus.connect.message.links.Links;
import org.campus.connect.message.message.view.View;
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
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonFormat(pattern = UtilsConstants.DATE_TIME_PATTERN)
  private LocalDateTime sendDate;
  @Enumerated(EnumType.STRING)
  private Status status;
  private String message;
  @Column(name = "responsible_id")
  private UUID responsible;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
    name = "message_courses_tb",
    schema = SchemaConstants.MESSAGES,
    joinColumns = @JoinColumn(name = "message_id"),
    inverseJoinColumns = @JoinColumn(name = "course_id")
  )
  private List<Course> courses;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
    name = "message_links_tb",
    schema = SchemaConstants.MESSAGES,
    joinColumns = @JoinColumn(name = "message_id"),
    inverseJoinColumns = @JoinColumn(name = "link_id")
  )
  private List<Links> links;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
    name = "message_attachments_tb",
    schema = SchemaConstants.MESSAGES,
    joinColumns = @JoinColumn(name = "message_id"),
    inverseJoinColumns = @JoinColumn(name = "file_id")
  )
  private List<File> attachments;

  @OneToMany(mappedBy = "message", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<View> views;
}

