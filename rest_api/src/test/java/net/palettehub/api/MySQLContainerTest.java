package net.palettehub.api;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests to see if the test containers mysql db is running.
 * 
 * @author Arthur Lewis
 */
public class MySQLContainerTest extends MySQLContainerBaseTest {
    
    @Test
    public void testMySQLContainerIsRunning() {
        assertTrue(mySQLContainer.isRunning());
    }

}
