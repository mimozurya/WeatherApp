package weather.app.weather_app.dao;

import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import weather.app.weather_app.models.User;

@Repository
@RequiredArgsConstructor
public class UserDAO {
    private final SessionFactory sessionFactory;

    public void saveNewUser(User user) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        session.persist(user);

        session.getTransaction().commit();
    }

    public User findUserByLogin(String login) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        String hql = "SELECT u FROM User u WHERE u.login=:login";
        Query<User> query = session.createQuery(hql, User.class);
        query.setParameter("login", login);

        session.getTransaction().commit();

        return query.uniqueResult();
    }
}
