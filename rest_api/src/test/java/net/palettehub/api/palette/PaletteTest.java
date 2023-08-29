package net.palettehub.api.palette;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import net.palettehub.api.AppTest;

public class PaletteTest {
    
    public static Palette[] getSamplePalettes(int len){
        Palette[] palettes = new Palette[len];
        for (int i = 0; i < palettes.length; i++){
            Palette palette = new Palette();
            palette.setPaletteId(AppTest.generateRandomString(32, AppTest.LETTERS_NUMBERS));
            palette.setColor1(AppTest.generateRandomString(6, AppTest.HEX));
            palette.setColor2(AppTest.generateRandomString(6, AppTest.HEX));
            palette.setColor3(AppTest.generateRandomString(6, AppTest.HEX));
            palette.setColor4(AppTest.generateRandomString(6, AppTest.HEX));
            palette.setColor5(AppTest.generateRandomString(6, AppTest.HEX));
            palettes[i] = palette;
        }
        return palettes;
    }

    /**
     * Creates a PrepraredStatement to insert a user into the users table.
     * @param conn SQL connection
     * @param palette Palette object to insert
     * @return PreparedStatement to insert palette ready to be executed.
     * @throws SQLException
     */
    public static PreparedStatement insertPalettePreparedStatement(Connection conn, Palette palette) throws SQLException{
        PreparedStatement ps = conn.prepareStatement("INSERT INTO palettes " + 
            "(palette_id, user_id, color_1, color_2, color_3, color_4, color_5, posted_timestamp) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
        ps.setString(1, palette.getPaletteId());
        ps.setString(2, palette.getUserId());
        ps.setString(3, palette.getColor1());
        ps.setString(4, palette.getColor2());
        ps.setString(5, palette.getColor3());
        ps.setString(6, palette.getColor4());
        ps.setString(7, palette.getColor5());
        ps.setLong(8, palette.getPosted());
        return ps;
    }

    /**
     * Creates a PrepraredStatement to insert a like into the likes table.
     * @param conn SQL connection
     * @param userId user_id for like
     * @param paletteId palette_id for like
     * @return PreparedStatement to insert like ready to be executed.
     * @throws SQLException
     */
    public static PreparedStatement insertLikePreparedStatement(Connection conn, String userId, String paletteId) throws SQLException{
        PreparedStatement ps = conn.prepareStatement("INSERT INTO likes (user_id, palette_id) VALUES (?, ?)");
        ps.setString(1, userId);
        ps.setString(2, paletteId);
        return ps;
    }

    /**
     * Creates a PrepraredStatement to delete a like from the likes table.
     * @param conn SQL connection
     * @param userId user_id for like
     * @param paletteId palette_id for like
     * @return PreparedStatement to delete like ready to be executed.
     * @throws SQLException
     */
    public static PreparedStatement deleteLikPreparedStatement(Connection conn, String userId, String paletteId) throws SQLException{
        PreparedStatement ps = conn.prepareStatement("DELETE FROM likes WHERE user_id = ? AND palette_id = ?");
        ps.setString(1, userId);
        ps.setString(2, paletteId);
        return ps;
    }

}
