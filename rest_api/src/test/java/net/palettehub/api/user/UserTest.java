package net.palettehub.api.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import net.palettehub.api.AppTest;
import net.palettehub.api.jwt.JwtUtil;

public class UserTest {
    
    /**
     * Create a User object with dummy data for testing.
     * @return User object with dummy data.
     */
    public static User[] getSampleUsers(int len){
        User[] users = new User[len];
        for (int i = 0; i < users.length; i++){
            User user = new User();
            user.setUserId(AppTest.generateRandomString(32, AppTest.LETTERS_NUMBERS));
            user.setGoogleId(AppTest.generateRandomString(24, AppTest.NUMBERS));
            user.setEmail(AppTest.generateRandomString(10, AppTest.LETTERS)+"@example.com");
            user.setName(AppTest.generateRandomString(8, AppTest.LETTERS) + " " + AppTest.generateRandomString(6, AppTest.LETTERS));
            user.setPictureUrl("https://www.example.com/"+AppTest.generateRandomString(20, AppTest.LETTERS));
            user.setBannerColorLeft(AppTest.generateRandomString(6, AppTest.HEX));
            user.setBannerColorRight(AppTest.generateRandomString(6, AppTest.HEX));
            user.setRole("user");
            users[i] = user;
        }
        return users;
    }

    /**
     * Creates and returns a PrepraredStatement to insert a user into the users table.
     * @param conn SQL connection
     * @param user User object to insert
     * @return PreparedStatement to insert user ready to be executed.
     * @throws SQLException
     */
    public static PreparedStatement insertUserPreparedStatement(Connection conn, User user) throws SQLException{
        PreparedStatement ps = conn.prepareStatement("INSERT INTO users (user_id, google_id, email, name, picture_url, show_picture, role, banner_color_1, banner_color_2) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
        ps.setString(1, user.getUserId());
        ps.setString(2, user.getGoogleId());
        ps.setString(3, user.getEmail());
        ps.setString(4, user.getName());
        ps.setString(5, user.getPictureUrl());
        ps.setBoolean(6, user.getShowPicture());
        ps.setString(7, user.getRole());
        ps.setString(8, user.getBannerColorLeft());
        ps.setString(9, user.getBannerColorRight());
        return ps;
    }

    /**
     * Mocks the user as being authenticated and sets the SecurityContextHolder context authentication.
     * @param user authenticated user
     * @param jwtUtil jwt utilities
     */
    public static void loginUser(User user, JwtUtil jwtUtil) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    user.getUserId(), null, jwtUtil.getAuthorities(user.getRole()));
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }

}
