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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private SessionService sessionService;

    @Mock
    private Model model;

    @InjectMocks
    private UserController userController;

    @Test
    void signUpPageTest_sessionIdIsNull_returnsSignUpPage() {
        // given
        UUID nullSessionId = null;

        // when
        String result = userController.signUpPage(nullSessionId, model);

        // then
        assertEquals("registration/sign-up", result);
    }

    @Test
    void signUpPageTest_sessionIdIsNotNull_returnsSignInPage() {
        // given
        UUID sessionId = UUID.randomUUID();

        // when
        String result = userController.signUpPage(sessionId, model);

        // then
        assertEquals("redirect:/", result);
    }

    @Test
    void signInPageTest_sessionIdIsNull_returnsSignInPage() {
        // given
        UUID nullSessionId = null;

        // when
        String result = userController.signInPage(nullSessionId, model);

        // then
        assertEquals("login/sign-in", result);
        verify(model).addAttribute(eq("user"), any(User.class));
    }

    @Test
    void indexPageTest_noSession_redirectsToSignIn() {
        // given
        UUID nullSessionId = null;

        // when
        String result = userController.indexPage(nullSessionId, model);

        // then
        assertEquals("redirect:/sign-in", result);
    }

    @Test
    void indexPageTest_invalidSession_redirectsToSignIn() {
        // given
        UUID invalidSessionId = UUID.randomUUID();
        when(sessionService.getUserBySessionId(invalidSessionId)).thenReturn(null);

        // when
        String result = userController.indexPage(invalidSessionId, model);

        // then
        assertEquals("redirect:/sign-in", result);
        verify(sessionService).getUserBySessionId(invalidSessionId);
    }
}
