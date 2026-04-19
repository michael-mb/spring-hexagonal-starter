package com.cozisoft.starter.business.api.model;

import com.cozisoft.starter.business.config.CentralMapperConfiguration;
import io.vavr.control.Try;
import org.mapstruct.Mapper;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.NoSuchElementException;
import java.util.Objects;

@Mapper(config = CentralMapperConfiguration.class)
public interface CommonMapper {
    default OffsetDateTime map(ZonedDateTime value) {
        return Try.success(value)
                .filter(Objects::nonNull)
                .map(zdt -> OffsetDateTime.ofInstant(zdt.toInstant(), ZoneOffset.UTC))
                .recover(NoSuchElementException.class, exception -> null)
                .getOrNull();
    }

    default ZonedDateTime map(OffsetDateTime value) {
        return Try.success(value)
                .filter(Objects::nonNull)
                .map(odt -> odt.atZoneSameInstant(ZoneOffset.UTC))
                .recover(NoSuchElementException.class, exception -> null)
                .getOrNull();
    }
}
