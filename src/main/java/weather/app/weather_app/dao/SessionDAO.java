package weather.app.weather_app.dao;

import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import weather.app.weather_app.models.Session;
import weather.app.weather_app.models.User;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class SessionDAO {
    private final SessionFactory sessionFactory;

    public void saveSession(Session cookieSession) {
        org.hibernate.Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        User user = session.get(User.class, cookieSession.getUser().getId());
        user.addSession(cookieSession);
        session.persist(user);

        session.getTransaction().commit();
    }

    public User findUserBySessionId(UUID sessionId) {
        org.hibernate.Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        Session newSession = session.get(Session.class, sessionId);
        User user = session.get(User.class, newSession.getUser().getId());

        session.getTransaction().commit();

        return user;
    }

    public void deleteSessionById(UUID sessionId) {
        org.hibernate.Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        Session newSession = session.get(Session.class, sessionId);
        session.remove(newSession);

        session.getTransaction().commit();
    }

    public boolean isSessionTimeExpired(UUID sessionId, LocalDateTime localTime) {
        org.hibernate.Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        Session newSession = session.get(Session.class, sessionId);
        if (newSession.getExpiresAt().isBefore(localTime)) {
            return false;
        }

        session.getTransaction().commit();

        return true;
    }
}
