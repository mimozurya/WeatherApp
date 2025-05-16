package weather.app.weather_app.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import weather.app.weather_app.dao.UserDAO;
import weather.app.weather_app.dto.UserResponse;
import weather.app.weather_app.models.User;
import weather.app.weather_app.utils.PasswordUtils;

import static weather.app.weather_app.utils.AuthUtils.SIGN_IN_URL;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserDAO userDAO;

    public String registerNewUser(UserResponse userResponse) {
        User user = new User(userResponse.login(), userResponse.password());
        if (userDAO.findUserByLogin(user.getLogin()) == null) {
            user.setPassword(PasswordUtils.hashPassword(user.getPassword()));
            userDAO.saveNewUser(user);
            return SIGN_IN_URL;
        }

        return "redirect:/sign-up-with-errors";
    }

    public User authorizeUser(UserResponse userResponse) {
        User user = new User(userResponse.login(), userResponse.password());
        User foundUser = userDAO.findUserByLogin(user.getLogin());
        if (foundUser != null && PasswordUtils.checkPassword(user.getPassword(), foundUser.getPassword())) {
            return foundUser;
        }

        return null;
    }
}
