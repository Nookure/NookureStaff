package com.nookure.staff.api.service;

import javax.crypto.SecretKey;
import org.jetbrains.annotations.NotNull;

public interface EncryptService {
    /**
     * Encrypts the given data with the given secret key.
     *
     * @param data      The data to encrypt.
     * @param secretKey The secret key to encrypt the data with.
     * @return The encrypted data.
     */
    byte @NotNull [] encrypt(byte @NotNull [] data, @NotNull final SecretKey secretKey);

    /**
     * Decrypts the given data with the given secret key.
     *
     * @param data      The data to decrypt.
     * @param secretKey The secret key to decrypt the data with.
     * @return The decrypted data.
     */
    byte @NotNull [] decrypt(byte @NotNull [] data, @NotNull final SecretKey secretKey);
}
