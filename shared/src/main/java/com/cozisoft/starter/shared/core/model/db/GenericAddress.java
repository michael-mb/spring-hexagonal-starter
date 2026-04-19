package com.cozisoft.starter.shared.core.model.db;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class GenericAddress extends BaseEntity {

    public static final String COUNTRY_COLUMN = "country";
    public static final String STATE_COLUMN = "state";
    public static final String CITY_COLUMN = "city";
    public static final String ZIP_CODE_COLUMN = "zip_code";
    public static final String STREET_COLUMN = "street";

    @Column(name = COUNTRY_COLUMN, nullable = false)
    private String country;
    @Column(name = STATE_COLUMN)
    private String state;
    @Column(name = CITY_COLUMN)
    private String city;
    @Column(name = STREET_COLUMN)
    private String street;
    @Column(name = ZIP_CODE_COLUMN)
    private String zipCode;

    public boolean isEmpty() {
        return street == null && city == null && state == null && zipCode == null && country == null;
    }

    @Override
    public String toString() {
        return String.join("\n", nonNull(street), nonNull(zipCode + " " + city), nonNull(state), nonNull(country));
    }

    private String nonNull(String s) {
        return (s == null || s.isBlank()) ? "" : s;
    }
}
