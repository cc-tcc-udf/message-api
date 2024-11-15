package org.campus.connect.message.audit;

import jakarta.persistence.PostPersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class AuditListener {

  private final ApplicationEventPublisher publisher;

  public AuditListener(final ApplicationEventPublisher publisher) {
    this.publisher = publisher;
  }

  @PostPersist
  public void postPersist(Object entity) {
    publisher.publishEvent(new AuditEntityEvent(entity, "CREATE"));
  }

  @PreUpdate
  public void preUpdate(Object entity) {
    publisher.publishEvent(new AuditEntityEvent(entity, "UPDATE"));
  }

  @PreRemove
  public void preRemove(Object entity) {
    publisher.publishEvent(new AuditEntityEvent(entity, "DELETE"));
  }
}
