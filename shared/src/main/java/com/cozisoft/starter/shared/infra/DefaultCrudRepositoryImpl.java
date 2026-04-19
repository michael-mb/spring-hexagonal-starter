package com.cozisoft.starter.shared.infra;

import com.cozisoft.starter.shared.core.exception.DataIntegrityViolationException;
import com.cozisoft.starter.shared.core.model.db.BaseEntity;
import com.cozisoft.starter.shared.core.model.payload.DatabaseColumnConstraintDefinition;
import com.cozisoft.starter.shared.core.port.out.DefaultCrudRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor(staticName = "of")
public class DefaultCrudRepositoryImpl<T extends BaseEntity> implements DefaultCrudRepository<T> {

    private final JpaRepository<T, Long> repository;
    private final Class<T> entityClass;

    @Override
    public T save(T entity) {
        try {
            return repository.save(entity);
        } catch (DataIntegrityViolationException exception) {
            if (exception.getCause() instanceof ConstraintViolationException constraintViolationException) {
                Set<DatabaseColumnConstraintDefinition> violatedConstraints =
                        entity.columnConstrains()
                                .stream()
                                .filter(c -> c.name().equals(constraintViolationException.getConstraintName()))
                                .collect(Collectors.toSet());
                throw DataIntegrityViolationException.fromDatabaseColumnConstraintDefinitions(violatedConstraints);
            }
            throw exception;
        }
    }

    @Override
    public Optional<T> findById(Long entityId) {
        return repository.findById(entityId);
    }

    @Override
    public List<T> findAll() {
        return repository.findAll()
                .stream()
                .filter(entity -> !entity.isDeleted())
                .toList();
    }

    @Override
    @Transactional
    public T deleteById(Long id, boolean hardDelete) {
        Optional<T> entity = findById(id);
        if (entity.isEmpty()) { return null; }

        T entityToDelete = entity.get();
        if (hardDelete) {
            repository.delete(entityToDelete);
            return entityToDelete;
        }

        entityToDelete.setDeleted(true);
        return save(entityToDelete);
    }
}
