package weather.app.weather_app.dao;

import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import weather.app.weather_app.models.Location;
import weather.app.weather_app.models.User;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CityDAO {
    private final SessionFactory sessionFactory;

    public void saveCityLocation(User findUser, Location location) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        User user = session.get(User.class, findUser.getId());
        user.addLocation(location);

        session.getTransaction().commit();
    }

    public List<Location> getCitiesCoordinates(User currentUser) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        List<Location> locations = session.createQuery(
                        "SELECT l FROM Location l WHERE l.user.id = :userId", Location.class)
                .setParameter("userId", currentUser.getId())
                .getResultList();

        session.getTransaction().commit();
        return locations;
    }

    public void deleteCity(User currentUser, Location location) {
        double lat = location.getLatitude();
        double lon = location.getLongitude();
        double tolerance = 0.5;

        Session session = sessionFactory.getCurrentSession();
        Location locationToDelete = session.createQuery(
                        "SELECT l FROM Location l WHERE l.latitude BETWEEN :minLat AND :maxLat " +
                                "AND l.longitude BETWEEN :minLon AND :maxLon " +
                                "AND l.user.id = :userId",
                        Location.class)
                .setParameter("minLat", lat - tolerance)
                .setParameter("maxLat", lat + tolerance)
                .setParameter("minLon", lon - tolerance)
                .setParameter("maxLon", lon + tolerance)
                .setParameter("userId", currentUser.getId())
                .uniqueResult();

        session.beginTransaction();

        User user = session.get(User.class, currentUser.getId());
        user.getLocations().removeIf(loc -> loc.getId() == locationToDelete.getId());
        session.remove(locationToDelete);

        session.getTransaction().commit();
    }
}
