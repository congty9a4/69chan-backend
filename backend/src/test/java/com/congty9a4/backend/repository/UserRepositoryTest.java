package com.congty9a4.backend.repository;

import com.congty9a4.backend.entity.Userchan;
import com.congty9a4.backend.repository.jpa.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

// Using @DataJpaTest to test JPA repositories with an in-memory database
@Slf4j
@DataJpaTest(
        properties = {
                "spring.datasource.url=jdbc:h2:mem:testdb",
                "spring.jpa.hibernate.ddl-auto=create-drop",
                "sql.init.mode=never"
        }
)
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    private Userchan testUser;

    private List<Userchan> users;

    @BeforeEach
    public void setUp() {
        testUser = Userchan.builder()
                .username("testUser")
                .password("testPassword")
                .email("test@example.com")
                .isActive(true)
                .build();

        users = new java.util.ArrayList<>();

        for (int i = 0; i < 5; i++) {
            Userchan user = Userchan.builder()
                    .username("user" + i)
                    .password("password" + i)
                    .email("user" + i + "@example.com")
                    .isActive(true)
                    .build();
            users.add(user);
        }

        userRepository.saveAll(users);
        userRepository.save(testUser);
    }

    @AfterEach
    public void tearDown() {
        userRepository.delete(testUser);
    }

    @Test
    void givenUser_whenSaved_thenCanBeFoundById() {

        Userchan savedUser = userRepository.findById(testUser.getId()).orElse(null);
        assertNotNull(savedUser);
        assertEquals(testUser.getId(), savedUser.getId());
        assertEquals(testUser.getUsername(), savedUser.getUsername());
    }

    @Test
    void testFindAllUsers() {
        var local_users = userRepository.findAll();
        assertNotNull(local_users);
        assertEquals(local_users.size(), users.size() + 1);
    }

    @Test
    void testDeleteUser() {
        userRepository.delete(testUser);
        var deletedUser = userRepository.findById(testUser.getId()).orElse(null);
        assertNull(deletedUser);
    }

    @Test
    void throwExceptionWhenFindByIdNotFound() {
        var user = userRepository.findById(UUID.randomUUID()).orElse(null);
        assertThrows(NullPointerException.class, () -> {
            log.info("User: {}", user.getUsername());
        });
    }
}
