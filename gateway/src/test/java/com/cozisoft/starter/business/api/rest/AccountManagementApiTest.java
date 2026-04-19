package com.cozisoft.starter.business.api.rest;

import com.cozisoft.starter.business.GatewayBaseApiTest;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AccountManagementApiTest extends GatewayBaseApiTest {

    @Test
    void registerAccount_shouldReturn201() throws Exception {
        client.createAccount(asUser())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber());
    }

    @Test
    void whoami_shouldReturn200() throws Exception {
        client.createAccount(asUser());

        client.whoami(asUser())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber());
    }

    @Test
    void getAccountById_shouldReturn200() throws Exception {
        var result = client.createAccount(asUser()).andReturn();
        long id = objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asLong();

        client.getAccountById(id, asUser())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
    }

    @Test
    void patchAccountById_asOwner_shouldReturn200() throws Exception {
        var result = client.createAccount(asUser()).andReturn();
        long id = objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asLong();

        client.patchAccountById(id, fixtures.loadAsString("account/patchAccountRequest.json"), asUser())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    void patchAccountById_asNonOwner_shouldReturn403() throws Exception {
        var result = client.createAccount(asUser()).andReturn();
        long id = objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asLong();

        client.patchAccountById(id, fixtures.loadAsString("account/patchAccountRequest.json"), asAdmin())
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteAccountById_asAdmin_shouldReturn204() throws Exception {
        var result = client.createAccount(asUser()).andReturn();
        long id = objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asLong();

        client.deleteAccountById(id, asAdmin())
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteAccountById_asUser_shouldReturn403() throws Exception {
        var result = client.createAccount(asUser()).andReturn();
        long id = objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asLong();

        client.deleteAccountById(id, asUser())
                .andExpect(status().isForbidden());
    }

    @Test
    void updateAccountStatus_asAdmin_shouldReturn204() throws Exception {
        var result = client.createAccount(asUser()).andReturn();
        long id = objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asLong();

        client.updateAccountStatus(id, Map.of("action", "suspend"), asAdmin())
                .andExpect(status().isNoContent());
    }

    @Test
    void updateAccountStatus_asUser_shouldReturn403() throws Exception {
        var result = client.createAccount(asUser()).andReturn();
        long id = objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asLong();

        client.updateAccountStatus(id, Map.of("action", "suspend"), asUser())
                .andExpect(status().isForbidden());
    }
}
