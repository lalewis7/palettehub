package net.palettehub.api;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Spring boot test class with some utility methods.
 * 
 * @author Arthur Lewis
 */
public class AppTest {
    
    public static void deleteAllData(Connection conn) throws SQLException{
        String[] tables = {"likes", "palettes", "collection_palettes", "collections", "users"};
        for (int i = 0; i < tables.length; i++){
            PreparedStatement ps = conn.prepareStatement("DELETE FROM "+tables[i]);
            //ps.setString(1, tables[i]);
            ps.execute();
        }
    }

    public static final int LETTERS = 1;
    public static final int NUMBERS = 2;
    public static final int LETTERS_NUMBERS = 3;
    public static final int HEX = 4;

    public static String generateRandomString(int len, int charsAllowed, String[] cannotEqualStrings){
        String availableChars;
        switch (charsAllowed) {
            case LETTERS:
                availableChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
                break;
            case NUMBERS:
                availableChars = "1234567890";
                break;
            case LETTERS_NUMBERS:
                availableChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
                break;
            case HEX:
                availableChars = "ABCDEF1234567890";
                break;
            default:
                availableChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
                break;
        }
        char[] chars = new char[len];
        for (int i = 0; i < chars.length; i++){
            chars[i] = availableChars.charAt((int) (availableChars.length() * Math.random()));
        }
        for (int i = 0; i < cannotEqualStrings.length; i++){
            if (new String(chars).equals(cannotEqualStrings[i]))
                return generateRandomString(len, charsAllowed, cannotEqualStrings);
        }
        return new String(chars);
    }

    public static String generateRandomString(int len, int charsAllowed){
        return generateRandomString(len, charsAllowed, new String[0]);
    }

}