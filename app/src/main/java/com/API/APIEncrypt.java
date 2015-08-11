package com.API;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class APIEncrypt {
    private final static String[] hexDigits = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
            "A", "B", "C", "D", "E", "F"};

    private static byte[] hexStringToByteArray(String hexString) {
        int len = hexString.length() / 2;
        byte[] result = new byte[len];
        try {
            for (int i = 0; i < len; i++)
                result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2), 16).byteValue();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static String byteArrayToHexString(byte[] b) {
        StringBuilder resultSb = new StringBuilder();
        for (byte aB : b) {
            resultSb.append(byteToHexString(aB));
        }
        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = (b < 0) ? b + 256 : b;
        return hexDigits[(n >> 4) & 0x0f] + hexDigits[n & 0x0f];
    }


    public static class MD5 {
        public static String encode(String origin) throws Exception {
            String resultString = null;

            try {
                resultString = origin;
                MessageDigest md = MessageDigest.getInstance("MD5");
                resultString = byteArrayToHexString(md.digest(resultString
                        .getBytes()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultString;
        }
    }

    public static class AES {
        /**
         * AES加密
         */
        public static String encrypt(String seed, String cleartext) throws Exception {
            try {
                if(cleartext == null)
                    return null;
                byte[] rawKey = getRawKey(seed.getBytes());
                byte[] result = encrypt(rawKey, cleartext.getBytes());
                return byteArrayToHexString(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * AES解密
         */
        public static String decrypt(String seed, String encrypted) throws Exception {
            try {
                if (encrypted == null)
                    return null;
                byte[] rawKey = getRawKey(seed.getBytes());
                byte[] enc = hexStringToByteArray(encrypted);
                byte[] result = decrypt(rawKey, enc);
                return new String(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        private static byte[] getRawKey(byte[] seed) throws Exception {
            try {
                KeyGenerator keygen = KeyGenerator.getInstance("AES");
                SecureRandom sr = null;
                if (android.os.Build.VERSION.SDK_INT >= 17) {
                    sr = SecureRandom.getInstance("SHA1PRNG", "Crypto");
                } else {
                    sr = SecureRandom.getInstance("SHA1PRNG");
                }
                sr.setSeed(seed);
                keygen.init(128, sr); // 192 and 256 bits may not be available
                SecretKey skey = keygen.generateKey();
                byte[] raw = skey.getEncoded();
                return raw;
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            return null;
        }


        private static byte[] encrypt(byte[] raw, byte[] clear) throws NoSuchAlgorithmException,
                NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
            try {
                SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
                Cipher cipher = Cipher.getInstance("AES");
                cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
                byte[] encrypted = cipher.doFinal(clear);
                return encrypted;
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            }
            return null;
        }

        private static byte[] decrypt(byte[] raw, byte[] encrypted) throws NoSuchAlgorithmException,
                NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
            try {
                SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
                Cipher cipher = Cipher.getInstance("AES");
                cipher.init(Cipher.DECRYPT_MODE, skeySpec);
                byte[] decrypted = cipher.doFinal(encrypted);
                return decrypted;
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}