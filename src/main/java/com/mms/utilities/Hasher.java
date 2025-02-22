/*
 * Copyright 2021 J.B. Slade.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mms.utilities;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author J.B. Slade
 */
public class Hasher {
    
    public static String getHash(String p, String s){
        byte [] salt = s.getBytes();
        byte [] pepper = "xSI$#Io!tX^bM4AN".getBytes();
        return getSecurePassword(getSecurePassword(p, salt), pepper);
    }
    
    public static String getSalt() {
        char[] password = new char[16];
        char[] validchars = {'A', 'a', 'B', 'b','C', 'c', 'D', 'd','E', 'e', 'F', 'f','G', 'g', 'H', 'h','I', 'i', 'J', 'j',
                             'K', 'k', 'L', 'l', 'M', 'm', 'N', 'n', 'O', 'o', 'P', 'p', 'Q', 'q', 'R', 'r', 'S', 's', 'T', 't',
                             'U', 'u', 'V', 'v', 'W', 'w', 'X', 'x', 'Y', 'y', 'Z', 'z', '!', '@', '#', '$', '%', '^', '&', '*',
                             '(', ')', '_', '-', '+', '=', ']', '[', ';', ':', '}', '{', '|', '\\', '/', '?', '<', '>', '.', ','};
        SecureRandom rand = new SecureRandom();
        for (int i = 0; i < 16; i++) {
            password[i] = validchars[rand.nextInt(validchars.length)];
        }
        return new String(password);
    }
    
    private static String getSecurePassword(String passwordToHash, byte[] pepper){
        String generatedPassword;
        // Create MessageDigest instance for MD5
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Hasher.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Add password bytes to digest
        md.update(pepper);
        //Get the hash's bytes
        byte[] bytes = md.digest(passwordToHash.getBytes());
        //This bytes[] has bytes in decimal format;
        //Convert it to hexadecimal format
        StringBuilder sb = new StringBuilder();
        for(int i=0; i< bytes.length ;i++)
        {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        //Get complete hashed password in hex format
        generatedPassword = sb.toString();
        return generatedPassword;
    }
}