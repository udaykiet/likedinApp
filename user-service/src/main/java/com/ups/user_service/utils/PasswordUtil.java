package com.ups.user_service.utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    // hash the password for the first time
    public static String hashPassword(String plainTextPassword){
        return BCrypt.hashpw(plainTextPassword , BCrypt.gensalt());
    }

    // check that the plain password matched with the previously hashed password or not
    public static boolean checkPassword(String plainTextPassword  , String hashedPassword){
        return BCrypt.checkpw(plainTextPassword ,hashedPassword);
    }
}
