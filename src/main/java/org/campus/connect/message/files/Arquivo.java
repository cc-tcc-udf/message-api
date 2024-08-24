package org.campus.connect.message.files;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.campus.connect.message.constants.SchemaConstants;
import org.campus.connect.message.utils.AbstractEntity;

import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "Arquivo_tb", schema = SchemaConstants.MESSAGES)
public class Arquivo extends AbstractEntity {
  private String name;
  private String type;
  private String url;
  private Long size;
  private String key;
  private UUID uid;
  private Long id_ext;
}