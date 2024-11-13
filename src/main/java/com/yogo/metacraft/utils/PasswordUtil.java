package com.yogo.metacraft.utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    // 비밀번호 해싱
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    // 비밀번호 검증
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}