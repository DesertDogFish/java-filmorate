package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    private static final String URL = "/users";
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserService userService;
    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setEmail("test@test.com");
        user.setLogin("test");
        user.setName("Test");
        user.setBirthday(LocalDate.now());
    }

    @Test
    public void testCreateWhenUserIsValidThenReturnOkRequest() throws Exception {
        user.setId(1);
        Mockito.when(userService.create(user)).thenReturn(user);
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
    public void testCreateWhenUserIsValidThenReturnOkRequest_EmptyName() throws Exception {
        User resultUser = new User();
        resultUser.setId(1);
        resultUser.setEmail("test@test.com");
        resultUser.setLogin("test");
        resultUser.setName("test");
        resultUser.setBirthday(LocalDate.now());
        user.setName(null);

        Mockito.when(userService.create(user)).thenReturn(resultUser);

        mvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(user))).andExpect(status().isOk()).andExpect(content().json(objectMapper.writeValueAsString(resultUser)));
    }

    @Test
    public void testFindAllWhenUsersExistThenReturnUsersWith200Status() throws Exception {
        User user1 = new User();
        user1.setEmail("test@test.com");
        user1.setLogin("test");
        user1.setName("Test");
        user1.setBirthday(LocalDate.now());

        User user2 = new User();
        user2.setEmail("test2@test.com");
        user2.setLogin("test2");
        user2.setName("Test2");
        user2.setBirthday(LocalDate.now().minusDays(1));

        user1.setId(1);
        user2.setId(2);
        List<User> users = Arrays.asList(user1, user2);

        Mockito.when(userService.findAll()).thenReturn(users);

        mvc.perform(get(URL)).andExpect(status().isOk()).andExpect(content().json(objectMapper.writeValueAsString(users)));
    }

    @Test
    public void testPutThenUpdateUserWith200Status() throws Exception {
        User user1 = new User();
        user1.setEmail("test@test.com");
        user1.setLogin("test");
        user1.setName("Test");
        user1.setBirthday(LocalDate.now());

        Mockito.when(userService.put(user)).thenReturn(user);

        mvc.perform(put(URL).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(user1))).andExpect(status().isOk()).andExpect(content().json(objectMapper.writeValueAsString(user1)));
    }

    @Test
    public void testPutWhenUserDoesNotExistThenReturnClientError() throws Exception {
        user.setId(777);
        Mockito.when(userService.put(user)).thenThrow(new ValidationException("Invalid User"));
        mvc.perform(put(URL).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(user))).andExpect(status().is4xxClientError());
    }

    @Test
    public void testAddFriendWhenFriendIsAddedThenReturnUserWith200Status() throws Exception {
        User friend = new User();
        friend.setId(2);
        friend.setEmail("friend@test.com");
        friend.setLogin("friend");
        friend.setName("Friend");
        friend.setBirthday(LocalDate.now().minusYears(5));

        user.getFriends().add(friend.getId());

        Mockito.when(userService.addFriend(user.getId(), friend.getId())).thenReturn(user);

        mvc.perform(put(URL + "/" + user.getId() + "/friends/" + friend.getId()).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(content().json(objectMapper.writeValueAsString(user)));
    }

    @Test
    public void testRemoveFriendWhenFriendIsRemovedThenReturnUserWith200Status() throws Exception {
        User friend = new User();
        friend.setId(2);
        friend.setEmail("friend@test.com");
        friend.setLogin("friend");
        friend.setName("Friend");
        friend.setBirthday(LocalDate.now().minusYears(5));

        user.getFriends().remove(friend.getId());

        Mockito.when(userService.removeFriend(user.getId(), friend.getId())).thenReturn(user);

        mvc.perform(delete(URL + "/" + user.getId() + "/friends/" + friend.getId()).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(content().json(objectMapper.writeValueAsString(user)));
    }

    @Test
    public void testPrintFriendsWhenFriendsAreRetrievedThenReturnListOfUsersWith200Status() throws Exception {
        List<User> friends = Arrays.asList(user);

        Mockito.when(userService.getFriends(user.getId())).thenReturn(friends);

        mvc.perform(get(URL + "/" + user.getId() + "/friends").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(content().json(objectMapper.writeValueAsString(friends)));
    }

    @Test
    public void testPrintCommonFriendsWhenCommonFriendsAreRetrievedThenReturnListOfUsersWith200Status() throws Exception {
        User otherUser = new User();
        otherUser.setId(3);
        otherUser.setEmail("other@test.com");
        otherUser.setLogin("other");
        otherUser.setName("Other");
        otherUser.setBirthday(LocalDate.now().minusYears(10));

        List<User> commonFriends = Arrays.asList(user);

        Mockito.when(userService.getCommonFriends(user.getId(), otherUser.getId())).thenReturn(commonFriends);

        mvc.perform(get(URL + "/" + user.getId() + "/friends/common/" + otherUser.getId()).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(content().json(objectMapper.writeValueAsString(commonFriends)));
    }
}
