package weather.app.weather_app.scheduling;

import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class SessionCleanupScheduler {
    private final SessionFactory sessionFactory;

    @Scheduled(fixedRate = 86400000)
    public void deleteExpiredSessions() {
        sessionFactory.getCurrentSession()
                .createQuery("DELETE FROM Session WHERE expiresAt < :now")
                .setParameter("now", LocalDateTime.now())
                .executeUpdate();
    }
}
