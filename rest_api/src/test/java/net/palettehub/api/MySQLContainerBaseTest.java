package net.palettehub.api;

import javax.sql.DataSource;

import org.junit.ClassRule;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.MountableFile;

import com.mysql.cj.jdbc.MysqlDataSource;

/**
 * Test containers class with mysql db container. All other test class should inherit this class.
 * 
 * @author Arthur Lewis
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class MySQLContainerBaseTest {

    private static String DBNAME="palette_hub";
    private static String USER="user";
    private static String PWD="pass";
    private static String MYSQL_IMAGE = "mysql:8";
 
    protected DataSource dataSource;

    public MySQLContainerBaseTest(){
        dataSource = getDataSource();
    }
    
    @ClassRule
    public static final MySQLContainer<?> mySQLContainer = new MySQLContainer<>(MYSQL_IMAGE)
            .withDatabaseName(DBNAME)
            .withUsername(USER)
            .withPassword(PWD)
            .withEnv("MYSQL_ROOT_PASSWORD", PWD)
            .withCopyFileToContainer(MountableFile.forClasspathResource("dump.sql"), "/docker-entrypoint-initdb.d/schema.sql");
    static {
        mySQLContainer.start();
    }

    @DynamicPropertySource
    static void sqlserverProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
    }

    private static DataSource getDataSource(){
        MysqlDataSource ds = new MysqlDataSource();
        ds.setDatabaseName(mySQLContainer.getDatabaseName());
        ds.setURL(mySQLContainer.getJdbcUrl());
        ds.setUser(mySQLContainer.getUsername());
        ds.setPassword(mySQLContainer.getPassword());
        return ds;
    }
 
}