package com.nookure.staff.api.config.common;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class PluginMessageConfig {
    @Setting
    public boolean enabled = false;

    @Setting
    @Comment("""
      Base64-encoded encryption key for plugin messages, generated if not present, must be 128, 192, or 256 bits
      You should have the same key on all servers in the network
      """)
    public String encryptionKey;

    @Setting
    @Comment("""
      It's a security feature to prevent players from tampering with the plugin messages
      if the plugin detects tampering, it will disconnect the player
      """)
    public boolean playerTamperingDetection = true;

    @Setting
    @Comment("""
      The message to send to the player when tampering is detected
      """)
    public String playerTamperingDetectionMessage = "<red>Plugin message tampering detected!";

    public PluginMessageConfig() {
        ensureEncryptionKey(256);
    }

    /**
     * Generates a new encryption key with the specified length.
     *
     * @param length Key length in bits (must be 128, 192, or 256)
     * @return Base64-encoded encryption key
     */
    private String generateEncryptionKey(final int length) {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(length);
            SecretKey secretKey = keyGen.generateKey();
            return Base64.getEncoder().encodeToString(secretKey.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("AES algorithm is not available", e);
        }
    }

    /**
     * Retrieves the SecretKey from the stored encryption key.
     *
     * @return A SecretKey instance
     */
    public SecretKey getSecretKey() {
        if (encryptionKey == null || encryptionKey.isEmpty()) {
            throw new IllegalStateException("Encryption key is not set");
        }

        byte[] decodedKey = Base64.getDecoder().decode(encryptionKey);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
    }

    /**
     * Generates and sets a new encryption key if none is present.
     */
    public void ensureEncryptionKey(final int length) {
        if (encryptionKey == null || encryptionKey.isEmpty()) {
            encryptionKey = generateEncryptionKey(length);
        }
    }
}
