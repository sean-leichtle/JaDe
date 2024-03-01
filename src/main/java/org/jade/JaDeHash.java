package org.jade;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class JaDeHash {

    private byte[] calculateHashValue(File file) {
        MessageDigest messageDigest;
        try (FileInputStream fileInputStream = new FileInputStream(file.getAbsolutePath())) {
            messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] buffer = new byte[1024];
            int read;
            do {
                read = fileInputStream.read(buffer);
                if (read > 0) {
                    messageDigest.update(buffer, 0, read);
                }
            } while (read != -1);
        } catch (
                NoSuchAlgorithmException |
                IOException e) {
            throw new RuntimeException(e);
        }
        return messageDigest.digest();
    }

    private String hashToString(byte[] arr) {
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < arr.length; i++) {
            sb.append(Integer.toString((arr[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    String getHashValue(File file) {
        return hashToString(calculateHashValue(file));
    }
}
