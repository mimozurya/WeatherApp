package weather.app.weather_app.services;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletResponse;
import weather.app.weather_app.controllers.UserController;
import weather.app.weather_app.dao.SessionDAO;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static weather.app.weather_app.utils.AuthUtils.COOKIE_NAME;

@ExtendWith(MockitoExtension.class)
public class SessionServiceTest {

    @Mock
    private SessionDAO sessionDAO;

    @InjectMocks
    private SessionService sessionService;

    @Test
    void deleteSessionCookie_createsEmptyCookie() {
        //when
        Cookie cookie = sessionService.deleteSessionCookie();

        //then
        assertEquals(COOKIE_NAME, cookie.getName());
        assertEquals("", cookie.getValue());
        assertEquals(0, cookie.getMaxAge());
    }

    @Test
    void deleteSession_callsDAOMethod() {
        //given
        UUID sessionId = UUID.randomUUID();
        HttpServletResponse response = new MockHttpServletResponse();

        //when
        sessionService.deleteSession(sessionId, response);

        //then
        verify(sessionDAO).deleteSessionById(sessionId);
    }
}
