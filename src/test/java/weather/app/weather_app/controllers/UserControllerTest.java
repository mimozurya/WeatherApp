package weather.app.weather_app.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import weather.app.weather_app.models.User;
import weather.app.weather_app.services.SessionService;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private SessionService sessionService;

    @Mock
    private Model model;

    @InjectMocks
    private UserController userController;

    @Test
    void signUpPageTest_sessionIdIsNull_returnsSignUp() {
        // given
        UUID nullSessionId = null;

        // when
        String result = userController.signUp(nullSessionId, model);

        // then
        assertEquals("registration/sign-up", result);
    }

    @Test
    void signUpPageTest_sessionIdIsNotNull_returnsSignIn() {
        // given
        UUID sessionId = UUID.randomUUID();

        // when
        String result = userController.signUp(sessionId, model);

        // then
        assertEquals("redirect:/", result);
    }

    @Test
    void signInPageTest_sessionIdIsNull_returnsSignIn() {
        // given
        UUID nullSessionId = null;

        // when
        String result = userController.signIn(nullSessionId, model);

        // then
        assertEquals("login/sign-in", result);
        verify(model).addAttribute(eq("user"), any(User.class));
    }

    @Test
    void indexPageTest_noSession_redirectsToSignIn() {
        // given
        UUID nullSessionId = null;

        // when
        String result = userController.index(nullSessionId, model);

        // then
        assertEquals("redirect:/sign-in", result);
    }
}
