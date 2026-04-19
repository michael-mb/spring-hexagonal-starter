package com.cozisoft.starter.shared.test;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class RequestParameters {
    private final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

    private RequestParameters() {}

    public static RequestParameters of() {
        return new RequestParameters();
    }

    public RequestParameters add(String key, Object value) {
        if (value != null) {
            params.add(key, value.toString());
        }
        return this;
    }

    public MultiValueMap<String, String> build() {
        return params;
    }
}
