package org.campus.connect.message.audit;

import jakarta.transaction.Transactional;
import org.campus.connect.message.infra.auth.AuthUserService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDateTime;

@Component
public class AuditEventListener {
  private final AuthUserService authUserService;
  private final AuditRepository auditRepository;

  public AuditEventListener(AuthUserService authUserService, AuditRepository auditRepository) {
    this.authUserService = authUserService;
    this.auditRepository = auditRepository;
  }

  @Transactional
  @TransactionalEventListener
  public void handleAuditEvent(AuditEntityEvent event) {
    Object entity = event.getEntity();
    String operation = event.getOperation();

    if (entity instanceof Audit) {
      return;
    }

    Audit audit = new Audit();
    audit.setUser(authUserService.getCurrentUser());
    audit.setEntityName(entity.getClass().getSimpleName());
    audit.setChangedData(entity.toString());
    audit.setUpdated(LocalDateTime.now());
    audit.setOperation(operation);
    auditRepository.saveAndFlush(audit);
  }


//  private UUID getEntityId(Object entity) throws IllegalAccessException {
//    for (Field field : entity.getClass().getDeclaredFields()) {
//      if (field.isAnnotationPresent(Id.class)) {
//        field.setAccessible(true);
//        return (UUID) field.get(entity);
//      }
//    }
//    return null;
//  }
}
