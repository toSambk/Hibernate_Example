package service;

import entity.User;
import entity.UserInfo;

import java.util.List;

public interface ApplicationUserService {

    boolean createUser(String login, String password);

    List<User> getUserList();

    boolean updateUser(String newLogin, String oldLogin, String password);

    boolean deleteUser(String login);

    User findByLogin(String login) throws Exception;

    List <UserInfo> getUserInfoList();

    boolean passwordCorrect (String login, String password);

    UserInfo getUserInfo (String login, String password) throws Exception;

}
