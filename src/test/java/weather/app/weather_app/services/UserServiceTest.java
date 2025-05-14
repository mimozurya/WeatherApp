package weather.app.weather_app.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import weather.app.weather_app.dao.UserDAO;
import weather.app.weather_app.models.User;
import weather.app.weather_app.util.PasswordUtil;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserDAO userDAO;

    @Mock
    private PasswordUtil passwordUtil;

    @InjectMocks
    private UserService userService;

    @Test
    void registerNewUser_validUser_returnSignInPage() {
        //given
        User user = new User(1, "test", "test");
        when(userDAO.findUserByLogin("test")).thenReturn(null);
        //when
        String result = userService.registerNewUser(user);
        //then
        assertEquals("redirect:/sign-in", result);
    }

    @Test
    void registerNewUser_invalidUser_returnSignUpPage() {
        //given
        User user = new User(1, "test", "test");
        when(userDAO.findUserByLogin("test")).thenReturn(user);
        //when
        String result = userService.registerNewUser(user);
        //then
        assertEquals("redirect:/sign-up", result);
    }

    @Test
    void authorizationUser_userNotFound_returnsNull() {
        // given
        User inputUser = new User(1, "unknown", "password");

        when(userDAO.findUserByLogin("unknown")).thenReturn(null);

        // when
        User result = userService.authorizationUser(inputUser);

        // then
        assertNull(result);
        verify(userDAO).findUserByLogin("unknown");
        verifyNoInteractions(passwordUtil);
    }
}
