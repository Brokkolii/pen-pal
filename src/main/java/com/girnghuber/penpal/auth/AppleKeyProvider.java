package com.girnghuber.penpal.auth;

import com.auth0.jwt.interfaces.RSAKeyProvider;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.HashMap;
import java.util.Map;

public class AppleKeyProvider implements RSAKeyProvider {

    private static final String APPLE_JWKS_URL = "https://appleid.apple.com/auth/keys";
    private final Map<String, RSAPublicKey> cachedKeys;

    public AppleKeyProvider() {
        this.cachedKeys = fetchApplePublicKeys();
    }

    @Override
    public RSAPublicKey getPublicKeyById(String kid) {
        return cachedKeys.get(kid);
    }

    @Override
    public RSAPrivateKey getPrivateKey() {
        return null;
    }

    @Override
    public String getPrivateKeyId() {
        return "";
    }

    private Map<String, RSAPublicKey> fetchApplePublicKeys() {
        Map<String, RSAPublicKey> keyMap = new HashMap<>();
        try {
            JsonNode keysNode = getKeysJson();

            // Parse each key and convert to RSAPublicKey
            for (JsonNode keyNode : keysNode) {
                String kid = keyNode.get("kid").asText();
                String n = keyNode.get("n").asText(); // Modulus
                String e = keyNode.get("e").asText(); // Exponent

                // Convert modulus and exponent from base64 to BigInteger
                BigInteger modulus = new BigInteger(1, java.util.Base64.getUrlDecoder().decode(n));
                BigInteger exponent = new BigInteger(1, java.util.Base64.getUrlDecoder().decode(e));

                // Create RSA Public Key
                RSAPublicKeySpec spec = new RSAPublicKeySpec(modulus, exponent);
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                RSAPublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(spec);

                // Cache the key by its kid
                keyMap.put(kid, publicKey);
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch Apple public keys", e);
        }

        return keyMap;
    }

    private static JsonNode getKeysJson() throws URISyntaxException, IOException {
        URI uri = new URI(APPLE_JWKS_URL);
        URL url = uri.toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        // Read the response into InputStream
        InputStream inputStream = connection.getInputStream();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jwksNode = objectMapper.readTree(inputStream);

        return jwksNode.get("keys");
    }
}
