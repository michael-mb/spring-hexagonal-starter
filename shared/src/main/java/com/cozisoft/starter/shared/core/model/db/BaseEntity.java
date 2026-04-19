package com.cozisoft.starter.shared.core.model.db;

import com.cozisoft.starter.shared.core.EqualsAndHashCodeUtils;
import com.cozisoft.starter.shared.core.model.payload.DatabaseColumnConstraintDefinition;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Set;

@Getter
@Setter
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity implements Serializable {
    public static final String ID_ATTRIBUTE = "id";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected Long id;

    @Setter
    @Builder.Default
    @Column(name = "deleted")
    protected boolean deleted = false;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    protected ZonedDateTime createdAt;

    @Setter
    @UpdateTimestamp
    @Column(name = "updated_at")
    protected ZonedDateTime updatedAt;

    @Override
    @SuppressWarnings("com.haulmont.jpb.EqualsDoesntCheckParameterClass")
    public final boolean equals(Object object) {
        return EqualsAndHashCodeUtils.checkEquality(this, object);
    }

    @Override
    public final int hashCode() {
        return EqualsAndHashCodeUtils.getHashCode(this);
    }

    public Set<DatabaseColumnConstraintDefinition> columnConstrains() {
        return Set.of();
    }
}
