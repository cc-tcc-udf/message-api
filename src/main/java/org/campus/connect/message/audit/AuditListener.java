package org.campus.connect.message.audit;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;
import org.campus.connect.message.infra.auth.AuthUserService;
import org.springframework.data.annotation.Id;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

public class AuditListener {

  private final AuthUserService authUserService;
  private final AuditRepository auditRepository;

  public AuditListener(final AuthUserService authUserService, final AuditRepository auditRepository) {
    this.authUserService = authUserService;
    this.auditRepository = auditRepository;
  }

  @PrePersist
  public void onPrePersist(Object entity) {
    audit(entity, "CREATE");
  }

  @PreUpdate
  public void onPreUpdate(Object entity) {
    audit(entity, "UPDATE");
  }

  @PreRemove
  public void onPreRemove(Object entity) {
    audit(entity, "DELETE");
  }

  private void audit(Object entity, String operation) {
    Audit audit = new Audit();
    audit.setUser(authUserService.getCurrentUser());
    audit.setEntityName(entity.getClass().getSimpleName());
    audit.setChangedData(entity.toString());
    audit.setUpdated(LocalDateTime.now());
    audit.setOperation(operation);
    try {
      Object entityId = getEntityId(entity);
      if (entityId != null) {
        audit.setEntityId(entityId.toString()); // Supondo que o campo entityId em Audit é String
      }
    } catch(Exception e){

    }
    saveAudit(audit);
  }

  private void saveAudit(Audit audit) {
    this.auditRepository.saveAndFlush(audit);
  }

  private Object getEntityId(Object entity) throws IllegalAccessException {
    for (Field field : entity.getClass().getDeclaredFields()) {
      if (field.isAnnotationPresent(Id.class)) {
        field.setAccessible(true);
        return field.get(entity);
      }
    }
    return null;
  }
}
