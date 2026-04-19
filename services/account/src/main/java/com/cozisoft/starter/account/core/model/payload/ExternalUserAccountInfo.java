package com.cozisoft.starter.account.core.model.payload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExternalUserAccountInfo {
    private String sub;
    private String givenName;
    private String familyName;
    private String nickname;
    private String name;
    private String picture;
    private String email;
    private boolean blocked;
    private String phoneNumber;
    private boolean emailVerified;
    private boolean phoneNumberVerified;
    private String locale;
    private Map<String, Object> userMetadata;
    private Map<String, Object> appMetadata;

    @Builder.Default
    private Set<String> roles = new HashSet<>();
    @Builder.Default
    private Set<String> permissions = Set.of();
}
