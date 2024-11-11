package org.campus.connect.message.audit;

import jakarta.persistence.PostPersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;
import org.campus.connect.message.infra.auth.AuthUserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.annotation.Id;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class AuditListener {

  private final AuthUserService authUserService;
  private final ApplicationContext context;

  public AuditListener(@Lazy final AuthUserService authUserService, ApplicationContext context) {
    this.authUserService = authUserService;
    this.context = context;
  }

  @PostPersist
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
    if (entity instanceof Audit) {
      return;
    }
    Audit audit = new Audit();
    audit.setUser(authUserService.getCurrentUser());
    audit.setEntityName(entity.getClass().getSimpleName());
    audit.setChangedData(entity.toString());
    audit.setUpdated(LocalDateTime.now());
    audit.setOperation(operation);
    try {
      UUID entityId = getEntityId(entity);
      if (entityId != null) {
        audit.setEntityId(entityId);
      }
    } catch (Exception ignored) {
      audit.setEntityId(null);
    }
    saveAudit(audit);
  }

  private void saveAudit(Audit audit) {
    AuditRepository auditRepository = context.getBean(AuditRepository.class);
    auditRepository.saveAndFlush(audit);
  }

  private UUID getEntityId(Object entity) throws IllegalAccessException {
    for (Field field : entity.getClass().getDeclaredFields()) {
      if (field.isAnnotationPresent(Id.class)) {
        field.setAccessible(true);
        return (UUID) field.get(entity);
      }
    }
    return null;
  }
}
