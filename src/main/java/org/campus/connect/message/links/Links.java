package org.campus.connect.message.links;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.campus.connect.message.constants.SchemaConstants;
import org.campus.connect.message.utils.AbstractEntity;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Links_tb", schema = SchemaConstants.MESSAGES)
public class Links extends AbstractEntity {
  private String titulo;
  private String link;
  private Long id_msg;
}
