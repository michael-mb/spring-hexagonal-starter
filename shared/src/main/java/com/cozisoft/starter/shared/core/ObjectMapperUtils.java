package com.cozisoft.starter.shared.core;

import lombok.experimental.UtilityClass;
import tools.jackson.databind.ObjectMapper;

@UtilityClass
public class ObjectMapperUtils {

    public ObjectMapper createObjectMapper() {
        return new ObjectMapper();
    }
}
