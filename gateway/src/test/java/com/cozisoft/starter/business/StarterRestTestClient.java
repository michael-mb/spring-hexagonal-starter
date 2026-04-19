package com.cozisoft.starter.business;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import tools.jackson.databind.json.JsonMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@RequiredArgsConstructor
public class StarterRestTestClient {
    private final MockMvc mockMvc;
    private final JsonMapper objectMapper;

    public ResultActions createAccount(RequestPostProcessor auth) throws Exception {
        return mockMvc.perform(post("/accounts").with(auth));
    }

    public ResultActions whoami(RequestPostProcessor auth) throws Exception {
        return mockMvc.perform(get("/accounts/whoami").with(auth));
    }

    public ResultActions getAccountById(Long id, RequestPostProcessor auth) throws Exception {
        return mockMvc.perform(get("/accounts/{id}", id).with(auth));
    }

    public ResultActions patchAccountById(Long id, String body, RequestPostProcessor auth) throws Exception {
        return mockMvc.perform(patch("/accounts/{id}", id)
                .with(auth)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));
    }

    public ResultActions deleteAccountById(Long id, RequestPostProcessor auth) throws Exception {
        return mockMvc.perform(delete("/accounts/{id}", id).with(auth));
    }

    public ResultActions updateAccountStatus(Long id, Object body, RequestPostProcessor auth) throws Exception {
        return mockMvc.perform(put("/accounts/{id}/status", id)
                .with(auth)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)));
    }
}
