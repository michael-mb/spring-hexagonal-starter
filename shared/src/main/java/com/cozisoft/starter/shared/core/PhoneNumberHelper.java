package com.cozisoft.starter.shared.core;

import com.cozisoft.starter.shared.core.model.db.PhoneNumber;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import lombok.experimental.UtilityClass;
import org.springframework.util.StringUtils;

@UtilityClass
public class PhoneNumberHelper {
    private static final PhoneNumberUtil PHONE_NUMBER_UTIL = PhoneNumberUtil.getInstance();

    public String formatPhoneNumber(PhoneNumber phoneNumber) {
        if (phoneNumber == null
                || !StringUtils.hasText(phoneNumber.getCountryCode())
                || !StringUtils.hasText(phoneNumber.getNationalNumber()))
            throw new IllegalArgumentException("Invalid phone number");

        Phonenumber.PhoneNumber parsed = new Phonenumber.PhoneNumber();
        parsed.setCountryCode(parseCountryCode(phoneNumber.getCountryCode()));
        parsed.setNationalNumber(parseNationalNumber(phoneNumber.getNationalNumber()));

        if (!PHONE_NUMBER_UTIL.isValidNumber(parsed))
            throw new IllegalArgumentException("Invalid phone number");

        return PHONE_NUMBER_UTIL.format(parsed, PhoneNumberUtil.PhoneNumberFormat.E164);
    }

    public PhoneNumber parsePhoneNumber(String phoneNumber) {
        if (!StringUtils.hasText(phoneNumber))
            throw new IllegalArgumentException("Invalid phone number");

        Phonenumber.PhoneNumber parsed;
        try {
            parsed = PHONE_NUMBER_UTIL.parse(phoneNumber, Phonenumber.PhoneNumber.CountryCodeSource.UNSPECIFIED.name());
        } catch (NumberParseException e) {
            throw new IllegalArgumentException("Invalid phone number", e);
        }

        if (!PHONE_NUMBER_UTIL.isValidNumber(parsed))
            throw new IllegalArgumentException("Invalid phone number");

        return PhoneNumber.builder()
                .countryCode("+" + parsed.getCountryCode())
                .nationalNumber(String.valueOf(parsed.getNationalNumber()))
                .build();
    }

    public Integer parseCountryCode(String countryCode) {
        if (countryCode.startsWith("+"))
            countryCode = countryCode.substring(1);
        return Integer.parseInt(countryCode);
    }

    public Long parseNationalNumber(String nationalNumber) {
        return Long.parseLong(nationalNumber.replaceAll("\\s+", ""));
    }

    public boolean isValid(String phoneNumber) {
        try {
            parsePhoneNumber(phoneNumber);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
