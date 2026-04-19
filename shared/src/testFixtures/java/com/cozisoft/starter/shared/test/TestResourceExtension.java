package com.cozisoft.starter.shared.test;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class TestResourceExtension implements BeforeAllCallback {

    private static final WireMockServer wireMockServer;
    private static final RSAKey rsaKey;
    private static final String issuerUrl;

    static {
        try {
            wireMockServer = new WireMockServer(WireMockConfiguration.options().dynamicPort());
            wireMockServer.start();

            rsaKey = new RSAKeyGenerator(2048)
                    .keyUse(KeyUse.SIGNATURE)
                    .algorithm(JWSAlgorithm.RS256)
                    .keyID(UUID.randomUUID().toString())
                    .generate();

            issuerUrl = "http://localhost:" + wireMockServer.port() + "/realms/test";

            System.setProperty("test.jwks.uri",
                    "http://localhost:" + wireMockServer.port() + "/.well-known/jwks.json");
            System.setProperty("test.issuer", issuerUrl);

            setupStubs();
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize test resources", e);
        }
    }

    private static void setupStubs() throws Exception {
        String jwksBody = "{\"keys\":[" + rsaKey.toPublicJWK().toJSONString() + "]}";

        wireMockServer.stubFor(get(urlEqualTo("/.well-known/jwks.json"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(jwksBody)));

        String oidcConfig = "{"
                + "\"issuer\":\"" + issuerUrl + "\","
                + "\"authorization_endpoint\":\"" + issuerUrl + "/protocol/openid-connect/auth\","
                + "\"token_endpoint\":\"" + issuerUrl + "/protocol/openid-connect/token\","
                + "\"jwks_uri\":\"http://localhost:" + wireMockServer.port() + "/.well-known/jwks.json\""
                + "}";

        wireMockServer.stubFor(get(urlPathMatching(".*\\.well-known/openid-configuration"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(oidcConfig)));
    }

    @Override
    public void beforeAll(ExtensionContext context) {
        // initialization handled in static block
    }

    public static RSAKey getRsaKey() {
        return rsaKey;
    }

    public static String getIssuer() {
        return issuerUrl;
    }
}
