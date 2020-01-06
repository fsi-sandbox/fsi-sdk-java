package com.github.enyata.config;


import com.github.enyata.aspect.Log;
import com.github.enyata.exceptions.EncryptionException;
import com.github.enyata.util.StringUtils;
import com.github.enyata.vo.ResetCredential;
import org.springframework.context.annotation.Configuration;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Configuration
public class SecurityConfiguration {
    private Cipher cipherEncrypt, cipherDecrypt;
    private ResetCredential resetCredential;
    /**
     * This method encrypts data passed to it with the existing EncryptionCiphers
     * which was earlier set by calling the setEncryptionCiphers method.
     * You need to call the setEncryptionCiphers at least once for it to work.
     * @param input
     * @return Returns the input value
     * @throws EncryptionException
     */
    @Log
    public  String encrypt(String input) throws EncryptionException {
        if (!StringUtils.isBlank(input) && cipherEncrypt != null) {
            try {
                byte[] encrypted = cipherEncrypt.doFinal(input.getBytes());
                return StringUtils.bytesToHex(encrypted);
            } catch ( IllegalBlockSizeException | BadPaddingException e) {
                throw new EncryptionException("Error occurred encrypting details" , e);

            }
        }

        throw new EncryptionException("Error occurred encrypting. Trying to encrypt null value");
    }

    /**
     * This method encrypt data passed. It creates new Cipher every time the method is called.
     * The cipher is not reused.
     * @param input
     * @param encryptionKey
     * @param ivKey
     * @return
     * @throws EncryptionException
     */
    @Log
    public  String encrypt(String input, String encryptionKey, String ivKey) throws EncryptionException {

        if (StringUtils.isBlank(input)) {
            throw new EncryptionException("Error occurred encrypting. Trying to encrypt null value");
        }

        if (StringUtils.isBlank(encryptionKey) || StringUtils.isBlank(ivKey)) {
            throw new EncryptionException("Either ivkey or encryption key is not provided. Please call the reset endpoint first");
        }

        try {
            SecretKeySpec encryptionSpec = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");
            IvParameterSpec ivKeySpec = new IvParameterSpec(ivKey.getBytes("UTF-8"));

            Cipher cipherEncrypt = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipherEncrypt.init(Cipher.ENCRYPT_MODE, encryptionSpec, ivKeySpec);
            byte[] encrypted = cipherEncrypt.doFinal(input.getBytes());
            return StringUtils.bytesToHex(encrypted);
        } catch (IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException e) {
            throw new EncryptionException("Error occurred encrypting details" , e);

        }
    }

    /**
     * This method encrypt data passed. It creates new Ciphers every time the method is called.
     * The ciphers are not reused.
     * @param input
     * @param encryptionKey
     * @param ivKey
     * @return decryped string
     * @throws EncryptionException
     */
    @Log
    public String decrypt(String input, String encryptionKey, String ivKey) throws EncryptionException {
        if (StringUtils.isBlank(input)) {
            throw new EncryptionException("Error occurred encrypting. Trying to encrypt null or empty value");
        }

        if (StringUtils.isBlank(encryptionKey) || StringUtils.isBlank(ivKey)) {
            throw new EncryptionException("Either ivkey or encryption key is not provided. Please call the reset endpoint first");
        }

        try {
            SecretKeySpec encryptionSpec = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");
            IvParameterSpec ivKeySpec = new IvParameterSpec(ivKey.getBytes("UTF-8"));
            cipherDecrypt = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipherDecrypt.init(Cipher.DECRYPT_MODE, encryptionSpec, ivKeySpec);
            byte[] original = cipherDecrypt.doFinal(StringUtils.hexStringToByteArray(input));
            return new String(original);
        } catch (IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException e) {
            throw new EncryptionException("Error occurred decrypting details" , e);
        }

    }

    /**
     * This method encrypts data passed to it with the existing DecryptionCipher
     * which was earlier set by calling the setEncryptionCiphers method.
     * You need to call the setEncryptionCiphers at least once for it to work.
     * @param input
     * @return Returns the input value
     * @throws EncryptionException
     */
    @Log
    public String decrypt(String input) throws EncryptionException {
        if (!StringUtils.isBlank(input) && cipherDecrypt != null) {
            try {
                byte[] original = cipherDecrypt.doFinal(StringUtils.hexStringToByteArray(input));
                return new String(original);
            } catch ( IllegalBlockSizeException | BadPaddingException e) {
                throw new EncryptionException("Error occurred decrypting details" , e);
            }
        }
        throw new EncryptionException("Error occurred encrypting. Trying to encrypt null value");
    }


    /**
     * This method encrypts using SHA-256 without using init-vector
     * @param input
     * @return byte array
     * @throws EncryptionException
     */
    @Log
    public String encryptWithoutIV(String input) throws EncryptionException {

        if (!StringUtils.isBlank(input)) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                return StringUtils.toHexString((md.digest(input.getBytes())));
            } catch ( NoSuchAlgorithmException e) {
                throw new EncryptionException("Error occurred encryption input" , e);
            }
        }
        throw new EncryptionException("Error occurred getting sha256 encryption. Trying to encrypt null or empty value");
    }

    /**
     * This method call caches the EncryptionCiphers using credentials gotten from calling the /Reset endpoint
     * or loading from an external source into memory, since the key may not change often.
     *
     * @param resetCredential
     * @throws EncryptionException
     */

    private void setEncryptionCiphers(ResetCredential resetCredential) throws EncryptionException {

        if (resetCredential == null || StringUtils.isBlank(resetCredential.getAesKey()) || StringUtils.isBlank(resetCredential.getIvKey())) {
            throw new EncryptionException("Either of ivkey or encryption key is not provided. Please call the reset endpoint first");
        }


        try {
            SecretKeySpec encryptionKey = new SecretKeySpec(resetCredential.getAesKey().getBytes("UTF-8"), "AES");
            IvParameterSpec ivKey = new IvParameterSpec(resetCredential.getIvKey().getBytes("UTF-8"));

            cipherEncrypt = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipherEncrypt.init(Cipher.ENCRYPT_MODE, encryptionKey, ivKey);

            cipherDecrypt = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipherDecrypt.init(Cipher.DECRYPT_MODE, encryptionKey, ivKey);
            this.resetCredential = resetCredential;
        } catch ( UnsupportedEncodingException | NoSuchPaddingException | NoSuchAlgorithmException|InvalidAlgorithmParameterException | InvalidKeyException e) {
            throw new EncryptionException("Error occurred setting encryption details" , e);
        }
    }

    public ResetCredential getResetCredential() {
        return resetCredential;
    }

    public void setResetCredential(ResetCredential resetCredential) throws EncryptionException {
        setEncryptionCiphers(resetCredential);
        this.resetCredential = resetCredential;
    }
}