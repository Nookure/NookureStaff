package com.nookure.staff.service;

import com.nookure.staff.api.config.common.PluginMessageConfig;
import com.nookure.staff.api.exception.DataIntegrityCheckFailedException;
import com.nookure.staff.api.service.EncryptService;
import java.util.Arrays;
import java.util.ServiceLoader;
import javax.crypto.SecretKey;
import org.junit.jupiter.api.Test;

public class AESGCMEncryptServiceTest {
    private final EncryptService service =
            ServiceLoader.load(EncryptService.class).findFirst().orElseThrow();

    private final PluginMessageConfig config = new PluginMessageConfig();
    private final SecretKey key = config.getSecretKey();

    @Test
    public void testEncrypt() {
        final byte[] data = "Hello, World!".getBytes();
        final byte[] encrypted = service.encrypt(data, key);
        final byte[] decrypted = service.decrypt(encrypted, key);

        assert Arrays.equals(data, decrypted);
    }

    @Test
    public void testEncryptModifyBeforeDecrypt() {
        final byte[] data = "Hello, World!".getBytes();
        final byte[] encrypted = service.encrypt(data, key);
        encrypted[0] = 0;

        try {
            service.decrypt(encrypted, key);
        } catch (DataIntegrityCheckFailedException e) {
            return;
        }

        throw new AssertionError("DataIntegrityCheckFailedException was not thrown");
    }
}
