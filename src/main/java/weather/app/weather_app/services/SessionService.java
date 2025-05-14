package weather.app.weather_app.services;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;
import weather.app.weather_app.controllers.UserController;
import weather.app.weather_app.dao.SessionDAO;
import weather.app.weather_app.models.Session;
import weather.app.weather_app.models.User;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class SessionService {
    private final int SESSION_DURATION_SECONDS = 3600;
    private final SessionDAO sessionDAO;

    public SessionService(SessionDAO sessionDAO) {
        this.sessionDAO = sessionDAO;
    }

    public Cookie createSessionCookie(User user) {
        UUID sessionId = createSessionId(user);
        Cookie sessionCookie = new Cookie(UserController.COOKIE_NAME, sessionId.toString());

        sessionCookie.setMaxAge(SESSION_DURATION_SECONDS);
        sessionCookie.setPath("/");
        sessionCookie.setHttpOnly(true);

        return sessionCookie;
    }

    public Cookie deleteSessionCookie() {
        Cookie sessionCookie = new Cookie(UserController.COOKIE_NAME, "");
        sessionCookie.setMaxAge(0);

        return sessionCookie;
    }

    private UUID createSessionId(User user) {
        UUID sessionId = UUID.randomUUID();
        LocalDateTime expiresAt = LocalDateTime.now().plusSeconds(SESSION_DURATION_SECONDS);
        Session session = new Session(sessionId, user, expiresAt);

        sessionDAO.saveSession(session);

        return sessionId;
    }

    public User getUserBySessionId(UUID sessionId) {
        return isSessionActive(sessionId) ? sessionDAO.findUserBySessionId(sessionId) : null;
    }

    public void deleteSession(UUID sessionId) {
        sessionDAO.deleteSessionById(sessionId);
    }

    private boolean isSessionActive(UUID sessionId) {
        return sessionDAO.isSessionTimeExpired(sessionId, LocalDateTime.now());
    }
}
