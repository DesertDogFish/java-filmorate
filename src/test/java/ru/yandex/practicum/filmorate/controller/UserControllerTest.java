package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.annotation.DirtiesContext.MethodMode;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;
    private static final String URL = "/users";

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setEmail("test@test.com");
        user.setLogin("test");
        user.setName("Test");
        user.setBirthday(LocalDate.now());
    }

    @Test
    @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
    public void testCreateWhenUserIsValidThenReturnOkRequest() throws Exception {
        user.setId(1);
        mvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(user))).andExpect(status().isOk()).andExpect(content().json(objectMapper.writeValueAsString(user)));
    }

    @Test
    public void testCreateWhenUserIsNotValidThenReturnBadRequest_Email() throws Exception {
        user.setEmail("invalid email");
        mvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(user))).andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateWhenUserIsNotValidThenReturnBadRequest_EmptyLogin() throws Exception {
        user.setLogin("");
        mvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(user))).andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateWhenUserIsNotValidThenReturnBadRequest_LoginWithSpaces() throws Exception {
        user.setLogin("lo gin");
        mvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(user))).andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateWhenUserIsNotValidThenReturnBadRequest_BirthDateInFuture() throws Exception {
        user.setBirthday(LocalDate.now().plusDays(1));
        mvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(user))).andExpect(status().isBadRequest());
    }

    @Test
    @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
    public void testCreateWhenUserIsValidThenReturnOkRequest_EmptyName() throws Exception {
        User resultUser = new User();
        resultUser.setId(1);
        resultUser.setEmail("test@test.com");
        resultUser.setLogin("test");
        resultUser.setName("test"); // name will be taken from login
        resultUser.setBirthday(LocalDate.now());

        user.setName(null);
        mvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(user))).andExpect(status().isOk()).andExpect(content().json(objectMapper.writeValueAsString(resultUser)));
    }

    @Test
    @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
    public void testFindAllWhenUsersExistThenReturnUsersWith200Status() throws Exception {
        User user1 = new User();
        user1.setEmail("test@test.com");
        user1.setLogin("test");
        user1.setName("Test");
        user1.setBirthday(LocalDate.now());

        mvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(user1)));

        User user2 = new User();
        user2.setEmail("test2@test.com");
        user2.setLogin("test2");
        user2.setName("Test2");
        user2.setBirthday(LocalDate.now().minusDays(1));

        mvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(user2)));

        user1.setId(1);
        user2.setId(2);
        List<User> users = Arrays.asList(user1, user2);

        mvc.perform(get(URL)).andExpect(status().isOk()).andExpect(content().json(objectMapper.writeValueAsString(users)));
    }

    @Test
    @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
    public void testPutWhenUserExistsThenUpdateUserWith200Status() throws Exception {
        User user1 = new User();
        user1.setEmail("test@test.com");
        user1.setLogin("test");
        user1.setName("Test");
        user1.setBirthday(LocalDate.now());

        mvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(user1)));

        user1.setId(1);
        user1.setEmail("test@test123.com");
        user1.setLogin("test123");
        user1.setName("Test123");

        mvc.perform(put(URL).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(user1))).andExpect(status().isOk()).andExpect(content().json(objectMapper.writeValueAsString(user1)));
    }

    @Test
    public void testPutWhenUserDoesNotExistThenReturnServerError() throws Exception {
        user.setId(777);
        mvc.perform(put(URL).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(user))).andExpect(status().is5xxServerError());
    }
}
