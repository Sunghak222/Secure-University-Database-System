package hk.polyu.comp.project3335.securedb.config;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

@Converter
public class AESEncryptConverter implements AttributeConverter<String, String> {

    private static final String SECRET = "secure_key_3335_secure_key_3335"; // 32 bytes key
    private static final String ALGO = "AES/CBC/PKCS5Padding";

    private final SecretKeySpec secretKey;

    public AESEncryptConverter() {
        this.secretKey = new SecretKeySpec(SECRET.getBytes(StandardCharsets.UTF_8), "AES");
    }

    @Override
    public String convertToDatabaseColumn(String plainValue) {
        try {
            if (plainValue == null) return null;

            // 1) IV 생성
            byte[] ivBytes = new byte[16];
            new SecureRandom().nextBytes(ivBytes);
            IvParameterSpec iv = new IvParameterSpec(ivBytes);

            // 2) AES 암호화
            Cipher cipher = Cipher.getInstance(ALGO);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
            byte[] encrypted = cipher.doFinal(plainValue.getBytes(StandardCharsets.UTF_8));

            // 3) "IV + CipherText" Base64 인코딩 후 저장
            byte[] combined = new byte[ivBytes.length + encrypted.length];
            System.arraycopy(ivBytes, 0, combined, 0, ivBytes.length);
            System.arraycopy(encrypted, 0, combined, ivBytes.length, encrypted.length);

            return Base64.getEncoder().encodeToString(combined);

        } catch (Exception e) {
            throw new RuntimeException("Encryption error", e);
        }
    }

    @Override
    public String convertToEntityAttribute(String dbValue) {
        try {
            if (dbValue == null) return null;

            byte[] decoded = Base64.getDecoder().decode(dbValue);

            // 앞 16 bytes = IV
            byte[] ivBytes = new byte[16];
            byte[] encryptedBytes = new byte[decoded.length - 16];

            System.arraycopy(decoded, 0, ivBytes, 0, 16);
            System.arraycopy(decoded, 16, encryptedBytes, 0, encryptedBytes.length);

            IvParameterSpec iv = new IvParameterSpec(ivBytes);

            Cipher cipher = Cipher.getInstance(ALGO);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);

            byte[] decrypted = cipher.doFinal(encryptedBytes);

            return new String(decrypted, StandardCharsets.UTF_8);

        } catch (Exception e) {
            throw new RuntimeException("Decryption error", e);
        }
    }
}
