package com.cozisoft.starter.shared.core.model.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public final class GenericAddressPayload {
    private String country;
    private String countryCode;
    private String state;
    private String city;
    private String street;
    private String zipCode;
}
