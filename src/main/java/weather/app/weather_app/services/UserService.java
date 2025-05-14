package weather.app.weather_app.services;

import org.springframework.stereotype.Service;
import weather.app.weather_app.dao.UserDAO;
import weather.app.weather_app.models.User;
import weather.app.weather_app.util.PasswordUtil;

@Service
public class UserService {
    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public String registerNewUser(User user) {
        if (userDAO.findUserByLogin(user.getLogin()) == null) {
            user.setPassword(PasswordUtil.hashPassword(user.getPassword()));
            userDAO.saveNewUser(user);
            return "redirect:/sign-in";
        }

        return "redirect:/sign-up-with-errors";
    }

    public User authorizationUser(User user) {
        User foundUser = userDAO.findUserByLogin(user.getLogin());
        if (foundUser != null && PasswordUtil.checkPassword(user.getPassword(), foundUser.getPassword())) {
            return foundUser;
        }

        return null;
    }
}
