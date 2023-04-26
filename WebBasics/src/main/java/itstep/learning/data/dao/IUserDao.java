package itstep.learning.data.dao;

import itstep.learning.data.entity.User;
import itstep.learning.model.UserModel;

import javax.annotation.Nonnull;
import java.util.List;

public interface IUserDao {
    List<User> getAll();
    boolean add(@Nonnull UserModel model);
    boolean isLoginUse(String login);
    User getUserByCredentials(String login, String password);
    User getUserByLogin(String login);
    User getUserProfile(String login);
    boolean updateName(User user);
    boolean updateEmail(User user);
    boolean updateAvatar(User user);
}