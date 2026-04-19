package com.cozisoft.starter.shared.core.model.payload;

import lombok.Builder;

@Builder
public record DataIntegrityConstraintViolation(String property, String errorMessage) {
}
