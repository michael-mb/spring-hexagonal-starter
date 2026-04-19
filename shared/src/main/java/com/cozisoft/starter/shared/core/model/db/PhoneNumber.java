package com.cozisoft.starter.shared.core.model.db;

import com.cozisoft.starter.shared.core.PhoneNumberHelper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhoneNumber {
    private String countryCode;
    private String nationalNumber;

    @Override
    public String toString() {
        return PhoneNumberHelper.formatPhoneNumber(this);
    }
}
