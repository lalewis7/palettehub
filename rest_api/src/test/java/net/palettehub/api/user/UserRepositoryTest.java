package net.palettehub.api.user;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(UserRepository.class)
public class UserRepositoryTest {

    @Autowired
     private TestEntityManager em;
    
    @Autowired
    private UserRepository underTest;

    @Test
    void checkCreateUser(){
        User user = new User();
        user.setUserId("userId");
        user.setGoogleId("googleId");
        underTest.createUser(user);

        int size = em.getEntityManager().createQuery("SELECT * FROM users WHERE user_id = userId").getResultList().size();

        assertEquals(size, 1);
    }

    @Test
    void checkUserIdExists(){}

    @Test
    void checkUserIdDoesNotExist(){}

    @Test
    void checkUserGoogleIdExists(){}

    @Test
    void checkUserGoogleIdDoesNotExist(){}

    @Test
    void checkUserLikedPalettes(){}

}
