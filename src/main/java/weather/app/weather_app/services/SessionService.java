package weather.app.weather_app.services;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import weather.app.weather_app.dao.SessionDAO;
import weather.app.weather_app.models.Session;
import weather.app.weather_app.models.User;

import java.time.LocalDateTime;
import java.util.UUID;

import static weather.app.weather_app.utils.AuthUtils.COOKIE_NAME;

@Service
@RequiredArgsConstructor
public class SessionService {
    private final int SESSION_DURATION_SECONDS = 3600;
    private final SessionDAO sessionDAO;

    public Cookie createSessionCookie(User user) {
        UUID sessionId = createSessionId(user);
        Cookie sessionCookie = new Cookie(COOKIE_NAME, sessionId.toString());

        sessionCookie.setMaxAge(SESSION_DURATION_SECONDS);
        sessionCookie.setPath("/");
        sessionCookie.setHttpOnly(true);

        return sessionCookie;
    }

    public Cookie deleteSessionCookie() {
        Cookie sessionCookie = new Cookie(COOKIE_NAME, "");
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
        if (sessionId != null) {
            return isSessionActive(sessionId) ? sessionDAO.findUserBySessionId(sessionId) : null;
        }

        return null;
    }

    public void deleteSession(UUID sessionId, HttpServletResponse response) {
        response.addCookie(deleteSessionCookie());
        sessionDAO.deleteSessionById(sessionId);
    }

    private boolean isSessionActive(UUID sessionId) {
        return sessionDAO.isSessionTimeExpired(sessionId, LocalDateTime.now());
    }
}
