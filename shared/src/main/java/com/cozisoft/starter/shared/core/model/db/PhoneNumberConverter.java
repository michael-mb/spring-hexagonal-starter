package com.cozisoft.starter.shared.core.model.db;

import com.cozisoft.starter.shared.core.PhoneNumberHelper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.util.StringUtils;

import java.util.NoSuchElementException;

@Converter
public class PhoneNumberConverter implements AttributeConverter<PhoneNumber, String> {

    @Override
    public String convertToDatabaseColumn(PhoneNumber attribute) {
        if (attribute == null) return null;
        if (!StringUtils.hasText(attribute.getCountryCode()) || !StringUtils.hasText(attribute.getNationalNumber()))
            throw new IllegalArgumentException("'countryCode' and 'number' of a phone number must not be blank or empty");
        try {
            return PhoneNumberHelper.formatPhoneNumber(attribute);
        } catch (NoSuchElementException e) {
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert phone number to database column", e);
        }
    }

    @Override
    public PhoneNumber convertToEntityAttribute(String dbData) {
        if (!StringUtils.hasText(dbData)) return null;
        try {
            return PhoneNumberHelper.parsePhoneNumber(dbData);
        } catch (IllegalArgumentException e) {
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert phone number to entity attribute", e);
        }
    }
}
