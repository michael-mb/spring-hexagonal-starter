package com.cozisoft.starter.shared.core.model.payload;

import com.cozisoft.starter.shared.core.model.db.DatabaseColumnConstraintKind;
import lombok.Builder;

@Builder
public record DatabaseColumnConstraintDefinition(
        String name,
        String attributeName,
        String description,
        DatabaseColumnConstraintKind kind
) {
}
