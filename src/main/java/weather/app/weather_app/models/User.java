package weather.app.weather_app.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(min = 3)
    private String login;

    @Size(min = 3)
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private List<Location> locations = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private List<Session> sessions = new ArrayList<>();

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public void addSession(Session session) {
        sessions.add(session);
        session.setUser(this);
    }

    public void addLocation(Location location) {
        locations.add(location);
        location.setUser(this);
    }
}
