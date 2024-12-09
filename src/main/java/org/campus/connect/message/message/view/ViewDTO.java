package org.campus.connect.message.message.view;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.campus.connect.message.message.MessageDTO;
import org.campus.connect.message.users.UsersDTO;
import org.campus.connect.message.utils.AbstractEntityDTO;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ViewDTO extends AbstractEntityDTO {
  private MessageDTO message;
  private UsersDTO user;
  private LocalDateTime viewDate;
  private boolean viewed;
  private boolean received;
  private boolean favorite;
  private String course;
}

