package com.cozisoft.starter.shared.core;

import com.cozisoft.starter.shared.core.model.db.BaseEntity;
import lombok.experimental.UtilityClass;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@UtilityClass
public class EqualsAndHashCodeUtils {

    @SuppressWarnings("unchecked")
    public <T extends BaseEntity> boolean checkEquality(T ref, Object object) {
        if (ref == object) return true;
        if (object == null) return false;
        Class<?> oEffectiveClass = object instanceof HibernateProxy hibernateProxy ? hibernateProxy.getHibernateLazyInitializer().getPersistentClass() : object.getClass();
        Class<?> thisEffectiveClass = ref instanceof HibernateProxy hibernateProxy ? hibernateProxy.getHibernateLazyInitializer().getPersistentClass() : ref.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        T other = (T) object;
        return ref.getId() != null && Objects.equals(ref.getId(), other.getId());
    }

    public <T extends BaseEntity> int getHashCode(T ref) {
        return ref instanceof HibernateProxy hibernateProxy ? hibernateProxy.getHibernateLazyInitializer().getPersistentClass().hashCode() : ref.getClass().hashCode();
    }
}
