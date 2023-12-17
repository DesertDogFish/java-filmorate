package ru.yandex.practicum.filmorate.dao.dbimpl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserDaoDbImplTest {
    private final JdbcTemplate jdbcTemplate;

    private final User user1 = User.builder()
            .email("email")
            .login("login")
            .name("name")
            .birthday(LocalDate.now())
            .friends(Set.of())
            .build();
    private final User user2 = User.builder()
            .email("email2")
            .login("login2")
            .name("name2")
            .birthday(LocalDate.now().minusDays(1))
            .friends(Set.of(1))
            .build();

    private UserDaoDbImpl userDao;

    @BeforeEach
    void beforeEach() {
        userDao = new UserDaoDbImpl(jdbcTemplate);
    }

    @Test
    void testGetAllUsers() {
        userDao.merge(user1);
        userDao.merge(user2);
        Map<Integer, User> expected = Map.of(1, user1, 2, user2);

        Map<Integer, User> users = userDao.get();

        assertEquals(2, users.size());
        assertEquals(users.get(1), user1);
        assertEquals(users.get(1).getId(), 1);
        assertEquals(users.get(2), user2);
        assertEquals(users.get(2).getId(), 2);
        assertThat(users).isNotNull().usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void testGetUserById() {
        userDao.merge(user1);

        User retrievedFilm = userDao.get(1);

        assertThat(retrievedFilm).isNotNull().usingRecursiveComparison().isEqualTo(user1);
    }

    @Test
    void testGetUserByIdNotFound() {
        assertThrows(UserNotFoundException.class, () -> userDao.get(1));
    }

    @Test
    void testMergeUserCreate() {
        User user = userDao.merge(user1);

        assertThat(user).isNotNull().usingRecursiveComparison().isEqualTo(user1);
        assertEquals(user.getId(), 1);
    }

    @Test
    void testMergeUserUpdate() {
        userDao.merge(user1);
        user1.setName("Updated User");
        user1.setLogin("Updated login");
        user1.setFriends(Set.of());
        user1.setBirthday(LocalDate.MIN);
        user1.setEmail("newMail");

        User updatedUser = userDao.merge(user1);

        assertThat(updatedUser).isNotNull().usingRecursiveComparison().isEqualTo(user1);
    }
}