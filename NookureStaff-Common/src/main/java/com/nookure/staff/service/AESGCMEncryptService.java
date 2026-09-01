package com.nookure.staff.service;

import com.google.auto.service.AutoService;
import com.google.inject.Singleton;
import com.nookure.staff.api.exception.DataIntegrityCheckFailedException;
import com.nookure.staff.api.service.EncryptService;
import java.security.SecureRandom;
import java.util.Arrays;
import javax.crypto.AEADBadTagException;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import org.jetbrains.annotations.NotNull;

@Singleton
@AutoService(EncryptService.class)
public class AESGCMEncryptService implements EncryptService {

    private static final String ENCRYPTION_ALGORITHM = "AES/GCM/NoPadding";
    private static final int IV_LENGTH = 12; // Recomendado: 12 bytes
    private static final int TAG_LENGTH = 128; // 128 bits = 16 bytes

    private final ThreadLocal<Cipher> cipherThreadLocal = ThreadLocal.withInitial(() -> {
        try {
            return Cipher.getInstance(ENCRYPTION_ALGORITHM);
        } catch (Exception e) {
            throw new RuntimeException("Error initializing Cipher", e);
        }
    });

    @Override
    public byte @NotNull [] encrypt(final byte @NotNull [] data, @NotNull final SecretKey secretKey) {
        try {
            Cipher cipher = cipherThreadLocal.get();
            byte[] iv = new byte[IV_LENGTH];
            new SecureRandom().nextBytes(iv);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, new GCMParameterSpec(TAG_LENGTH, iv));
            byte[] ciphertext = cipher.doFinal(data);

            byte[] result = new byte[iv.length + ciphertext.length];
            System.arraycopy(iv, 0, result, 0, iv.length);
            System.arraycopy(ciphertext, 0, result, iv.length, ciphertext.length);
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Error during encryption", e);
        }
    }

    @Override
    public byte @NotNull [] decrypt(final byte @NotNull [] data, @NotNull final SecretKey secretKey) {
        try {
            Cipher cipher = cipherThreadLocal.get();

            byte[] iv = Arrays.copyOfRange(data, 0, IV_LENGTH);
            byte[] ciphertext = Arrays.copyOfRange(data, IV_LENGTH, data.length);

            cipher.init(Cipher.DECRYPT_MODE, secretKey, new GCMParameterSpec(TAG_LENGTH, iv));

            return cipher.doFinal(ciphertext);
        } catch (AEADBadTagException e) {
            throw new DataIntegrityCheckFailedException(
                    "The tag integrity check failed, the data has been tampered", e);
        } catch (Exception e) {
            throw new RuntimeException("Error during decryption", e);
        }
    }
}
