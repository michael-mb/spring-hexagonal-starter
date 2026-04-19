package com.cozisoft.starter.business;

import com.cozisoft.starter.account.core.port.out.IdPUserManagementAdapter;
import com.cozisoft.starter.shared.test.BaseApiTest;
import com.cozisoft.starter.shared.test.DbResetService;
import com.cozisoft.starter.shared.test.ServiceModuleTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@Testcontainers
@ServiceModuleTest
@Import(CoreTestConfiguration.class)
public abstract class GatewayBaseApiTest extends BaseApiTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
            .withInitScript("init-test.sql");

    @MockitoBean
    protected IdPUserManagementAdapter idpUserManagementAdapter;

    @Autowired
    protected DbResetService dbResetService;

    @Autowired
    protected StarterRestTestClient client;

    @Autowired
    protected FixtureProvider fixtures;

    protected static final String ADMIN_SUB = "test-admin-sub";
    protected static final String USER_SUB = "test-user-sub";

    @BeforeEach
    void setupMocks() {
        when(idpUserManagementAdapter.getUserById(ADMIN_SUB)).thenReturn(Optional.empty());
        when(idpUserManagementAdapter.getUserById(USER_SUB)).thenReturn(Optional.empty());
        doNothing().when(idpUserManagementAdapter).patchUser(any());
    }

    @AfterEach
    void resetDb() {
        dbResetService.resetAll();
    }

    protected RequestPostProcessor asAdmin() {
        return withAuth(ADMIN_SUB, "ADMIN");
    }

    protected RequestPostProcessor asUser() {
        return withAuth(USER_SUB, "USER");
    }
}
