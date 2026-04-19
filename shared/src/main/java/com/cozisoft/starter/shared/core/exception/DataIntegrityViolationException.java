package com.cozisoft.starter.shared.core.exception;

import com.cozisoft.starter.shared.core.model.payload.DataIntegrityConstraintViolation;
import com.cozisoft.starter.shared.core.model.payload.DatabaseColumnConstraintDefinition;
import lombok.Getter;
import lombok.experimental.StandardException;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Getter
@StandardException
public class DataIntegrityViolationException extends RuntimeException {
    private Set<DataIntegrityConstraintViolation> violations;

    private DataIntegrityViolationException(Set<DataIntegrityConstraintViolation> violations) {
        this.violations = violations;
    }

    public static DataIntegrityViolationException fromDatabaseColumnConstraintDefinitions(Set<DatabaseColumnConstraintDefinition> violations) {
        Objects.requireNonNull(violations, "violations must not be null");
        return Optional.of(violations)
                .filter(v -> !v.isEmpty())
                .map(v -> v.stream()
                        .map(def -> DataIntegrityConstraintViolation.builder()
                                .property(def.attributeName())
                                .errorMessage(def.description())
                                .build())
                        .collect(java.util.stream.Collectors.toSet())
                )
                .map(DataIntegrityViolationException::new)
                .orElseThrow(() -> new IllegalArgumentException("violations must not be empty"));
    }

    public static DataIntegrityViolationException from(Set<DataIntegrityConstraintViolation> violations) {
        return new DataIntegrityViolationException(violations);
    }
}
