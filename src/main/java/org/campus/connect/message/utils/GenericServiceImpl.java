package org.campus.connect.message.utils;

import org.campus.connect.message.infra.auth.AuthUserService;
import org.campus.connect.message.infra.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public abstract class GenericServiceImpl<E extends AbstractEntity, D extends AbstractEntityDTO> implements GenericService<D> {

  private final JpaRepository<E, UUID> repository;
  private final EntityMapper<D, E> mapper;
  @Autowired
  private AuthUserService authUserService;

  public GenericServiceImpl(
    JpaRepository<E, UUID> repository,
    EntityMapper<D, E> mapper
  ) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Override
  public D save(D dto) throws Exception {
    return executeSave(dto);
  }

  @Override
  public Optional<D> findOneById(UUID id) {
    return id == null ? Optional.empty() : repository.findById(id)
      .filter(entity -> !Boolean.TRUE.equals(entity.getExcluded()))
      .map(mapper::toDto);
  }

  @Override
  public void delete(UUID id) throws Exception {
    Optional<D> optional = findOneById(id);
    if (optional.isPresent()) {
      D dto = optional.get();
      dto.setExcluded(true);
      executeSave(dto);
    }
  }

  protected D executeSave(D dto) throws Exception {
    E entity = mapper.toEntity(dto);

    if (dto.getId() == null) {
      setAuditFieldsOnCreate(entity);
    } else {
      Optional<D> existingDto = findOneById(dto.getId());
      if (existingDto.isEmpty()) {
        throw new ResourceNotFoundException("Registro não encontrado com ID: " + dto.getId());
      }
      setAuditFieldsOnUpdate(entity, existingDto.get());
    }

    repository.saveAndFlush(entity);
    return mapper.toDto(entity);
  }

  private void setAuditFieldsOnCreate(E entity) {
    String currentUserJson = this.authUserService.getCurrentUser();
    entity.setCreatedBy(currentUserJson);
    entity.setCreated(LocalDateTime.now());
    entity.setUpdatedBy(currentUserJson);
    entity.setUpdated(LocalDateTime.now());
  }

  private void setAuditFieldsOnUpdate(E entity, D existingDto) {
    String currentUserJson = this.authUserService.getCurrentUser();
    entity.setCreated(existingDto.getCreated());
    entity.setCreatedBy(existingDto.getCreatedBy());
    entity.setUpdated(LocalDateTime.now());
    entity.setUpdatedBy(currentUserJson);
  }

  @Override
  public List<D> diffBetweenBasedOnId(List<D> listA, List<D> listB) {
    if (listA == null || listA.isEmpty()) return listB != null ? listB : new ArrayList<>();
    if (listB == null) return new ArrayList<>();

    return listA.stream()
      .filter(candidate -> isPresentInList(candidate, listB))
      .collect(Collectors.toList());
  }

  private boolean isPresentInList(D candidate, List<D> comparisonList) {
    return candidate != null && candidate.getId() != null && comparisonList.stream()
      .anyMatch(compared -> candidate.getId().equals(compared.getId()));
  }

  // Abstract method that subclasses must implement
  public abstract List<D> findAll();
}
